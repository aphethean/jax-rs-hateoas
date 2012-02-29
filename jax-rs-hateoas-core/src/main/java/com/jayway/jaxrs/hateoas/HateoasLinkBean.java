package com.jayway.jaxrs.hateoas;

import java.util.Collection;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: kallestenflo
 * Date: 2/27/12
 * Time: 4:54 PM
 */
public interface HateoasLinkBean {

    Collection<Map<String, Object>> getLinks();

    void setLinks(Collection<Map<String, Object>> links);

}
