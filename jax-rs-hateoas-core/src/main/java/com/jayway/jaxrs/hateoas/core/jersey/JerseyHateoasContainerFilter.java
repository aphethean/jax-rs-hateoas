package com.jayway.jaxrs.hateoas.core.jersey;

import com.jayway.jaxrs.hateoas.web.RequestContext;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: kallestenflo
 * Date: 10/10/11
 * Time: 1:09 PM
 */
public class JerseyHateoasContainerFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger log = LoggerFactory.getLogger(JerseyHateoasContainerFilter.class);

    @Override
    public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {

        log.debug("container response filter");

        return response;
    }

    @Override
    public ContainerRequest filter(ContainerRequest request) {

        log.debug("container request filter");
        log.debug("request.getAbsolutePath : " + request.getAbsolutePath());
        log.debug("request.getBaseUri : " + request.getBaseUri());

        //RequestContext.clearCurrentRequest();

        return request;
    }
}
