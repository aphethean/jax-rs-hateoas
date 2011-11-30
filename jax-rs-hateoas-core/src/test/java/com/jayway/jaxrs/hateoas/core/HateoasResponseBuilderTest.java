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

package com.jayway.jaxrs.hateoas.core;

import com.jayway.jaxrs.hateoas.EachCallback;
import com.jayway.jaxrs.hateoas.HateoasLink;
import com.jayway.jaxrs.hateoas.core.HateoasResponse.HateoasResponseBuilder;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.mock;

public class HateoasResponseBuilderTest {
    @Test
    public void verifyMultipleEachCallbacks(){
        HateoasResponseBuilder tested = HateoasResponse.ok();
        final HateoasLink link1 = mock(HateoasLink.class);
        final HateoasLink link2 = mock(HateoasLink.class);
        final HateoasLink link3 = mock(HateoasLink.class);

        tested.each(new EachCallback<Object>() {
            @Override
            public Collection<HateoasLink> getLinks(Object entity) {
                return Arrays.asList(link1, link2);
            }
        });

        tested.each(new EachCallback<Object>() {
            @Override
            public Collection<HateoasLink> getLinks(Object entity) {
                return Arrays.asList(link3);
            }
        });

        EachCallback<Object> eachCallback = (EachCallback<Object>) Whitebox.getInternalState(tested, "eachCallback");
        Collection<HateoasLink> result = eachCallback.getLinks("tjoho");

        assertArrayEquals(new Object[]{link1, link2, link3}, result.toArray());
    }
}
