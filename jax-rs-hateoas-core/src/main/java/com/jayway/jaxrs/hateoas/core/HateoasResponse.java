package com.jayway.jaxrs.hateoas.core;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Variant;

import org.apache.commons.lang.StringUtils;

import com.jayway.jaxrs.hateoas.EachCallback;
import com.jayway.jaxrs.hateoas.HateoasContext;
import com.jayway.jaxrs.hateoas.HateoasContextProvider;
import com.jayway.jaxrs.hateoas.HateoasLink;
import com.jayway.jaxrs.hateoas.HateoasLinkInjector;
import com.jayway.jaxrs.hateoas.HateoasOption;
import com.jayway.jaxrs.hateoas.HateoasVerbosity;
import com.jayway.jaxrs.hateoas.LinkableInfo;

/**
 * Created by IntelliJ IDEA. User: kallestenflo Date: 8/2/11 Time: 8:40 AM
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
		return created(HateoasResponseBuilder.makeLink(linkId, parameters));
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
	 * A class used to build Response instances that contain metadata instead of
	 * or in addition to an entity. An initial instance may be obtained via
	 * static methods of the Response class, instance methods provide the
	 * ability to set metadata. E.g. to create a response that indicates the
	 * creation of a new resource:
	 * 
	 * <pre>
	 * &#64;POST
	 * Response addWidget(...) {
	 *   Widget w = ...
	 *   URI widgetId = UriBuilder.fromResource(Widget.class)...
	 *   return Response.created(widgetId).build();
	 * }
	 * </pre>
	 * <p/>
	 * <p>
	 * Several methods have parameters of type URI, {@link UriBuilder} provides
	 * convenient methods to create such values as does
	 * <code>URI.create()</code>.
	 * </p>
	 * <p/>
	 * <p>
	 * Where multiple variants of the same method are provided, the type of the
	 * supplied parameter is retained in the metadata of the built
	 * {@code Response}.
	 * </p>
	 */
	public static abstract class HateoasResponseBuilder extends ResponseBuilder {

		private static HateoasLinkInjector linkInjector;
		private static HateoasVerbosity verbosity;

		public abstract HateoasResponseBuilder link(String id, Object... params);

		public abstract HateoasResponseBuilder links(HateoasLink... link);

		public abstract HateoasResponseBuilder selfLink(String id, Object... params);
		
		public static HateoasLink makeLink(String id, Object... params) {
			HateoasContext hateoasContext = HateoasContextProvider
					.getDefaultContext();
			LinkableInfo linkableInfo = hateoasContext.getLinkableInfo(id);
			return DefaultHateoasLink.fromLinkableInfo(linkableInfo, params);
		}

		public static HateoasLink makeSelfLink(String id, Object... params) {
			HateoasContext hateoasContext = HateoasContextProvider
					.getDefaultContext();
			LinkableInfo linkableInfo = hateoasContext.getLinkableInfo(id);
			return DefaultHateoasLink.fromLinkableInfo(linkableInfo, "self",
					params);
		}

		/**
		 * Protected constructor, use one of the static methods of
		 * <code>Response</code> to obtain an instance.
		 */
		protected HateoasResponseBuilder() {
		}

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

		public static void configure(HateoasLinkInjector linkInjector,
				HateoasVerbosity verbosity) {
			HateoasResponseBuilder.linkInjector = linkInjector;
			HateoasResponseBuilder.verbosity = verbosity;
		}

		public static HateoasLinkInjector getLinkInjector() {
			return HateoasResponseBuilder.linkInjector;
		}

		public static HateoasVerbosity getVerbosity(String header) {
			if (StringUtils.isNotBlank(header)) {
				String[] headerSplit = StringUtils.split(header, ",");
				List<HateoasOption> options = new LinkedList<HateoasOption>();
				for (String oneOption : headerSplit) {
					options.add(HateoasOption.valueOf(oneOption.trim()));
				}

				return new HateoasVerbosity(
						options.toArray(new HateoasOption[0]));
			} else {
				return HateoasResponseBuilder.verbosity;
			}

		}

		public abstract HateoasResponseBuilder each(String id,
				String... entityField);

		public abstract HateoasResponseBuilder each(EachCallback callback);
	}

}
