package com.jayway.jaxrs.hateoas;

import java.util.Map;

public interface HateoasLink {

	String getRel();

	String getMethod();

	String getId();

	String getHref();

	String[] getConsumes();

	String[] getProduces();

	String getLabel();

	String getDescription();

	Class<?> getTemplateClass();

	Map<String, Object> toMap(HateoasVerbosity verbosity);

}