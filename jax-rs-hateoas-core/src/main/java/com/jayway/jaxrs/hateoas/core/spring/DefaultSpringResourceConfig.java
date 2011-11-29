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

import com.jayway.jaxrs.hateoas.core.jersey.JerseyHateoasContextFilter;
import com.sun.jersey.api.core.DefaultResourceConfig;

import java.util.List;

/**
 * Custom ResourceConfig implementation to be used in {@link SpringHateoasServlet},
 * adding the {@link JerseyHateoasContextFilter} automatically.
 * 
 * @author Mattias Hellborg Arthursson
 */
public class DefaultSpringResourceConfig extends DefaultResourceConfig {

    public static final String DEFAULT_FILTER_NAME = JerseyHateoasContextFilter.class.getName();

    @Override
    public List getContainerRequestFilters() {
        List requestFilters = super.getContainerRequestFilters();
        addDefaultFilterIfApplicable(requestFilters);
        return requestFilters;
    }

    private void addDefaultFilterIfApplicable(List filters) {
        if(!filters.contains(DEFAULT_FILTER_NAME)){
            filters.add(DEFAULT_FILTER_NAME);
        }
    }

    @Override
    public List getContainerResponseFilters() {
        List responseFilters = super.getContainerResponseFilters();
        addDefaultFilterIfApplicable(responseFilters);
        return responseFilters;
    }
}
