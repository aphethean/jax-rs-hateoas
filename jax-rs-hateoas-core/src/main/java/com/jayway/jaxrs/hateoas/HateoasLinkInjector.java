package com.jayway.jaxrs.hateoas;

import java.util.Collection;

public interface HateoasLinkInjector {
	Object injectLinks(Object entity, Collection<HateoasLink> links,
			HateoasVerbosity verbosity);
}
