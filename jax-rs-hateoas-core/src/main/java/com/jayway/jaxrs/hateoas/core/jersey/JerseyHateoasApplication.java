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

import com.jayway.jaxrs.hateoas.*;
import com.jayway.jaxrs.hateoas.core.HateoasConfigurationFactory;
import com.jayway.jaxrs.hateoas.core.HateoasResponse.HateoasResponseBuilder;
import com.sun.jersey.api.core.PackagesResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

/**
 * JAX-RS Application adding HATEOAS capability to a Jersey application. Subclass this to add hypermedia capability
 * to your Jersey application.
 *
 * @author Mattias Hellborg Arthursson
 * @author Kalle Stenflo
 */
public abstract class JerseyHateoasApplication extends PackagesResourceConfig {

    private static final Logger log = LoggerFactory.getLogger(JerseyHateoasApplication.class);

    /**
     * Creates a new Application with he given {@link HateoasVerbosity} level.
     *
     * @param verbosity the verbosity level to use in the application
     * @param props     properties to be passed to {@link PackagesResourceConfig} constructor
     */
    public JerseyHateoasApplication(HateoasVerbosity verbosity,
                                    Map<String, Object> props) {
        this(HateoasConfigurationFactory.createLinkInjector(props),
                HateoasConfigurationFactory.createCollectionWrapperStrategy(props),
                verbosity,
                HateoasConfigurationFactory.createHateoasViewFactory(props, JerseyHateoasViewFactory.class.getName()),
                props);
    }

    /**
     * Creates a new Application based on Servlet init params. Note the HATEOAS
     * properties can be combined with standard Jersey properties defined in
     * {@link com.sun.jersey.api.core.ResourceConfig}
     *
     * @param props properties to be passed to {@link PackagesResourceConfig} constructor
     * @see com.jayway.jaxrs.hateoas.core.HateoasConfigurationFactory#PROPERTY_HATEOAS_VERBOSITY
     * @see com.jayway.jaxrs.hateoas.core.HateoasConfigurationFactory#PROPERTY_HATEOAS_LINK_INJECTOR
     * @see com.jayway.jaxrs.hateoas.core.HateoasConfigurationFactory#PROPERTY_HATEOAS_COLLECTION_WRAPPER_STRATEGY
     * @see com.jayway.jaxrs.hateoas.core.HateoasConfigurationFactory#PROPERTY_HATEOAS_VIEW_FACTORY
     */
    public JerseyHateoasApplication(Map<String, Object> props) {
        this(HateoasConfigurationFactory.createLinkInjector(props),
                HateoasConfigurationFactory.createCollectionWrapperStrategy(props),
                HateoasConfigurationFactory.createVerbosity(props),
                HateoasConfigurationFactory.createHateoasViewFactory(props, JerseyHateoasViewFactory.class.getName()),
                props);
    }

    public JerseyHateoasApplication(HateoasLinkInjector<Object> linkInjector,
                                    CollectionWrapperStrategy collectionWrapperStrategy,
                                    HateoasVerbosity verbosity,
                                    HateoasViewFactory viewFactory,
                                    Map<String, Object> props) {
        super(props);

        Set<Class<?>> allClasses = getClasses();
        for (Class<?> clazz : allClasses) {
            HateoasContextProvider.getDefaultContext().mapClass(clazz);
        }

        HateoasResponseBuilder.configure(linkInjector, collectionWrapperStrategy, viewFactory);
        HateoasVerbosity.setDefaultVerbosity(verbosity);

        JerseyHateoasContextFilter filter = new JerseyHateoasContextFilter();

        super.getContainerRequestFilters().add(filter);
        super.getContainerResponseFilters().add(filter);
    }

}
