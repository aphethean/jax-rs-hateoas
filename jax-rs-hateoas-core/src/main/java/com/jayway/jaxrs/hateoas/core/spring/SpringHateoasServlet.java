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

import com.jayway.jaxrs.hateoas.*;
import com.jayway.jaxrs.hateoas.core.HateoasConfigurationFactory;
import com.jayway.jaxrs.hateoas.core.HateoasResponse.HateoasResponseBuilder;
import com.jayway.jaxrs.hateoas.core.jersey.JerseyHateoasContextFilter;
import com.jayway.jaxrs.hateoas.core.jersey.JerseyHateoasViewFactory;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.WebApplication;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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


    private final static JerseyHateoasContextFilter HATEOAS_CONTEXT_FILTER = new JerseyHateoasContextFilter();


    @Override
    protected void initiate(ResourceConfig rc, WebApplication wa) {
        addHateoasRequestFilterIfApplicable(rc);
        addHateoasResponseFilterIfApplicable(rc);

        super.initiate(rc, wa);

        Set<Class<?>> allClasses = rc.getRootResourceClasses();
        for (Class<?> clazz : allClasses) {
            HateoasContextProvider.getDefaultContext().mapClass(clazz);
        }

        HateoasLinkInjector<Object> linkInjector = HateoasConfigurationFactory.createLinkInjector(rc.getProperties());
        CollectionWrapperStrategy collectionWrapperStrategy = HateoasConfigurationFactory.createCollectionWrapperStrategy(rc.getProperties());
        HateoasVerbosity verbosity = HateoasConfigurationFactory.createVerbosity(rc.getProperties());
        HateoasViewFactory viewFactory = HateoasConfigurationFactory.createHateoasViewFactory(rc.getProperties(), JerseyHateoasViewFactory.class.getName());

        HateoasResponseBuilder.configure(linkInjector, collectionWrapperStrategy, viewFactory);
        HateoasVerbosity.setDefaultVerbosity(verbosity);
    }


    private void addHateoasRequestFilterIfApplicable(ResourceConfig rc) {
        if (!rc.getContainerRequestFilters().contains(HATEOAS_CONTEXT_FILTER)) {
            boolean found = false;
            for (Object f : rc.getContainerRequestFilters()) {
                if (f instanceof String) {
                    String filterString = (String) f;
                    if (filterString.contains(HATEOAS_CONTEXT_FILTER.getClass().getName())) {
                        found = true;
                        break;
                    }
                } else if (f instanceof ContainerRequestFilter) {
                    ContainerRequestFilter r = (ContainerRequestFilter) f;
                    if (HATEOAS_CONTEXT_FILTER.equals(r)) {
                        found = true;
                        break;
                    }
                }
            }
            if(!found){
                rc.getContainerRequestFilters().add(HATEOAS_CONTEXT_FILTER);
            }
        }
    }

    private void addHateoasResponseFilterIfApplicable(ResourceConfig rc) {
        if (!rc.getContainerResponseFilters().contains(HATEOAS_CONTEXT_FILTER)) {
            boolean found = false;
            for (Object f : rc.getContainerResponseFilters()) {
                if (f instanceof String) {
                    String filterString = (String) f;
                    if (filterString.contains(HATEOAS_CONTEXT_FILTER.getClass().getName())) {
                        found = true;
                        break;
                    }
                } else if (f instanceof ContainerResponseFilter) {
                    ContainerResponseFilter r = (ContainerResponseFilter) f;
                    if (HATEOAS_CONTEXT_FILTER.equals(r)) {
                        found = true;
                        break;
                    }
                }
            }
            if(!found){
                rc.getContainerResponseFilters().add(HATEOAS_CONTEXT_FILTER);
            }
        }
    }

}
