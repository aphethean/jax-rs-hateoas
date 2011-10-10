/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jayway.jaxrs.hateoas.core;

import com.jayway.jaxrs.hateoas.EachCallback;
import com.jayway.jaxrs.hateoas.HateoasLink;
import com.jayway.jaxrs.hateoas.HateoasLinkInjector;
import com.jayway.jaxrs.hateoas.HateoasVerbosity;
import com.jayway.jaxrs.hateoas.core.HateoasResponse.HateoasResponseBuilder;
import com.jayway.jaxrs.hateoas.support.HateoasCollectionWrapper;
import com.jayway.jaxrs.hateoas.support.ReflectionUtils;
import com.jayway.jaxrs.hateoas.web.RequestContext;
import com.sun.jersey.core.header.OutBoundHeaders;
import com.sun.jersey.core.spi.factory.ResponseImpl;

import javax.ws.rs.core.*;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.*;

/**
 * @author Mattias Hellborg Arthursson
 * @author Kalle Stenflo
 */
public class HateoasResponseBuilderImpl extends
		HateoasResponse.HateoasResponseBuilder {

	private Response.StatusType statusType = Response.Status.NO_CONTENT;

	private OutBoundHeaders headers;

	private Object entity;

	private Type entityType;

	private Collection<HateoasLink> links = new HashSet<HateoasLink>();

	private EachCallback eachCallback;

    @Override
	public HateoasResponseBuilder link(String id, Object... params) {
		return links(HateoasResponseBuilder.makeLink(id, params));
	}

	@Override
	public HateoasResponseBuilder selfLink(String id, Object... params) {
		return links(HateoasResponseBuilder.makeSelfLink(id, params));
	}

	@Override
	public HateoasResponse.HateoasResponseBuilder links(HateoasLink... links) {
		this.links.addAll(Arrays.asList(links));

		return this;
	}

	@Override
	public HateoasResponseBuilder each(final String id,
			final String... entityField) {
		return each(new ReflectionBasedEachCallback(id, entityField));
	}

	@Override
	public HateoasResponseBuilder each(EachCallback callback) {
		this.eachCallback = callback;
		return this;
	}

	public HateoasResponseBuilderImpl() {
	}

	private HateoasResponseBuilderImpl(HateoasResponseBuilderImpl that) {
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

	public HateoasResponse build() {
		HateoasLinkInjector linkInjector = HateoasResponseBuilder
				.getLinkInjector();
		HateoasVerbosity verbosity = HateoasResponseBuilder
				.getVerbosity(RequestContext.getRequestContext().getVerbosityHeader());

		Object newEntity = entity;
		if (newEntity != null) {
			newEntity = linkInjector.injectLinks(entity, new HashSet<HateoasLink>(links), verbosity);

			if (newEntity instanceof HateoasCollectionWrapper) {
				if (eachCallback != null) {
					HateoasCollectionWrapper wrapper = (HateoasCollectionWrapper) newEntity;
					wrapper.transformRows(linkInjector, eachCallback, verbosity);
				}
			}
		}

		final HateoasResponse r = new HateoasResponseImpl(statusType,
				getHeaders(), newEntity, entityType);
		reset();
		return r;
	}

	private void reset() {
		statusType = Response.Status.NO_CONTENT;
		headers = null;
		entity = null;
		entityType = null;
        links.clear();
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

	private final class ReflectionBasedEachCallback implements EachCallback {
		private final String id;
		private final String[] entityField;

		private ReflectionBasedEachCallback(String id, String[] entityField) {
			this.id = id;
			this.entityField = entityField;
		}

		@Override
		public Collection<HateoasLink> getLinks(Object entity) {
			LinkedList<Object> argumentList = new LinkedList<Object>();

			for (String fieldName : entityField) {
				Object fieldValue = ReflectionUtils.getFieldValue(entity,
						fieldName);
				argumentList.add(fieldValue);
			}

			return Collections.singletonList(makeSelfLink(id,
					argumentList.toArray()));
		}
	}

}
