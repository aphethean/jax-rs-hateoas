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
import com.jayway.jaxrs.hateoas.support.FieldPath;

import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Extension of the standard JAX-RS {@link Response}, providing access to a {@link HateoasResponseBuilder} rather
 * than the standard {@link ResponseBuilder}.
 *
 * @author Mattias Hellborg Arthursson
 * @author Kalle Stenflo
 */
public abstract class HateoasResponse extends Response {
	/**
	 * Protected constructor, use one of the static methods to obtain a
	 * {@link ResponseBuilder} instance and obtain a Response from that.
	 */
	protected HateoasResponse() {
	}

	/**
	 * Return the response entity. The response will be serialized using a
	 * MessageBodyWriter for either the class of the entity or, in the case of
	 * {@link javax.ws.rs.core.GenericEntity}, the value of
	 * {@link javax.ws.rs.core.GenericEntity#getRawType()}.
	 *
	 * @return an object instance or null if there is no entity
	 * @see javax.ws.rs.ext.MessageBodyWriter
	 */
	public abstract Object getEntity();

	/**
	 * Get the status code associated with the response.
	 *
	 * @return the response status code or -1 if the status was not set.
	 */
	public abstract int getStatus();

	/**
	 * Get metadata associated with the response as a map. The returned map may
	 * be subsequently modified by the JAX-RS runtime. Values will be serialized
	 * using a {@link javax.ws.rs.ext.RuntimeDelegate.HeaderDelegate} if one is
	 * available via
	 * {@link javax.ws.rs.ext.RuntimeDelegate#createHeaderDelegate(java.lang.Class)}
	 * for the class of the value or using the values {@code toString} method if
	 * a header delegate is not available.
	 *
	 * @return response metadata as a map
	 */
	public abstract MultivaluedMap<String, Object> getMetadata();

	/**
	 * Create a new ResponseBuilder by performing a shallow copy of an existing
	 * Response. The returned builder has its own metadata map but entries are
	 * simply references to the keys and values contained in the supplied
	 * Response metadata map.
	 *
	 * @param response
	 *            a Response from which the status code, entity and metadata
	 *            will be copied
	 * @return a new ReponseBuilder
	 */
	public static HateoasResponseBuilder fromResponse(Response response) {
		HateoasResponseBuilder b = status(response.getStatus());
		b.entity(response.getEntity());
		for (String headerName : response.getMetadata().keySet()) {
			List<Object> headerValues = response.getMetadata().get(headerName);
			for (Object headerValue : headerValues) {
				b.header(headerName, headerValue);
			}
		}
		return b;
	}

	/**
	 * Create a new ResponseBuilder with the supplied status.
	 *
	 * @param status
	 *            the response status
	 * @return a new ResponseBuilder
	 * @throws IllegalArgumentException
	 *             if status is null
	 */
	public static HateoasResponseBuilder status(StatusType status) {
		HateoasResponseBuilder b = HateoasResponseBuilder.newInstance();
		b.status(status);
		return b;
	}
	
	/**
	 * Create a new ResponseBuilder with the supplied status.
	 *
	 * @param status
	 *            the response status
	 * @return a new ResponseBuilder
	 * @throws IllegalArgumentException
	 *             if status is null
	 */
	public static HateoasResponseBuilder status(Status status) {
		return status((StatusType) status);
	}

	/**
	 * Create a new ResponseBuilder with the supplied status.
	 *
	 * @param status
	 *            the response status
	 * @return a new ResponseBuilder
	 * @throws IllegalArgumentException
	 *             if status is less than 100 or greater than 599.
	 */
	public static HateoasResponseBuilder status(int status) {
		HateoasResponseBuilder b = HateoasResponseBuilder.newInstance();
		b.status(status);
		return b;
	}

	/**
	 * Create a new ResponseBuilder with an OK status.
	 *
	 * @return a new ResponseBuilder
	 */
	public static HateoasResponseBuilder ok() {
		HateoasResponseBuilder b = status(Status.OK);
		return b;
	}

	/**
	 * Create a new ResponseBuilder that contains a representation. It is the
	 * callers responsibility to wrap the actual entity with
	 * {@link javax.ws.rs.core.GenericEntity} if preservation of its generic
	 * type is required.
	 *
	 * @param entity
	 *            the representation entity data
	 * @return a new ResponseBuilder
	 */
	public static HateoasResponseBuilder ok(Object entity) {
		HateoasResponseBuilder b = ok();
		b.entity(entity);
		return b;
	}

	/**
	 * Create a new ResponseBuilder that contains a representation. It is the
	 * callers responsibility to wrap the actual entity with
	 * {@link javax.ws.rs.core.GenericEntity} if preservation of its generic
	 * type is required.
	 *
	 * @param entity
	 *            the representation entity data
	 * @param type
	 *            the media type of the entity
	 * @return a new ResponseBuilder
	 */
	public static HateoasResponseBuilder ok(Object entity, MediaType type) {
		HateoasResponseBuilder b = ok();
		b.entity(entity);
		b.type(type);
		return b;
	}

	/**
	 * Create a new ResponseBuilder that contains a representation. It is the
	 * callers responsibility to wrap the actual entity with
	 * {@link javax.ws.rs.core.GenericEntity} if preservation of its generic
	 * type is required.
	 *
	 * @param entity
	 *            the representation entity data
	 * @param type
	 *            the media type of the entity
	 * @return a new ResponseBuilder
	 */
	public static HateoasResponseBuilder ok(Object entity, String type) {
		HateoasResponseBuilder b = ok();
		b.entity(entity);
		b.type(type);
		return b;
	}

	/**
	 * Create a new ResponseBuilder that contains a representation. It is the
	 * callers responsibility to wrap the actual entity with
	 * {@link javax.ws.rs.core.GenericEntity} if preservation of its generic
	 * type is required.
	 *
	 * @param entity
	 *            the representation entity data
	 * @param variant
	 *            representation metadata
	 * @return a new ResponseBuilder
	 */
	public static HateoasResponseBuilder ok(Object entity, Variant variant) {
		HateoasResponseBuilder b = ok();
		b.entity(entity);
		b.variant(variant);
		return b;
	}

	/**
	 * Create a new ResponseBuilder with an server error status.
	 *
	 * @return a new ResponseBuilder
	 */
	public static HateoasResponseBuilder serverError() {
		HateoasResponseBuilder b = status(Status.INTERNAL_SERVER_ERROR);
		return b;
	}

	/**
	 * Create a new ResponseBuilder for a created resource, set the location
	 * header using the supplied value.
	 *
	 * @param location
	 *            the URI of the new resource. If a relative URI is supplied it
	 *            will be converted into an absolute URI by resolving it
	 *            relative to the request URI (see
	 *            {@link javax.ws.rs.core.UriInfo#getRequestUri}).
	 * @return a new ResponseBuilder
	 * @throws java.lang.IllegalArgumentException
	 *             if location is null
	 */
	public static HateoasResponseBuilder created(URI location) {
		HateoasResponseBuilder b = status(Status.CREATED).location(location);
		return b;
	}

	public static HateoasResponseBuilder created(HateoasLink link) {
		try {
			return created(new URI(link.getHref()));
		} catch (URISyntaxException e) {
			throw new RuntimeException("Invalid URI in href", e);
		}
	}

	public static HateoasResponseBuilder created(String linkId,
			Object... parameters) {
		return created(null, linkId, parameters);
	}
	
	public static HateoasResponseBuilder created(HateoasContext context, String linkId,
			Object... parameters) {
		
		return created(HateoasResponseBuilder.makeLink(context, linkId, null, parameters));
	}

	/**
	 * Create a new ResponseBuilder for an empty response.
	 *
	 * @return a new ResponseBuilder
	 */
	public static HateoasResponseBuilder noContent() {
		HateoasResponseBuilder b = status(Status.NO_CONTENT);
		return b;
	}

	/**
	 * Create a new ResponseBuilder with a not-modified status.
	 *
	 * @return a new ResponseBuilder
	 */
	public static HateoasResponseBuilder notModified() {
		HateoasResponseBuilder b = status(Status.NOT_MODIFIED);
		return b;
	}

	/**
	 * Create a new ResponseBuilder with a not-modified status.
	 *
	 * @param tag
	 *            a tag for the unmodified entity
	 * @return a new ResponseBuilder
	 * @throws java.lang.IllegalArgumentException
	 *             if tag is null
	 */
	public static HateoasResponseBuilder notModified(EntityTag tag) {
		HateoasResponseBuilder b = notModified();
		b.tag(tag);
		return b;
	}

	/**
	 * Create a new ResponseBuilder with a not-modified status and a strong
	 * entity tag. This is a shortcut for
	 * <code>notModified(new EntityTag(<i>value</i>))</code>.
	 *
	 * @param tag
	 *            the string content of a strong entity tag. The JAX-RS runtime
	 *            will quote the supplied value when creating the header.
	 * @return a new ResponseBuilder
	 * @throws java.lang.IllegalArgumentException
	 *             if tag is null
	 */
	public static HateoasResponseBuilder notModified(String tag) {
		HateoasResponseBuilder b = notModified();
		b.tag(tag);
		return b;
	}

	/**
	 * Create a new ResponseBuilder for a redirection. Used in the
	 * redirect-after-POST (aka POST/redirect/GET) pattern.
	 *
	 * @param location
	 *            the redirection URI. If a relative URI is supplied it will be
	 *            converted into an absolute URI by resolving it relative to the
	 *            base URI of the application (see {@link UriInfo#getBaseUri}).
	 * @return a new ResponseBuilder
	 * @throws java.lang.IllegalArgumentException
	 *             if location is null
	 */
	public static HateoasResponseBuilder seeOther(URI location) {
		HateoasResponseBuilder b = status(Status.SEE_OTHER).location(location);
		return b;
	}

	/**
	 * Create a new ResponseBuilder for a temporary redirection.
	 *
	 * @param location
	 *            the redirection URI. If a relative URI is supplied it will be
	 *            converted into an absolute URI by resolving it relative to the
	 *            base URI of the application (see {@link UriInfo#getBaseUri}).
	 * @return a new ResponseBuilder
	 * @throws java.lang.IllegalArgumentException
	 *             if location is null
	 */
	public static HateoasResponseBuilder temporaryRedirect(URI location) {
		HateoasResponseBuilder b = status(Status.TEMPORARY_REDIRECT).location(
				location);
		return b;
	}

	/**
	 * Create a new ResponseBuilder for a not acceptable response.
	 *
	 * @param variants
	 *            list of variants that were available, a null value is
	 *            equivalent to an empty list.
	 * @return a new ResponseBuilder
	 */
	public static HateoasResponseBuilder notAcceptable(List<Variant> variants) {
		HateoasResponseBuilder b = status(Status.NOT_ACCEPTABLE).variants(
				variants);
		return b;
	}

    /**
     * Extension of the standard JAX-RS {@link ResponseBuilder}, adding methods to specify hypermedia in responses.
     * @see #link(String, String, Object...)
     * @see #links(com.jayway.jaxrs.hateoas.HateoasLink...)
     * @see #selfLink(String, Object...)
     * @see #each(String, String, String...)
     * @see #each(com.jayway.jaxrs.hateoas.LinkProducer)
     * @see #makeLink(String, String, Object...)
     */
	public static abstract class HateoasResponseBuilder extends ResponseBuilder {

		private static HateoasLinkInjector<Object> linkInjector;
        private static CollectionWrapperStrategy collectionWrapperStrategy;
        private static HateoasViewFactory viewFactory;
        
        //public abstract HateoasLinkBuilder linkBuilder(String id);

        /**
         * Append a link to the entity root, corresponding to the supplied id, building the URI using the specified
         * parameters.
         *
         * @param id the @Linkable id of the target method.
         * @param rel the relation of the linked resource in the current context.
         * @param params the parameters to use for populating path parameters.  @return this.
         * @return this
         */
        public abstract HateoasResponseBuilder link(String id, String rel, Object... params);

        /**
         * This method is an alternative mechanism if you have not used @Linkable to register your links.
         * @see {@link HateoasResponseBuilder#link(String, String, Object...)}
         * @param context The HateoasContext to lookup {@link LinkableInfo}
         * @param id
         * @param rel
         * @param params
         * @return
         */
        public abstract HateoasResponseBuilder link(HateoasContext context, String id, String rel, Object... params);
        
        /**
         * Append a link to the object at the specified FieldPath, corresponding to the supplied id,
         * building the URI using the specified parameters.
         *
         * If the object at the target FieldPath is a {@link java.util.Collection},
         * the link will be applied to <b>each</b> entry in the collection.
         *
         * If any object encountered in the middle of the FieldPath is a Collection,
         * the rest of the path will be traversed for each element in that collection,
         * making sure that the links will be applied at each end node.
         *
         * @param fieldPath The FieldPath of the targeted object in the object graph represented by the entity root.
         * @param id the @Linkable id of the target method.
         * @param rel the relation of the linked resource in the current context.
         * @param entityFields the fields in the nested elements that should be retrieved using reflection and used for
         * populating the path parameters.
         * @return this
         */
        public abstract HateoasResponseBuilder link(FieldPath fieldPath, String id, String rel,
                                                    String... entityFields);

        /**
         * This method is an alternative mechanism if you have not used @Linkable to register your links.
         * @see {@link HateoasResponseBuilder#link(FieldPath, String, String, String...)}
         * @param context The HateoasContext to lookup {@link LinkableInfo}
         * @param fieldPath
         * @param id
         * @param rel
         * @param entityFields
         * @return
         */
        public abstract HateoasResponseBuilder link(HateoasContext context, FieldPath fieldPath, String id, String rel,
                String... entityFields);

        /**
         * Append a LinkProducer to be applied for generating links for the object at the specified FieldPath.
         *
         * If the object at the target FieldPath is a {@link java.util.Collection},
         * the LinkProducer will be applied to <b>each</b> entry in the collection,
         * and the resulting links will be appended to the links collection of each individual entry.
         *
         * If any object encountered in the middle of the FieldPath is a Collection,
         * the rest of the path will be traversed for each element in that collection,
         * making sure that the links will be applied at each end node.
         *
         * @param fieldPath The FieldPath of the targeted object in the object graph represented by the entity root.
         * @param linkProducer The LinkProducer to be applied to the target for producing links for it.
         * @return this.
         */
        public abstract HateoasResponseBuilder link(FieldPath fieldPath, LinkProducer<?> linkProducer);

        /**
         * Append a number of HateoasLinks.
         *
         * @param link the links to append.
         * @return this.
         * @see #link(String, String, Object...)
         * @see #makeLink(String, String, Object...)
         */
		public abstract HateoasResponseBuilder links(HateoasLink... link);

		/**
         * This method is an alternative mechanism if you have not used @Linkable to register your links.
         * @see {@link HateoasResponseBuilder#links(HateoasLink...)}
         * @param context The HateoasContext to lookup {@link LinkableInfo}
		 * @param link
		 * @return
		 */
		public abstract HateoasResponseBuilder links(HateoasContext context, HateoasLink... link);

        /**
         * Append a link corresponding to the supplied id, defaulting the rel to 'self' and building the URI using
         * the specified parameters.
         *
         * @param id the @Linkable id of the target method.
         * @param params the parameters to use for populating path parameters.
         * @return this.
         */
		public abstract HateoasResponseBuilder selfLink(String id, Object... params);

		/**
         * This method is an alternative mechanism if you have not used @Linkable to register your links.
         * @see {@link HateoasResponseBuilder#selfLink(String, Object...)}
         * @param context The HateoasContext to lookup {@link LinkableInfo}
		 * @param id
		 * @param params
		 * @return
		 */
		public abstract HateoasResponseBuilder selfLink(HateoasContext context, String id, Object... params);

        /**
         * Append a link to be included for each element nested in the entity (which is expected to be a Collection).
         * @param id the @Linkable id of the target method.
         * @param rel the relation of the linked resource from an item in the list.
         * @param entityFields the fields in the nested elements that should be retrieved using reflection and used for
         * populating the path parameters.
         * @return this.
         */
        public abstract HateoasResponseBuilder each(String id, String rel, String... entityFields);

        /**
         * This method is an alternative mechanism if you have not used @Linkable to register your links.
         * @see {@link HateoasResponseBuilder#each(String, String, String...)}
         * @param context The HateoasContext to lookup {@link LinkableInfo}
         * @param id
         * @param rel
         * @param entityFields
         * @return
         */
        public abstract HateoasResponseBuilder each(HateoasContext context, String id, String rel, String... entityFields);

        /**
         * Append a link to be included for each element nested in the entity (which is expected to be a Collection),
         * defaulting the rel to 'self'.
         * @param id the @Linkable id of the target method.
         * @param entityFields the fields in the nested elements that should be retrieved using reflection and used for
         * populating the path parameters.
         * @return this.
         */
        public abstract HateoasResponseBuilder selfEach(String id, String... entityFields);

        /**
         * This method is an alternative mechanism if you have not used @Linkable to register your links.
         * @see {@link HateoasResponseBuilder#selfEach(String, String...)}
         * @param context The HateoasContext to lookup {@link LinkableInfo}
         * @param id
         * @param entityFields
         * @return
         */
        public abstract HateoasResponseBuilder selfEach(HateoasContext context, String id, String... entityFields);

		public abstract HateoasResponseBuilder each(LinkProducer<?> linkProducer);
		public abstract HateoasResponseBuilder each(HateoasContext context, LinkProducer<?> linkProducer);

        /**
         * Construct a {@link HateoasLink} for the supplied id, building the URI using the specified parameters.
         *
         *
         * @param id the @Linkable id of the target method.
         * @param rel the relation of the linked resource in the current context.
         * @param params the parameters to use for populating path parameters.  @return a populated HateoasLink instance.
         */
		public static HateoasLink makeLink(String id, String rel, Object... params) {
			return makeLink(null, id, rel, params);
		}
		
		/**
         * This method is an alternative mechanism if you have not used @Linkable to register your links.
         * @see {@link HateoasResponseBuilder#selfEach(String, String...)}
		 * @param hateoasContext
		 * @param id
		 * @param rel
		 * @param params
		 * @return
		 */
		public static HateoasLink makeLink(HateoasContext hateoasContext, String id, String rel, Object... params) {
			if (hateoasContext == null)
				hateoasContext = HateoasContextProvider.getDefaultContext();
			LinkableInfo linkableInfo = hateoasContext.getLinkableInfo(id);
			return DefaultHateoasLink.fromLinkableInfo(linkableInfo, rel, params);
		}

		/**
		 * Protected constructor, use one of the static methods of
		 * <code>Response</code> to obtain an instance.
		 */
		protected HateoasResponseBuilder() {}

		/**
		 * Create a new builder instance.
		 *
		 * @return a new ResponseBuilder
		 */
		protected static HateoasResponseBuilder newInstance() {
			HateoasResponseBuilder b = new HateoasResponseBuilderImpl(); // RuntimeDelegate.getInstance().createResponseBuilder();
			return b;
		}

		/**
		 * Create a Response instance from the current ResponseBuilder. The
		 * builder is reset to a blank state equivalent to calling the ok
		 * method.
		 *
		 * @return a Response instance
		 */
		public abstract HateoasResponse build();

        /**
         * Create a Response by wrapping the entity in a Viewable using the provided template.
         *
         * @return a Response instance
         */
        public abstract HateoasResponse render(String template);

		/**
		 * Create a copy of the ResponseBuilder preserving its state.
		 *
		 * @return a copy of the ResponseBuilder
		 */
		@Override
		public abstract HateoasResponseBuilder clone();

		/**
		 * Set the status on the ResponseBuilder.
		 *
		 * @param status
		 *            the response status
		 * @return the updated ResponseBuilder
		 * @throws IllegalArgumentException
		 *             if status is less than 100 or greater than 599.
		 */
		public abstract HateoasResponseBuilder status(int status);

		/**
		 * Set the status on the ResponseBuilder.
		 *
		 * @param status
		 *            the response status
		 * @return the updated ResponseBuilder
		 * @throws IllegalArgumentException
		 *             if status is null
		 */
		public HateoasResponseBuilder status(StatusType status) {
			if (status == null)
				throw new IllegalArgumentException();
			return status(status.getStatusCode());
		}

		;

		/**
		 * Set the status on the ResponseBuilder.
		 *
		 * @param status
		 *            the response status
		 * @return the updated ResponseBuilder
		 * @throws IllegalArgumentException
		 *             if status is null
		 */
		public HateoasResponseBuilder status(Status status) {
			return status((StatusType) status);
		}

		/**
		 * Set the entity on the ResponseBuilder. It is the callers
		 * responsibility to wrap the actual entity with {@link GenericEntity}
		 * if preservation of its generic type is required.
		 *
		 * @param entity
		 *            the response entity
		 * @return the updated ResponseBuilder
		 */
		public abstract HateoasResponseBuilder entity(Object entity);

		/**
		 * Set the response media type on the ResponseBuilder.
		 *
		 * @param type
		 *            the media type of the response entity, if null any
		 *            existing value for type will be removed
		 * @return the updated ResponseBuilder
		 */
		public abstract HateoasResponseBuilder type(MediaType type);

		/**
		 * Set the response media type on the ResponseBuilder.
		 *
		 * @param type
		 *            the media type of the response entity, if null any
		 *            existing value for type will be removed
		 * @return the updated ResponseBuilder
		 * @throws IllegalArgumentException
		 *             if type cannot be parsed
		 */
		public abstract HateoasResponseBuilder type(String type);

		/**
		 * Set representation metadata on the ResponseBuilder. Equivalent to
		 * setting the values of content type, content language, and content
		 * encoding separately using the values of the variant properties.
		 *
		 * @param variant
		 *            metadata of the response entity, a null value is
		 *            equivalent to a variant with all null properties.
		 * @return the updated ResponseBuilder
		 */
		public abstract HateoasResponseBuilder variant(Variant variant);

		/**
		 * Add a Vary header that lists the available variants.
		 *
		 * @param variants
		 *            a list of available representation variants, a null value
		 *            will remove an existing value for vary.
		 * @return the updated ResponseBuilder
		 */
		public abstract HateoasResponseBuilder variants(List<Variant> variants);

		/**
		 * Set the language on the ResponseBuilder.
		 *
		 * @param language
		 *            the language of the response entity, if null any existing
		 *            value for language will be removed
		 * @return the updated ResponseBuilder
		 */
		public abstract HateoasResponseBuilder language(String language);

		/**
		 * Set the language on the ResponseBuilder.
		 *
		 * @param language
		 *            the language of the response entity, if null any existing
		 *            value for type will be removed
		 * @return the updated ResponseBuilder
		 */
		public abstract HateoasResponseBuilder language(Locale language);

		/**
		 * Set the location on the ResponseBuilder.
		 *
		 * @param location
		 *            the location. If a relative URI is supplied it will be
		 *            converted into an absolute URI by resolving it relative to
		 *            the base URI of the application (see
		 *            {@link UriInfo#getBaseUri}). If null any existing value
		 *            for location will be removed.
		 * @return the updated ResponseBuilder
		 */
		public abstract HateoasResponseBuilder location(URI location);

		public abstract HateoasResponseBuilder location(HateoasLink location);

		/**
		 * Set the content location on the ResponseBuilder.
		 *
		 * @param location
		 *            the content location. Relative or absolute URIs may be
		 *            used for the value of content location. If null any
		 *            existing value for content location will be removed.
		 * @return the updated ResponseBuilder
		 */
		public abstract HateoasResponseBuilder contentLocation(URI location);

		/**
		 * Set an entity tag on the ResponseBuilder.
		 *
		 * @param tag
		 *            the entity tag, if null any existing entity tag value will
		 *            be removed.
		 * @return the updated ResponseBuilder
		 */
		public abstract HateoasResponseBuilder tag(EntityTag tag);

		/**
		 * Set a strong entity tag on the ResponseBuilder. This is a shortcut
		 * for <code>tag(new EntityTag(<i>value</i>))</code>.
		 *
		 * @param tag
		 *            the string content of a strong entity tag. The JAX-RS
		 *            runtime will quote the supplied value when creating the
		 *            header. If null any existing entity tag value will be
		 *            removed.
		 * @return the updated ResponseBuilder
		 */
		public abstract HateoasResponseBuilder tag(String tag);

		/**
		 * Set the last modified date on the ResponseBuilder.
		 *
		 * @param lastModified
		 *            the last modified date, if null any existing last modified
		 *            value will be removed.
		 * @return the updated ResponseBuilder
		 */
		public abstract HateoasResponseBuilder lastModified(Date lastModified);

		/**
		 * Set the cache control data on the ResponseBuilder.
		 *
		 * @param cacheControl
		 *            the cache control directives, if null removes any existing
		 *            cache control directives.
		 * @return the updated ResponseBuilder
		 */
		public abstract HateoasResponseBuilder cacheControl(
				CacheControl cacheControl);

		/**
		 * Set the expires date on the ResponseBuilder.
		 *
		 * @param expires
		 *            the expiration date, if null removes any existing expires
		 *            value.
		 * @return the updated ResponseBuilder
		 */
		public abstract HateoasResponseBuilder expires(Date expires);

		/**
		 * Add a header to the ResponseBuilder.
		 *
		 * @param name
		 *            the name of the header
		 * @param value
		 *            the value of the header, the header will be serialized
		 *            using a
		 *            {@link javax.ws.rs.ext.RuntimeDelegate.HeaderDelegate} if
		 *            one is available via
		 *            {@link javax.ws.rs.ext.RuntimeDelegate#createHeaderDelegate(java.lang.Class)}
		 *            for the class of {@code value} or using its
		 *            {@code toString} method if a header delegate is not
		 *            available. If {@code value} is null then all current
		 *            headers of the same name will be removed.
		 * @return the updated ResponseBuilder
		 */
		public abstract HateoasResponseBuilder header(String name, Object value);

		/**
		 * Add cookies to the ResponseBuilder.
		 *
		 * @param cookies
		 *            new cookies that will accompany the response. A null value
		 *            will remove all cookies, including those added via the
		 *            {@link #header(java.lang.String, java.lang.Object)}
		 *            method.
		 * @return the updated ResponseBuilder
		 */
		public abstract HateoasResponseBuilder cookie(NewCookie... cookies);

		public static void configure(HateoasLinkInjector<Object> linkInjector,
                                     CollectionWrapperStrategy collectionWrapperStrategy,
                                     HateoasViewFactory hateoasViewFactory) {
			HateoasResponseBuilder.linkInjector = linkInjector;
            HateoasResponseBuilder.collectionWrapperStrategy= collectionWrapperStrategy;
            HateoasResponseBuilder.viewFactory = hateoasViewFactory;
		}

        public static HateoasViewFactory getViewFactory(){
            return viewFactory;
        }
		public static HateoasLinkInjector<Object> getLinkInjector() {
			return linkInjector;
		}

        public static CollectionWrapperStrategy getCollectionWrapperStrategy(){
            return collectionWrapperStrategy;
        }
	}

}
