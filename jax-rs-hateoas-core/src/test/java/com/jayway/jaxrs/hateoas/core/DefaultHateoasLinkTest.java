package com.jayway.jaxrs.hateoas.core;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.ws.rs.core.UriBuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jayway.jaxrs.hateoas.DummyDto;
import com.jayway.jaxrs.hateoas.HateoasVerbosity;
import com.jayway.jaxrs.hateoas.Linkable;
import com.jayway.jaxrs.hateoas.LinkableInfo;
import com.jayway.jaxrs.hateoas.TestUtils;
import com.jayway.jaxrs.hateoas.web.RequestContext;

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
				"/dummy/{id1}/{id2}", "dummy", "POST", DEFAULT_MEDIA_TYPE,
				DEFAULT_MEDIA_TYPE, "test label", "test description",
				DummyDto.class);
		tested = DefaultHateoasLink.fromLinkableInfo(linkableInfo, 1, 2);
	}

	@After
	public void cleanup() {
		RequestContext.clearRequestContext();
	}

	@Test
	public void verifyFromLinkableInfo() {
		assertNotNull(tested);
		assertEquals("test.dummy", tested.getId());
		assertEquals("dummy", tested.getRel());
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
		assertEquals("dummy", result.get("rel"));
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
				"/dummy/{id1}/{id2}", "dummy", "GET", DEFAULT_MEDIA_TYPE,
				DEFAULT_MEDIA_TYPE, "test label", "test description",
				DummyDto.class);
		tested = DefaultHateoasLink.fromLinkableInfo(linkableInfo, 1, 2);
		Map<String, Object> result = tested.toMap(HateoasVerbosity.MAXIMUM);

		assertEquals("test.dummy", result.get("id"));
		assertNull(result.get("consumes"));
		TestUtils.assertArray((String[]) result.get("produces"), "*/*");
		assertNull(result.get("template"));

	}

	@Test
	public void onDeleteMethodProducesAndTemplateAreOmitted() {
		LinkableInfo linkableInfo = new LinkableInfo("test.dummy",
				"/dummy/{id1}/{id2}", "dummy", "DELETE", DEFAULT_MEDIA_TYPE,
				DEFAULT_MEDIA_TYPE, "test label", "test description",
				DummyDto.class);
		tested = DefaultHateoasLink.fromLinkableInfo(linkableInfo, 1, 2);
		Map<String, Object> result = tested.toMap(HateoasVerbosity.MAXIMUM);

		assertEquals("test.dummy", result.get("id"));
		assertNull(result.get("consumes"));
		TestUtils.assertArray((String[]) result.get("produces"), "*/*");
		assertNull(result.get("template"));
	}

	@Test
	public void whenNoTemplateExplainingStringIsReturned() {
		LinkableInfo linkableInfo = new LinkableInfo("test.dummy",
				"/dummy/{id1}/{id2}", "dummy", "PUT", DEFAULT_MEDIA_TYPE,
				DEFAULT_MEDIA_TYPE, "test label", "test description",
				Linkable.NoTemplate.class);
		tested = DefaultHateoasLink.fromLinkableInfo(linkableInfo, 1, 2);
		Map<String, Object> result = tested.toMap(HateoasVerbosity.MAXIMUM);

		assertEquals("test.dummy", result.get("id"));
		assertEquals("NOT_DEFINED", result.get("template"));
	}

}
