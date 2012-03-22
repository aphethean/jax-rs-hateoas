package com.jayway.jaxrs.hateoas.support;

import com.google.common.collect.Lists;
import com.jayway.jaxrs.hateoas.HateoasInjectException;
import com.jayway.jaxrs.hateoas.HateoasLinkInjector;
import com.jayway.jaxrs.hateoas.HateoasVerbosity;
import com.jayway.jaxrs.hateoas.LinkProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

/**
 * Strategy based link injector that tries to inject the links with all configured {@link HateoasLinkInjector}.
 *
 * @author Kalle Stenflo
 */
public class StrategyBasedLinkInjector implements HateoasLinkInjector<Object> {

    private static final Logger log = LoggerFactory.getLogger(StrategyBasedLinkInjector.class);

    private List<HateoasLinkInjector<Object>> strategies; 
    
    public StrategyBasedLinkInjector() {
        strategies = Lists.newArrayList();
        strategies.add(new MapBasedHateoasLinkInjector());
        strategies.add(new HateoasLinkBeanLinkInjector());
        strategies.add(new ReflectionBasedHateoasLinkInjector());
        strategies.add(new JavassistHateoasLinkInjector());
    }

    public StrategyBasedLinkInjector(Collection<HateoasLinkInjector<Object>> newStrategies) {
        strategies = Lists.newArrayList();
        strategies.addAll(newStrategies);
    }

    @Override
    public boolean canInject(Object entity) {
        return true;
    }

    @Override
    public Object injectLinks(Object entity, LinkProducer<Object> objectLinkProducer, HateoasVerbosity verbosity) {
        for (HateoasLinkInjector<Object> strategy : strategies) {
            log.debug("Trying link injector strategy : " + strategy.getClass().getName() );

            if(strategy.canInject(entity)){
                log.debug("Selected link injector " + strategy.getClass().getName() + " for entity : " + entity.getClass().getName());
                return strategy.injectLinks(entity, objectLinkProducer, verbosity);
            }
        }
        throw new HateoasInjectException("No suitable injector found for " + entity.getClass());
    }
}
