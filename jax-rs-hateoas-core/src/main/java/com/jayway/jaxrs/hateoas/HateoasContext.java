package com.jayway.jaxrs.hateoas;

public interface HateoasContext {

	void mapClass(Class<?> clazz);

	LinkableInfo getLinkableInfo(String link);

}