package com.jayway.jaxrs.hateoas.support;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.jayway.jaxrs.hateoas.*;

import java.util.Collection;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: kallestenflo
 * Date: 2/27/12
 * Time: 6:06 PM
 */
public class HateoasLinkBeanLinkInjector implements HateoasLinkInjector<Object> {
    @Override
    public boolean canInject(Object entity) {
        return (entity instanceof HateoasLinkBean);
    }

    @Override
    public Object injectLinks(Object entity, LinkProducer<Object> linkProducer, final HateoasVerbosity verbosity) {

        HateoasLinkBean linkBean = (HateoasLinkBean) entity;

        Collection<Map<String,Object>> links = Collections2.transform(linkProducer.getLinks(entity),
                new Function<HateoasLink, Map<String, Object>>() {
                    @Override
                    public Map<String, Object> apply(HateoasLink from) {
                        return from.toMap(verbosity);
                    }
                });

        linkBean.setLinks(links);

        return linkBean;
    }
}
