package com.jayway.jaxrs.hateoas.core.jersey;

import java.util.Set;

import com.jayway.jaxrs.hateoas.HateoasContextProvider;
import com.jayway.jaxrs.hateoas.HateoasLinkInjector;
import com.jayway.jaxrs.hateoas.HateoasVerbosity;
import com.jayway.jaxrs.hateoas.core.HateoasResponse.HateoasResponseBuilder;
import com.jayway.jaxrs.hateoas.support.JavassistHateoasLinkInjector;
import com.sun.jersey.api.core.PackagesResourceConfig;

public abstract class JerseyHateoasApplication extends PackagesResourceConfig {

	public JerseyHateoasApplication(String... packages) {
		this(HateoasVerbosity.MAXIMUM, packages);
	}

	public JerseyHateoasApplication(HateoasVerbosity verbosity,
			String... packages) {
		this(new JavassistHateoasLinkInjector(), verbosity, packages);
	}

	public JerseyHateoasApplication(HateoasLinkInjector linkInjector,
			HateoasVerbosity verbosity, String... packages) {
		super(packages);

		Set<Class<?>> allClasses = getClasses();
		for (Class<?> clazz : allClasses) {
			HateoasContextProvider.getDefaultContext().mapClass(clazz);
		}

		HateoasResponseBuilder.configure(linkInjector, verbosity);
	}
}
