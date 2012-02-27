package com.jayway.jaxrs.hateoas;

/**
 * Creates a view representation using the given template and model;
 *
 * @author Kalle Stenflo
 */
public interface HateoasViewFactory {
    
    
    public Object createView(String template, Object model);
}
