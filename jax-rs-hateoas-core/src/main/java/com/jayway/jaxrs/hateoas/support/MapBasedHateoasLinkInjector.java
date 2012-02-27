package com.jayway.jaxrs.hateoas.support;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.jayway.jaxrs.hateoas.*;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: kallestenflo
 * Date: 2/24/12
 * Time: 4:26 PM
 */
public class MapBasedHateoasLinkInjector implements HateoasLinkInjector<Object> {

    private static final String DEFAULT_LINKS_FIELD_NAME = "links";
    private final String linksFieldName;

    public MapBasedHateoasLinkInjector(String linksFieldName) {
        this.linksFieldName = linksFieldName;
    }

    /**
     * Construct an instance using the default field name ('links').
     */
    public MapBasedHateoasLinkInjector() {
        this(DEFAULT_LINKS_FIELD_NAME);
    }

    @Override
    public boolean canInject(Object entity) {
        return (entity instanceof Map);
    }


    @Override
    public Map injectLinks(Object entity, LinkProducer<Object> linkProducer, final HateoasVerbosity verbosity) {
        try {
            Map<String, Object> entityMap = (Map<String, Object>) entity;

            Collection links;

            if (!entityMap.containsKey(linksFieldName)) {
                entityMap.put(linksFieldName, Lists.newLinkedList());
            }
            links = (Collection) entityMap.get(linksFieldName);

            Collection<Map<String, Object>> linksToAdd = Collections2.transform(linkProducer.getLinks(entity),
                    new Function<HateoasLink, Map<String, Object>>() {
                        @Override
                        public Map<String, Object> apply(HateoasLink from) {
                            return from.toMap(verbosity);
                        }
                    });
            
            links.addAll(linksToAdd);


            return entityMap;
        } catch (Exception e) {
            throw new HateoasInjectException(e);
        }
    }

}
