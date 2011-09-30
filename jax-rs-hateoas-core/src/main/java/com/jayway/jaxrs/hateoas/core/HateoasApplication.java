package com.jayway.jaxrs.hateoas.core;

import java.util.Set;

import javax.ws.rs.core.Application;

import com.jayway.jaxrs.hateoas.HateoasContextProvider;
import com.jayway.jaxrs.hateoas.HateoasLinkInjector;
import com.jayway.jaxrs.hateoas.HateoasVerbosity;
import com.jayway.jaxrs.hateoas.core.HateoasResponse.HateoasResponseBuilder;
import com.jayway.jaxrs.hateoas.support.JavassistHateoasLinkInjector;

public class HateoasApplication extends Application {
	public HateoasApplication() {
		this(HateoasVerbosity.MAXIMUM);
	}

	public HateoasApplication(HateoasVerbosity verbosity) {
		this(new JavassistHateoasLinkInjector(), verbosity);
	}

	public HateoasApplication(HateoasLinkInjector linkInjector,
			HateoasVerbosity verbosity) {

		Set<Class<?>> allClasses = getClasses();
		for (Class<?> clazz : allClasses) {
			HateoasContextProvider.getDefaultContext().mapClass(clazz);
		}

		HateoasResponseBuilder.configure(linkInjector, verbosity);

	}
}
