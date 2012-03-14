/*
 * Copyright 2011 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jayway.jaxrs.hateoas.core;

import com.jayway.jaxrs.hateoas.*;
import com.jayway.jaxrs.hateoas.core.HateoasResponse.HateoasResponseBuilder;
import com.jayway.jaxrs.hateoas.support.AtomRels;
import com.jayway.jaxrs.hateoas.support.FieldPath;
import com.jayway.jaxrs.hateoas.support.ReflectionUtils;
import com.jayway.jaxrs.hateoas.web.RequestContext;
import com.sun.jersey.api.view.Viewable;
import com.sun.jersey.core.header.OutBoundHeaders;
import com.sun.jersey.core.spi.factory.ResponseImpl;

import javax.ws.rs.core.*;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.*;
import java.util.Map.Entry;

/**
 * Default implementation of {@link HateoasResponseBuilder}.
 *
 * @author Mattias Hellborg Arthursson
 * @author Kalle Stenflo
 */
public class HateoasResponseBuilderImpl extends HateoasResponse.HateoasResponseBuilder {

    private Response.StatusType statusType = Response.Status.NO_CONTENT;

    private OutBoundHeaders headers;

    private Object entity;

    private Type entityType;

    private final Map<FieldPath, ChainedLinkProducer> linkMappings = new HashMap<FieldPath, ChainedLinkProducer>();

    @Override
    public HateoasResponseBuilder link(String id, String rel, Object... params) {
        return links(HateoasResponseBuilder.makeLink(id, rel, params));
    }

    @Override
    public HateoasResponseBuilder link(HateoasContext context, String id, String rel, Object... params) {
        return links(HateoasResponseBuilder.makeLink(context, id, rel, params));
    }

    @Override
    public HateoasResponseBuilder link(FieldPath fieldPath, String id, String rel, String... entityFields) {
        return link(null, fieldPath, id, rel, entityFields);
    }

    @Override
    public HateoasResponseBuilder link(HateoasContext context, FieldPath fieldPath, String id, String rel, String... entityFields) {
        return link(fieldPath, new ReflectionBasedLinkProducer(context, id, rel, entityFields));
    }

    @Override
    public HateoasResponseBuilder link(FieldPath fieldPath, LinkProducer<?> linkProducer) {
    	if(!linkMappings.containsKey(fieldPath)) {
            linkMappings.put(fieldPath, new ChainedLinkProducer());
        }

        ChainedLinkProducer chainedLinkProducer = linkMappings.get(fieldPath);
        chainedLinkProducer.append(linkProducer);

        return this;
    }

    @Override
    public HateoasResponseBuilder selfLink(String id, Object... params) {
        return link(id, AtomRels.SELF, params);
    }

    @Override
    public HateoasResponseBuilder selfLink(HateoasContext context, String id, Object... params) {
        return link(context, id, AtomRels.SELF, params);
    }

    @Override
    public HateoasResponse.HateoasResponseBuilder links(HateoasLink... links) {
        return link(FieldPath.EMPTY_PATH, new FixedLinkProducer(Arrays.asList(links)));
    }

    @Override
    public HateoasResponse.HateoasResponseBuilder links(HateoasContext context, HateoasLink... links) {
        return link(FieldPath.EMPTY_PATH, new FixedLinkProducer(Arrays.asList(links)));
    }

    @Override
    public HateoasResponseBuilder each(String id, String rel, String... entityFields) {
    	return each(null, id, rel, entityFields);
    }

    @Override
    public HateoasResponseBuilder each(HateoasContext context, String id, String rel, String... entityFields) {
    	return each(new ReflectionBasedLinkProducer(context, id, rel, entityFields));
    }

    @Override
    public HateoasResponseBuilder selfEach(String id, String... entityFields) {
        return each(id, AtomRels.SELF, entityFields);
    }

    @Override
    public HateoasResponseBuilder selfEach(HateoasContext context, String id, String... entityFields) {
        return each(context, id, AtomRels.SELF, entityFields);
    }

    @Override
    public HateoasResponseBuilder each(LinkProducer<?> linkProducer) {
    	return each(null, linkProducer);
    }
    
    @Override
    public HateoasResponseBuilder each(HateoasContext context, LinkProducer<?> linkProducer) {
    	CollectionWrapperStrategy collectionWrapperStrategy = HateoasResponseBuilder.getCollectionWrapperStrategy();
        return link(FieldPath.path(collectionWrapperStrategy.rowsFieldName()), linkProducer);
    }

    public HateoasResponseBuilderImpl() {
    	super();
    }

    private HateoasResponseBuilderImpl(HateoasResponseBuilderImpl that) {
    	super();
        this.statusType = that.statusType;
        this.entity = that.entity;
        if (that.headers != null) {
            this.headers = new OutBoundHeaders(that.headers);
        } else {
            this.headers = null;
        }
        this.entityType = that.entityType;
    }

    public HateoasResponse.HateoasResponseBuilder entityWithType(Object entity,
                                                                 Type entityType) {
        this.entity = entity;
        this.entityType = entityType;
        return this;
    }

    private OutBoundHeaders getHeaders() {
        if (headers == null)
            headers = new OutBoundHeaders();
        return headers;
    }

    // Response.Builder

    @SuppressWarnings("unchecked")
    public HateoasResponse build() {
        HateoasLinkInjector<Object> linkInjector = HateoasResponseBuilder.getLinkInjector();

        CollectionWrapperStrategy collectionWrapperStrategy = HateoasResponseBuilder.getCollectionWrapperStrategy();
        HateoasVerbosity verbosity = HateoasVerbosity.valueOf(RequestContext.getRequestContext().getVerbosityHeader());

        Object newEntity = entity;
        if (entity != null) {
            if (Collection.class.isAssignableFrom(entity.getClass())) {
                newEntity = collectionWrapperStrategy.wrapRootCollection((Collection<Object>) entity);
            }

            Set<Entry<FieldPath,ChainedLinkProducer>> entries = linkMappings.entrySet();
            for (Entry<FieldPath, ChainedLinkProducer> entry : entries) {
                newEntity = entry.getKey().injectLinks(newEntity, linkInjector, entry.getValue(), verbosity);
            }
        }

        final HateoasResponse r = new HateoasResponseImpl(statusType,
                                                          getHeaders(), newEntity, entityType);
        reset();
        return r;
    }

    public HateoasResponse render(String template){
        HateoasLinkInjector<Object> linkInjector = HateoasResponseBuilder.getLinkInjector();

        CollectionWrapperStrategy collectionWrapperStrategy = HateoasResponseBuilder.getCollectionWrapperStrategy();
        HateoasVerbosity verbosity = HateoasVerbosity.valueOf(RequestContext.getRequestContext().getVerbosityHeader());

        Object newEntity = entity;
        if (entity != null) {
            if (Collection.class.isAssignableFrom(entity.getClass())) {
                newEntity = collectionWrapperStrategy.wrapRootCollection((Collection<Object>) entity);
            }

            Set<Entry<FieldPath,ChainedLinkProducer>> entries = linkMappings.entrySet();
            for (Entry<FieldPath, ChainedLinkProducer> entry : entries) {
                Object oldEntity = newEntity;
                newEntity = entry.getKey().injectLinks(newEntity, linkInjector, entry.getValue(), verbosity);
                Object postEntity = newEntity;
            }
        }

        final HateoasResponse r = new HateoasResponseImpl(statusType, getHeaders(), HateoasResponseBuilder.getViewFactory().createView(template, newEntity), entityType);
        reset();
        return r;
    }

    private void reset() {
        statusType = Response.Status.NO_CONTENT;
        headers = null;
        entity = null;
        entityType = null;
        linkMappings.clear();
    }

    @Override
    public HateoasResponse.HateoasResponseBuilder clone() {
        return new HateoasResponseBuilderImpl(this);
    }

    public HateoasResponse.HateoasResponseBuilder status(
            Response.StatusType status) {
        if (status == null)
            throw new IllegalArgumentException();
        this.statusType = status;
        return this;
    }

    ;

    public HateoasResponse.HateoasResponseBuilder status(int status) {
        return status(ResponseImpl.toStatusType(status));
    }

    public HateoasResponse.HateoasResponseBuilder entity(Object entity) {
        this.entity = entity;
        this.entityType = (entity != null) ? entity.getClass() : null;
        return this;
    }

    public HateoasResponse.HateoasResponseBuilder type(MediaType type) {
        headerSingle(HttpHeaders.CONTENT_TYPE, type);
        return this;
    }

    public HateoasResponse.HateoasResponseBuilder type(String type) {
        return type(type == null ? null : MediaType.valueOf(type));
    }

    public HateoasResponse.HateoasResponseBuilder variant(Variant variant) {
        if (variant == null) {
            type((MediaType) null);
            language((String) null);
            encoding(null);
            return this;
        }

        type(variant.getMediaType());
        // TODO set charset
        language(variant.getLanguage());
        encoding(variant.getEncoding());

        return this;
    }

    public HateoasResponse.HateoasResponseBuilder variants(
            List<Variant> variants) {
        if (variants == null) {
            header(HttpHeaders.VARY, null);
            return this;
        }

        if (variants.isEmpty())
            return this;

        MediaType accept = variants.get(0).getMediaType();
        boolean vAccept = false;

        Locale acceptLanguage = variants.get(0).getLanguage();
        boolean vAcceptLanguage = false;

        String acceptEncoding = variants.get(0).getEncoding();
        boolean vAcceptEncoding = false;

        for (Variant v : variants) {
            vAccept |= !vAccept && vary(v.getMediaType(), accept);
            vAcceptLanguage |= !vAcceptLanguage
                    && vary(v.getLanguage(), acceptLanguage);
            vAcceptEncoding |= !vAcceptEncoding
                    && vary(v.getEncoding(), acceptEncoding);
        }

        StringBuilder vary = new StringBuilder();
        append(vary, vAccept, HttpHeaders.ACCEPT);
        append(vary, vAcceptLanguage, HttpHeaders.ACCEPT_LANGUAGE);
        append(vary, vAcceptEncoding, HttpHeaders.ACCEPT_ENCODING);

        if (vary.length() > 0)
            header(HttpHeaders.VARY, vary.toString());
        return this;
    }

    private boolean vary(MediaType v, MediaType vary) {
        return v != null && !v.equals(vary);
    }

    private boolean vary(Locale v, Locale vary) {
        return v != null && !v.equals(vary);
    }

    private boolean vary(String v, String vary) {
        return v != null && !v.equalsIgnoreCase(vary);
    }

    private void append(StringBuilder sb, boolean v, String s) {
        if (v) {
            if (sb.length() > 0)
                sb.append(',');
            sb.append(s);
        }
    }

    public HateoasResponse.HateoasResponseBuilder language(String language) {
        headerSingle(HttpHeaders.CONTENT_LANGUAGE, language);
        return this;
    }

    public HateoasResponse.HateoasResponseBuilder language(Locale language) {
        headerSingle(HttpHeaders.CONTENT_LANGUAGE, language);
        return this;
    }

    public HateoasResponse.HateoasResponseBuilder location(URI location) {
        headerSingle(HttpHeaders.LOCATION, location);
        return this;
    }

    public HateoasResponse.HateoasResponseBuilder location(HateoasLink location) {
        headerSingle(HttpHeaders.LOCATION, location.getHref());
        return this;
    }

    public HateoasResponse.HateoasResponseBuilder contentLocation(URI location) {
        headerSingle(HttpHeaders.CONTENT_LOCATION, location);
        return this;
    }

    public Response.ResponseBuilder encoding(String encoding) {
        headerSingle(HttpHeaders.CONTENT_ENCODING, encoding);
        return this;
    }

    public HateoasResponse.HateoasResponseBuilder tag(EntityTag tag) {
        headerSingle(HttpHeaders.ETAG, tag);
        return this;
    }

    public HateoasResponse.HateoasResponseBuilder tag(String tag) {
        return tag(tag == null ? null : new EntityTag(tag));
    }

    public HateoasResponse.HateoasResponseBuilder lastModified(Date lastModified) {
        headerSingle(HttpHeaders.LAST_MODIFIED, lastModified);
        return this;
    }

    public HateoasResponse.HateoasResponseBuilder cacheControl(
            CacheControl cacheControl) {
        headerSingle(HttpHeaders.CACHE_CONTROL, cacheControl);
        return this;
    }

    public HateoasResponse.HateoasResponseBuilder expires(Date expires) {
        headerSingle(HttpHeaders.EXPIRES, expires);
        return this;
    }

    public HateoasResponse.HateoasResponseBuilder cookie(NewCookie... cookies) {
        if (cookies != null) {
            for (NewCookie cookie : cookies)
                header(HttpHeaders.SET_COOKIE, cookie);
        } else {
            header(HttpHeaders.SET_COOKIE, null);
        }
        return this;
    }

    public HateoasResponse.HateoasResponseBuilder header(String name,
                                                         Object value) {
        return header(name, value, false);
    }

    public HateoasResponse.HateoasResponseBuilder headerSingle(String name,
                                                               Object value) {
        return header(name, value, true);
    }

    public HateoasResponse.HateoasResponseBuilder header(String name,
                                                         Object value, boolean single) {
        if (value != null) {
            if (single) {
                getHeaders().putSingle(name, value);
            } else {
                getHeaders().add(name, value);
            }
        } else {
            getHeaders().remove(name);
        }
        return this;
    }

    private final class ReflectionBasedLinkProducer implements LinkProducer<Object> {
        private final HateoasContext hateoasContext;
        private final String id;
        private final String rel;
        private final String[] entityFields;

        private ReflectionBasedLinkProducer(HateoasContext hateoasContext, String id, String rel, String... entityFields) {
            this.hateoasContext = hateoasContext;
        	this.id = id;
            this.rel = rel;
            this.entityFields = entityFields;
        }

        @Override
        public Collection<HateoasLink> getLinks(Object entity) {
            LinkedList<Object> argumentList = new LinkedList<Object>();

            for (String fieldName : entityFields) {
                Object fieldValue = ReflectionUtils.getFieldValueHierarchical(entity, fieldName);
                argumentList.add(fieldValue);
            }

            return Collections.singletonList(HateoasResponseBuilder.makeLink(hateoasContext, id, rel, argumentList.toArray()));
        }
    }

    private final static class ChainedLinkProducer implements LinkProducer<Object> {
        private final Collection<LinkProducer<Object>> wrappedCallbacks = new LinkedList<LinkProducer<Object>>();

        @SuppressWarnings("unchecked")
        public void append(LinkProducer<?> callback) {
            wrappedCallbacks.add((LinkProducer<Object>) callback);
        }

        @Override
        public Collection<HateoasLink> getLinks(Object entity) {
            Collection<HateoasLink> result = new LinkedList<HateoasLink>();
            for (LinkProducer<Object> callback : wrappedCallbacks) {
                result.addAll(callback.getLinks(entity));
            }

            return result;
        }
    }

    public final static class FixedLinkProducer implements LinkProducer<Object> {
        private Collection<HateoasLink> links;

        public FixedLinkProducer(HateoasLink link) {
            this(Collections.singleton(link));
        }

        public FixedLinkProducer(Collection<HateoasLink> links) {
            this.links = links;
        }

        @Override
        public Collection<HateoasLink> getLinks(Object entity) {
            return links;
        }
    }

}
