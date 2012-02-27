package com.jayway.jaxrs.hateoas.support;

import com.jayway.jaxrs.hateoas.HateoasViewFactory;

/**
 * Created by IntelliJ IDEA.
 * User: kallestenflo
 * Date: 2/24/12
 * Time: 5:02 PM
 */
public class DefaultHateoasViewFactory implements HateoasViewFactory {
    @Override
    public Object createView(String template, Object model) {
        return model;
    }
}
