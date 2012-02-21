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

package com.jayway.jaxrs.hateoas.core.spring;

import com.jayway.jaxrs.hateoas.CollectionWrapperStrategy;
import com.jayway.jaxrs.hateoas.HateoasContextProvider;
import com.jayway.jaxrs.hateoas.HateoasLinkInjector;
import com.jayway.jaxrs.hateoas.HateoasVerbosity;
import com.jayway.jaxrs.hateoas.core.HateoasConfigurationFactory;
import com.jayway.jaxrs.hateoas.core.HateoasResponse.HateoasResponseBuilder;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.spi.container.WebApplication;
import com.sun.jersey.spi.container.servlet.WebConfig;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.util.Map;
import java.util.Set;

/**
 * Adds HATEOAS capability to the Jersey {@link SpringServlet}. Use this instead of the standard SpringServlet to add
 * hypermedia capability to a Spring/Jersey application.
 *
 * @author Mattias Hellborg Arthursson
 * @author Kalle Stenflo
 */
public class SpringHateoasServlet extends SpringServlet {

    private final static Logger logger = LoggerFactory.getLogger(SpringHateoasServlet.class);

    @Override
    protected ResourceConfig getDefaultResourceConfig(Map<String, Object> props,
                                                      WebConfig webConfig) throws ServletException {
        return new DefaultSpringResourceConfig();
    }

    @Override
    protected void initiate(ResourceConfig rc, WebApplication wa) {
        super.initiate(rc, wa);

        Set<Class<?>> allClasses = rc.getRootResourceClasses();
        for (Class<?> clazz : allClasses) {
            HateoasContextProvider.getDefaultContext().mapClass(clazz);
        }

        HateoasLinkInjector<Object> linkInjector = HateoasConfigurationFactory.createLinkInjector(rc.getProperties());
        CollectionWrapperStrategy collectionWrapperStrategy = HateoasConfigurationFactory.createCollectionWrapperStrategy(rc.getProperties());
        HateoasVerbosity verbosity = HateoasConfigurationFactory.createVerbosity(rc.getProperties());

        HateoasResponseBuilder.configure(linkInjector, collectionWrapperStrategy);
        HateoasVerbosity.setDefaultVerbosity(verbosity);
    }
}
