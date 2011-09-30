package com.jayway.jaxrs.hateoas.support;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.jayway.jaxrs.hateoas.HateoasInjectException;
import com.jayway.jaxrs.hateoas.HateoasLink;
import com.jayway.jaxrs.hateoas.HateoasLinkInjector;
import com.jayway.jaxrs.hateoas.HateoasVerbosity;

public class ReflectionBasedHateoasLinkInjector implements HateoasLinkInjector {

	private static final String DEFAULT_LINKS_FIELD_NAME = "links";
	private final String linksFieldName;

	public ReflectionBasedHateoasLinkInjector() {
		this(DEFAULT_LINKS_FIELD_NAME);
	}

	public ReflectionBasedHateoasLinkInjector(String linksFieldName) {
		this.linksFieldName = linksFieldName;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public Object injectLinks(Object entity, Collection<HateoasLink> links,
			final HateoasVerbosity verbosity) {
		try {
			if (Collection.class.isAssignableFrom(entity.getClass())) {
				Collection<Object> entityAsCollection = (Collection<Object>) entity;
				entity = new HateoasCollectionWrapper(entityAsCollection);
			}

			Field field = ReflectionUtils.getField(entity, linksFieldName);

			if (Collection.class.isAssignableFrom(field.getType())) {
				Collection<HateoasLink> originalLinks = (Collection<HateoasLink>) field
						.get(entity);
				if (originalLinks == null) {
					originalLinks = new LinkedList<HateoasLink>();
				}

				originalLinks.addAll(links);

				field.set(entity, Collections2.transform(originalLinks,
						new Function<HateoasLink, Map<String, Object>>() {
							@Override
							public Map<String, Object> apply(HateoasLink from) {
								return from.toMap(verbosity);
							}
						}));
			} else {
				throw new IllegalArgumentException("Field '" + linksFieldName
						+ "' is not a Collection object");
			}

			return entity;
		} catch (Exception e) {
			throw new HateoasInjectException(e);
		}
	}
}
