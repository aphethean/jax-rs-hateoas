package com.jayway.jaxrs.hateoas;

import java.util.Collection;

public interface EachCallback {
	Collection<HateoasLink> getLinks(Object entity);
}
