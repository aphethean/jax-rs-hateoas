package com.jayway.demo.library.rest.application.hateoas;

import com.jayway.jaxrs.hateoas.HateoasVerbosity;
import com.jayway.jaxrs.hateoas.core.jersey.JerseyHateoasApplication;

public class LibraryApplication extends JerseyHateoasApplication {
	public LibraryApplication() {
		super(HateoasVerbosity.GENERIC_CLIENT,
				"com.jayway.demo.library.rest.resources.hateoas");
	}
}
