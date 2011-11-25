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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Iterables;
import com.jayway.jaxrs.hateoas.HateoasLink;
import com.jayway.jaxrs.hateoas.HateoasVerbosity;

/**
 * @author Mattias Hellborg Arthursson
 */
public class JavassistHateoasLinkInjectorTest {
	private final static Map<String, Object> EXPECTED_MAP = new HashMap<String, Object>();
	private HateoasLink linkMock;
	private JavassistHateoasLinkInjector tested;

	@Before
	public void prepareTestedInstance() {
		tested = new JavassistHateoasLinkInjector();
		linkMock = mock(HateoasLink.class);
		when(linkMock.toMap(HateoasVerbosity.MINIMUM)).thenReturn(EXPECTED_MAP);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void linksFieldIsInjectedAutomatically() {
		DummyEntity dummyEntity = new DummyEntity();
		dummyEntity.setId("someId");

		DummyEntity returnedEntity = (DummyEntity) tested.injectLinks(
				dummyEntity, Arrays.asList(linkMock), HateoasVerbosity.MINIMUM);

		assertNotSame(dummyEntity, returnedEntity);
		assertEquals("someId", returnedEntity.getId());

		Collection<Map<String, Object>> links = (Collection<Map<String, Object>>) ReflectionUtils
				.getFieldValue(returnedEntity, "links");
		assertSame(EXPECTED_MAP, Iterables.getOnlyElement(links));
	}

	@Test
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

	public static class DummyEntity {
		private String id;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}
	}
}
