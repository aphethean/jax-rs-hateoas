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

package com.jayway.jaxrs.hateoas.support;

import com.google.common.collect.Iterables;
import com.jayway.jaxrs.hateoas.HateoasLinkInjector;
import com.jayway.jaxrs.hateoas.HateoasVerbosity;
import com.jayway.jaxrs.hateoas.LinkProducer;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Mattias Hellborg Arthursson
 */
public class FieldPathTest {

    @Test
    public void testParse() {
        FieldPath tested = FieldPath.path("oneField.anotherField");

        assertEquals(2, Iterables.size(tested));
        assertEquals("oneField", Iterables.get(tested, 0));
        assertEquals("anotherField", Iterables.get(tested, 1));
    }

    @Test
    public void verifyThatEmptySegmentIsRemoved() {
        FieldPath tested = FieldPath.path("oneField.anotherField.");

        assertEquals(2, Iterables.size(tested));
        assertEquals("oneField", Iterables.get(tested, 0));
        assertEquals("anotherField", Iterables.get(tested, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyThatBlankSegmentIsNotAllowed() {
        FieldPath tested = FieldPath.path("oneField..anotherField");
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyThatBlankSegmentIsNotAllowed2() {
        FieldPath tested = FieldPath.path("..oneField");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void verifyInjectLinksForEmptyPath() throws NoSuchFieldException, IllegalAccessException {
        FieldPath tested = FieldPath.EMPTY_PATH;

        HateoasLinkInjector<Object> linkInjector = mock(HateoasLinkInjector.class);

        DummyBean expectedInput = new DummyBean();
        DummyBean expectedOutput = new DummyBean();

        LinkProducer<Object> expectedLinkProducer = mock(LinkProducer.class);

        when(linkInjector.injectLinks(expectedInput, expectedLinkProducer, HateoasVerbosity.MAXIMUM))
                .thenReturn(expectedOutput);

        Object result = tested.injectLinks(expectedInput, linkInjector, expectedLinkProducer, HateoasVerbosity.MAXIMUM);
        assertSame(expectedOutput, result);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void verifyInjectLinksForSinglePath() throws NoSuchFieldException, IllegalAccessException {
        FieldPath tested = FieldPath.path("nested");

        HateoasLinkInjector<Object> linkInjector = mock(HateoasLinkInjector.class);

        DummyBean expectedInput = new DummyBean();
        NestedBean expectedOutput = new NestedBean();

        LinkProducer<Object> expectedLinkProducer = mock(LinkProducer.class);

        when(linkInjector.injectLinks(expectedInput.nested, expectedLinkProducer, HateoasVerbosity.MAXIMUM))
                .thenReturn(expectedOutput);

        DummyBean result = (DummyBean) tested.injectLinks(expectedInput, linkInjector,
                expectedLinkProducer, HateoasVerbosity.MAXIMUM);

        assertSame(expectedInput, result);
        assertSame(expectedOutput, result.nested);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void verifyInjectLinksDeeperNestedPath() throws NoSuchFieldException, IllegalAccessException {
        FieldPath tested = FieldPath.path("nested.nested2");

        HateoasLinkInjector<Object> linkInjector = mock(HateoasLinkInjector.class);

        DummyBean expectedInput = new DummyBean();
        NestedBean2 expectedOutput = new NestedBean2();

        LinkProducer<Object> expectedLinkProducer = mock(LinkProducer.class);

        when(linkInjector.injectLinks(expectedInput.nested.nested2, expectedLinkProducer, HateoasVerbosity.MAXIMUM))
                .thenReturn(expectedOutput);

        DummyBean result = (DummyBean) tested.injectLinks(expectedInput, linkInjector,
                expectedLinkProducer, HateoasVerbosity.MAXIMUM);

        assertSame(expectedInput, result);
        assertSame(expectedOutput, result.nested.nested2);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void verifyInjectLinksDeeperNestedPathThatIsNull() throws NoSuchFieldException, IllegalAccessException {
        FieldPath tested = FieldPath.path("nested.nested2");

        HateoasLinkInjector<Object> linkInjector = mock(HateoasLinkInjector.class);

        DummyBean expectedInput = new DummyBean(null);

        LinkProducer<Object> expectedLinkProducer = mock(LinkProducer.class);

        DummyBean result = (DummyBean) tested.injectLinks(expectedInput, linkInjector,
                expectedLinkProducer, HateoasVerbosity.MAXIMUM);

        assertSame(expectedInput, result);

    }

    @Test
    @SuppressWarnings("unchecked")
    public void verifyWithNestedCollection() throws NoSuchFieldException, IllegalAccessException {
        FieldPath tested = FieldPath.path("nestedBeans");

        HateoasLinkInjector<Object> linkInjector = mock(HateoasLinkInjector.class);

        NestedBean expectedInputItem1 = new NestedBean();
        NestedBean expectedInputItem2 = new NestedBean();
        NestedBean expectedOutputItem1 = new NestedBean();
        NestedBean expectedOutputItem2 = new NestedBean();

        CollectionContainingNested expectedInput =
                new CollectionContainingNested(Arrays.asList(expectedInputItem1, expectedInputItem2));

        LinkProducer<Object> expectedLinkProducer = mock(LinkProducer.class);

        when(linkInjector.injectLinks(expectedInputItem1, expectedLinkProducer, HateoasVerbosity.MAXIMUM))
                .thenReturn(expectedOutputItem1);
        when(linkInjector.injectLinks(expectedInputItem2, expectedLinkProducer, HateoasVerbosity.MAXIMUM))
                .thenReturn(expectedOutputItem2);

        CollectionContainingNested result = (CollectionContainingNested) tested.injectLinks(expectedInput, linkInjector,
                expectedLinkProducer,
                HateoasVerbosity.MAXIMUM);
        assertSame(expectedInput, result);
        assertSame(expectedOutputItem1, Iterables.get(result.nestedBeans, 0));
        assertSame(expectedOutputItem2, Iterables.get(result.nestedBeans, 1));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void verifyWithNestedCollectionThatIsNull() throws NoSuchFieldException, IllegalAccessException {
        FieldPath tested = FieldPath.path("nestedBeans");

        HateoasLinkInjector<Object> linkInjector = mock(HateoasLinkInjector.class);

        CollectionContainingNested expectedInput =
                new CollectionContainingNested(null);

        LinkProducer<Object> expectedLinkProducer = mock(LinkProducer.class);

        CollectionContainingNested result = (CollectionContainingNested) tested.injectLinks(expectedInput, linkInjector,
                expectedLinkProducer,
                HateoasVerbosity.MAXIMUM);
        assertSame(expectedInput, result);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void verifyWithPathExtendingOverNestedCollection() {
        FieldPath tested = FieldPath.path("nestedBeans.nested2");


        NestedBean expectedInputItem1 = new NestedBean();
        NestedBean expectedInputItem2 = new NestedBean();
        NestedBean2 expectedOutputItem1 = new NestedBean2();
        NestedBean2 expectedOutputItem2 = new NestedBean2();

        HateoasLinkInjector<Object> linkInjector = mock(HateoasLinkInjector.class);
        LinkProducer<Object> expectedLinkProducer = mock(LinkProducer.class);

        CollectionContainingNested expectedInput =
                new CollectionContainingNested(Arrays.asList(expectedInputItem1, expectedInputItem2));

        when(linkInjector.injectLinks(expectedInputItem1.nested2, expectedLinkProducer, HateoasVerbosity.MAXIMUM))
                .thenReturn(expectedOutputItem1);
        when(linkInjector.injectLinks(expectedInputItem2.nested2, expectedLinkProducer, HateoasVerbosity.MAXIMUM))
                .thenReturn(expectedOutputItem2);

        CollectionContainingNested result = (CollectionContainingNested) tested.injectLinks(expectedInput, linkInjector,
                expectedLinkProducer,
                HateoasVerbosity.MAXIMUM);

        assertSame(expectedOutputItem1, Iterables.get(result.nestedBeans, 0).nested2);
        assertSame(expectedOutputItem2, Iterables.get(result.nestedBeans, 1).nested2);
    }


    public final static class DummyBean {
        private NestedBean nested;

        public DummyBean(NestedBean nested) {
            this.nested = nested;
        }

        public DummyBean() {
            nested = new NestedBean();
        }
    }

    public final static class NestedBean {
        private NestedBean2 nested2;

        public NestedBean() {
            nested2 = new NestedBean2();
        }
    }

    public final static class NestedBean2 {

    }

    public final static class CollectionContainingNested {
        private Collection<NestedBean> nestedBeans;
        private String name = "tjoho";

        public CollectionContainingNested(Collection<NestedBean> nestedBeans) {
            this.nestedBeans = nestedBeans;
        }
    }
}
