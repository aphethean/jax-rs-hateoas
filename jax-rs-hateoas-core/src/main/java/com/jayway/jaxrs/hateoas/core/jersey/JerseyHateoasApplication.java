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
package com.jayway.jaxrs.hateoas.core.jersey;

import com.jayway.jaxrs.hateoas.CollectionWrapperStrategy;
import com.jayway.jaxrs.hateoas.HateoasContextProvider;
import com.jayway.jaxrs.hateoas.HateoasLinkInjector;
import com.jayway.jaxrs.hateoas.HateoasVerbosity;
import com.jayway.jaxrs.hateoas.core.HateoasResponse.HateoasResponseBuilder;
import com.jayway.jaxrs.hateoas.support.DefaultCollectionWrapperStrategy;
import com.jayway.jaxrs.hateoas.support.JavassistHateoasLinkInjector;
import com.sun.jersey.api.core.PackagesResourceConfig;

import java.util.Set;

/**
 * JAX-RS Application adding HATEOAS capability to a Jersey application. Subclass this to add hypermedia capability
 * to your Jersey application.
 *
 * @author Mattias Hellborg Arthursson
 * @author Kalle Stenflo
 */
public abstract class JerseyHateoasApplication extends PackagesResourceConfig {

	public JerseyHateoasApplication(String... packages) {
		this(HateoasVerbosity.MAXIMUM, packages);
	}

	public JerseyHateoasApplication(HateoasVerbosity verbosity,
			String... packages) {
		this(new JavassistHateoasLinkInjector(), new DefaultCollectionWrapperStrategy(), verbosity, packages);
	}

	public JerseyHateoasApplication(HateoasLinkInjector<Object> linkInjector,
                                    CollectionWrapperStrategy collectionWrapperStrategy, HateoasVerbosity verbosity,
                                    String... packages) {
		super(packages);

		Set<Class<?>> allClasses = getClasses();
		for (Class<?> clazz : allClasses) {
			HateoasContextProvider.getDefaultContext().mapClass(clazz);
		}

        HateoasResponseBuilder.configure(linkInjector, collectionWrapperStrategy);
        HateoasVerbosity.setDefaultVerbosity(verbosity);

        JerseyHateoasContextFilter filter = new JerseyHateoasContextFilter();

        super.getContainerRequestFilters().add(filter);
        super.getContainerResponseFilters().add(filter);
	}
}
