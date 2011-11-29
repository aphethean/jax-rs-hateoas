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

import com.jayway.jaxrs.hateoas.HateoasContextProvider;
import com.jayway.jaxrs.hateoas.HateoasLinkInjector;
import com.jayway.jaxrs.hateoas.HateoasVerbosity;
import com.jayway.jaxrs.hateoas.core.HateoasResponse.HateoasResponseBuilder;
import com.jayway.jaxrs.hateoas.support.JavassistHateoasLinkInjector;
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
 */
public class SpringHateoasServlet extends SpringServlet {

    private final static Logger logger = LoggerFactory.getLogger(SpringHateoasServlet.class);

    public final static String PROPERTY_LINK_INJECTOR = "com.jayway.jaxrs.hateoas.property.LinkInjector";
    public static final String PROPERTY_HATEOAS_VERBOSITY = "com.jayway.jaxrs.hateoas.property.Verbosity";

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

        HateoasResponseBuilder.configure(getLinkInjector(rc));
        HateoasVerbosity.setDefaultVerbosity(getVerbosity(rc));
    }

    @SuppressWarnings("unchecked")
    private HateoasLinkInjector<Object> getLinkInjector(ResourceConfig rc) {
        HateoasLinkInjector<Object> linkInjector = null;

        Object linkInjectorProperty = rc.getProperty(PROPERTY_LINK_INJECTOR);
        if (linkInjectorProperty == null) {
            logger.info("Using default LinkInjcetor", linkInjectorProperty);
            linkInjector = new JavassistHateoasLinkInjector();
        } else {
            logger.info("Using {} as LinkInjector", linkInjectorProperty);
            try {
                linkInjector = (HateoasLinkInjector<Object>)
                        Class.forName((String) linkInjectorProperty).newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Failed to instantiate " + linkInjector);
            }
        }

        return linkInjector;
    }

    private HateoasVerbosity getVerbosity(ResourceConfig rc) {
        Object hateoasVerbosity = rc.getProperty(PROPERTY_HATEOAS_VERBOSITY);

        if (hateoasVerbosity != null) {
            logger.info("Using Verbosity: {}", hateoasVerbosity);
            return HateoasVerbosity.valueOf((String) hateoasVerbosity);
        } else {
            logger.info("Using default verbosity");
            return HateoasVerbosity.MAXIMUM;
        }
    }
}
