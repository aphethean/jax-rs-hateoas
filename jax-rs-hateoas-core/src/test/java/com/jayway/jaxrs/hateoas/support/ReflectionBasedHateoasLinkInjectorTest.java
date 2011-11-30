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
import com.jayway.jaxrs.hateoas.HateoasLink;
import com.jayway.jaxrs.hateoas.HateoasVerbosity;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static junit.framework.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Mattias Hellborg Arthursson
 */
public class ReflectionBasedHateoasLinkInjectorTest {

	private final static Map<String, Object> EXPECTED_MAP = new HashMap<String, Object>();
	private ReflectionBasedHateoasLinkInjector tested;
	private HateoasLink linkMock;

	@Before
	public void prepareTestedInstance() {
		tested = new ReflectionBasedHateoasLinkInjector();
		linkMock = mock(HateoasLink.class);
		when(linkMock.toMap(HateoasVerbosity.MINIMUM)).thenReturn(EXPECTED_MAP);
	}

	@Test
	public void linksAreInjectedInLinksField() {
		DummyEntity entity = new DummyEntity();

		Object returnedEntity = tested.injectLinks(entity,
				Arrays.asList(linkMock), HateoasVerbosity.MINIMUM);

		assertSame(entity, returnedEntity);
		assertSame(EXPECTED_MAP, Iterables.getOnlyElement(entity.getLinks()));
	}

	@Test
    @SuppressWarnings("unchecked")
	public void entityCollectionWillResultInCollectionWrapper() {
		DummyEntity expectedEntity = new DummyEntity();
		List<DummyEntity> entityList = Arrays.asList(expectedEntity);

		HateoasCollectionWrapper returnedEntity = (HateoasCollectionWrapper) tested
				.injectLinks(entityList, Arrays.asList(linkMock),
						HateoasVerbosity.MINIMUM);

		assertSame(expectedEntity, Iterables.getOnlyElement(returnedEntity));
		assertSame(EXPECTED_MAP,
				Iterables.getOnlyElement(returnedEntity.getLinks()));
	}

	private final static class DummyEntity {
		private Collection<Map<String, Object>> links = new LinkedList<Map<String, Object>>();

		public Collection<Map<String, Object>> getLinks() {
			return links;
		}
	}

}
