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

import com.jayway.jaxrs.hateoas.*;
import com.jayway.jaxrs.hateoas.support.AtomRels;
import com.jayway.jaxrs.hateoas.web.RequestContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import static junit.framework.Assert.*;

public class DefaultHateoasLinkTest {

	public static final String[] DEFAULT_MEDIA_TYPE = new String[] { "*/*" };
	private DefaultHateoasLink tested;

	@Before
	public void prepareTestedInstance() throws IllegalArgumentException,
			URISyntaxException {
		RequestContext requestContext = new RequestContext(
				UriBuilder.fromUri(new URI("http://www.example.com/api")), null);
		RequestContext.setRequestContext(requestContext);
		LinkableInfo linkableInfo = new LinkableInfo("test.dummy",
				"/dummy/{id1}/{id2}", "POST", DEFAULT_MEDIA_TYPE,
				DEFAULT_MEDIA_TYPE, "test label", "test description",
				DummyDto.class);
		tested = DefaultHateoasLink.fromLinkableInfo(linkableInfo, AtomRels.SELF, 1, 2);
	}

	@After
	public void cleanup() {
		RequestContext.clearRequestContext();
	}

	@Test
	public void verifyFromLinkableInfo() {
		assertNotNull(tested);
		assertEquals("test.dummy", tested.getId());
		assertEquals("self", tested.getRel());
		assertEquals("POST", tested.getMethod());
		assertEquals("http://www.example.com/api/dummy/1/2", tested.getHref());
		assertEquals("test label", tested.getLabel());
		assertEquals("test description", tested.getDescription());
		assertEquals(DummyDto.class, tested.getTemplateClass());

		TestUtils.assertArray(tested.getConsumes(), "*/*");
		TestUtils.assertArray(tested.getProduces(), "*/*");
	}

	@Test
	public void verifyToMap() {
		Map<String, Object> result = tested.toMap(HateoasVerbosity.MAXIMUM);

		assertEquals("test.dummy", result.get("id"));
		assertEquals("self", result.get("rel"));
		assertEquals("POST", result.get("method"));
		assertEquals("http://www.example.com/api/dummy/1/2", result.get("href"));
		assertEquals("test label", result.get("label"));
		assertEquals("test description", result.get("description"));
		TestUtils.assertArray((String[]) result.get("consumes"), "*/*");
		TestUtils.assertArray((String[]) result.get("produces"), "*/*");
		assertEquals(new DummyDto(), result.get("template"));
	}

	@Test
	public void onGetMethodProducesAndTemplateAreOmitted() {
		LinkableInfo linkableInfo = new LinkableInfo("test.dummy",
				"/dummy/{id1}/{id2}", "GET", DEFAULT_MEDIA_TYPE,
				DEFAULT_MEDIA_TYPE, "test label", "test description",
				DummyDto.class);
		tested = DefaultHateoasLink.fromLinkableInfo(linkableInfo, AtomRels.SELF, 1, 2);
		Map<String, Object> result = tested.toMap(HateoasVerbosity.MAXIMUM);

		assertEquals("test.dummy", result.get("id"));
		assertNull(result.get("consumes"));
		TestUtils.assertArray((String[]) result.get("produces"), "*/*");
		assertNull(result.get("template"));

	}

	@Test
	public void onDeleteMethodProducesAndTemplateAreOmitted() {
		LinkableInfo linkableInfo = new LinkableInfo("test.dummy",
				"/dummy/{id1}/{id2}", "DELETE", DEFAULT_MEDIA_TYPE,
				DEFAULT_MEDIA_TYPE, "test label", "test description",
				DummyDto.class);
		tested = DefaultHateoasLink.fromLinkableInfo(linkableInfo, AtomRels.SELF, 1, 2);
		Map<String, Object> result = tested.toMap(HateoasVerbosity.MAXIMUM);

		assertEquals("test.dummy", result.get("id"));
		assertNull(result.get("consumes"));
		TestUtils.assertArray((String[]) result.get("produces"), "*/*");
		assertNull(result.get("template"));
	}

	@Test
	public void whenNoTemplateExplainingStringIsReturned() {
		LinkableInfo linkableInfo = new LinkableInfo("test.dummy",
				"/dummy/{id1}/{id2}", "PUT", DEFAULT_MEDIA_TYPE,
				DEFAULT_MEDIA_TYPE, "test label", "test description",
				Linkable.NoTemplate.class);
		tested = DefaultHateoasLink.fromLinkableInfo(linkableInfo, AtomRels.SELF, 1, 2);
		Map<String, Object> result = tested.toMap(HateoasVerbosity.MAXIMUM);

		assertEquals("test.dummy", result.get("id"));
		assertEquals("NOT_DEFINED", result.get("template"));
	}

}
