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
package com.jayway.jaxrs.hateoas.core;

import com.jayway.jaxrs.hateoas.CollectionWrapperStrategy;
import com.jayway.jaxrs.hateoas.HateoasContextProvider;
import com.jayway.jaxrs.hateoas.HateoasLinkInjector;
import com.jayway.jaxrs.hateoas.HateoasVerbosity;
import com.jayway.jaxrs.hateoas.core.HateoasResponse.HateoasResponseBuilder;
import com.jayway.jaxrs.hateoas.support.DefaultCollectionWrapperStrategy;
import com.jayway.jaxrs.hateoas.support.DefaultHateoasViewFactory;
import com.jayway.jaxrs.hateoas.support.JavassistHateoasLinkInjector;

import javax.ws.rs.core.Application;
import java.util.Set;

/**
 * Plain JAX-RS application adding HATEOAS capability. Subclass this to add hypermedia capability to a plain JAX-RS
 * application.
 *
 * @author Mattias Hellborg Arthursson
 * @author Kalle Stenflo
 */
public class HateoasApplication extends Application {
	public HateoasApplication() {
		this(HateoasVerbosity.MAXIMUM);
	}

	public HateoasApplication(HateoasVerbosity verbosity) {
		this(new JavassistHateoasLinkInjector(), new DefaultCollectionWrapperStrategy(), verbosity);
	}

	public HateoasApplication(HateoasLinkInjector<Object> linkInjector,
                              CollectionWrapperStrategy collectionWrapperStrategy, HateoasVerbosity verbosity) {

		Set<Class<?>> allClasses = getClasses();
		for (Class<?> clazz : allClasses) {
			HateoasContextProvider.getDefaultContext().mapClass(clazz);
		}

		HateoasResponseBuilder.configure(linkInjector, collectionWrapperStrategy, new DefaultHateoasViewFactory());
        HateoasVerbosity.setDefaultVerbosity(verbosity);
	}
}
