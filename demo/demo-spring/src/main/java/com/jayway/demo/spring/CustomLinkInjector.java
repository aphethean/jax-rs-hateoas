package com.jayway.demo.spring;

import com.jayway.jaxrs.hateoas.HateoasVerbosity;
import com.jayway.jaxrs.hateoas.LinkProducer;
import com.jayway.jaxrs.hateoas.support.JavassistHateoasLinkInjector;
import com.jayway.jaxrs.hateoas.support.StrategyBasedLinkInjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: kallestenflo
 * Date: 2/21/12
 * Time: 7:07 PM
 */
public class CustomLinkInjector extends StrategyBasedLinkInjector {

    private static final Logger log = LoggerFactory.getLogger(CustomLinkInjector.class);


    @Override
    public boolean canInject(Object entity) {
        return true;
    }

    @Override
    public Object injectLinks(Object entity, LinkProducer<Object> linkProducer, HateoasVerbosity verbosity) {

        log.debug("CustomLinkInjector injecting links");

        return super.injectLinks(entity, linkProducer, verbosity);
    }
}
