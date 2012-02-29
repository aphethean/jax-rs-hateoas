package com.jayway.jaxrs.hateoas.support;

import com.google.common.collect.Iterables;
import com.jayway.jaxrs.hateoas.HateoasLink;
import com.jayway.jaxrs.hateoas.HateoasVerbosity;
import com.jayway.jaxrs.hateoas.core.HateoasResponseBuilderImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by IntelliJ IDEA.
 * User: kallestenflo
 * Date: 2/27/12
 * Time: 10:19 PM
 */
public class MapBasedHateoasLinkInjectorTest {

    private final static Map<String, Object> EXPECTED_MAP = new HashMap<String, Object>();
    private HateoasLink linkMock;
    private MapBasedHateoasLinkInjector tested;
    private HateoasResponseBuilderImpl.FixedLinkProducer linkProducer;

    @Before
    public void prepareTestedInstance() {
        tested = new MapBasedHateoasLinkInjector();
        linkMock = mock(HateoasLink.class);
        linkProducer = new HateoasResponseBuilderImpl.FixedLinkProducer(linkMock);
        when(linkMock.toMap(HateoasVerbosity.MINIMUM)).thenReturn(EXPECTED_MAP);
    }


    @SuppressWarnings("unchecked")
    @Test
    public void linksFieldIsInjectedAutomaticallyInMaps() {
        Map<String, Object> dummyEntity = new HashMap<String, Object>();
        dummyEntity.put("id", "someId");

        Map<String, Object> returnedEntity = (Map<String, Object>) tested.injectLinks(
                dummyEntity, linkProducer, HateoasVerbosity.MINIMUM);

        assertSame(dummyEntity, returnedEntity);
        assertEquals("someId", dummyEntity.get("id"));

        Collection<Map<String, Object>> links = (Collection<Map<String, Object>>) dummyEntity.get("links");
        assertSame(EXPECTED_MAP, Iterables.getOnlyElement(links));
    }
}
