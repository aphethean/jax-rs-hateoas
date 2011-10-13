package com.jayway.jaxrs.hateoas;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class DefaultHateoasContextTest {

	private DefaultHateoasContext tested;

	@Before
	public void setupTestedInstance() {
		tested = new DefaultHateoasContext();
		tested.mapClass(DummyAnnotatedClass.class);
		tested.mapClass(SneakyAnnotatedClass.class);
	}

	@Test
	public void verifyFullyDocumented() {

		LinkableInfo result = tested
				.getLinkableInfo("test.dummy.fully.documented");

		assertNotNull(result);
		assertEquals("test.dummy.fully.documented", result.getId());
		assertEquals("dummy", result.getRel());
		assertEquals("PUT", result.getHttpMethod());
		assertEquals("/dummy", result.getMethodPath());
		assertEquals("test label", result.getLabel());
		assertEquals("test description", result.getDescription());
		assertEquals(DummyDto.class, result.getTemplateClass());

		TestUtils.assertArray(result.getConsumes(), "application/json", "application/xml");
		TestUtils.assertArray(result.getProduces(), "text/html", "text/plain");
	}

	@Test
	public void verifyGetMethod() {
		LinkableInfo result = tested.getLinkableInfo("test.dummy.get");

		assertEquals("test.dummy.get", result.getId());
		assertEquals("dummy2", result.getRel());
		assertEquals("GET", result.getHttpMethod());
		assertEquals("/dummy", result.getMethodPath());

		TestUtils.assertArray(result.getConsumes(), "*/*");
		TestUtils.assertArray(result.getProduces(), "*/*");
	}

	@Test
	public void verifyPostMethod() {
		LinkableInfo result = tested.getLinkableInfo("test.dummy.post");
		assertEquals("test.dummy.post", result.getId());
		assertEquals("dummy", result.getRel());
		assertEquals("POST", result.getHttpMethod());
		assertEquals("/dummy", result.getMethodPath());

		TestUtils.assertArray(result.getConsumes(), "application/json");
		TestUtils.assertArray(result.getProduces(), "*/*");
	}

	@Test
	public void verifyDeleteMethod() {
		LinkableInfo result = tested.getLinkableInfo("test.dummy.delete");
		assertEquals("test.dummy.delete", result.getId());
		assertEquals("dummy", result.getRel());
		assertEquals("DELETE", result.getHttpMethod());
		assertEquals("/dummy", result.getMethodPath());
	}

	@Test
	public void verifySubpath() {
		LinkableInfo result = tested.getLinkableInfo("test.dummy.get.subpath");

		assertEquals("test.dummy.get.subpath", result.getId());
		assertEquals("dummy2", result.getRel());
		assertEquals("GET", result.getHttpMethod());
		assertEquals("/dummy/subpath", result.getMethodPath());
	}

	@Test
	public void verifySneakyPath() {
		LinkableInfo result = tested.getLinkableInfo("test.dummy.sneaky.get");

		assertEquals("test.dummy.sneaky.get", result.getId());
		assertEquals("dummy2", result.getRel());
		assertEquals("GET", result.getHttpMethod());
		assertEquals("/dummy", result.getMethodPath());
	}

	@Test
	public void verifySneakySubpath() {
		LinkableInfo result = tested
				.getLinkableInfo("test.dummy.sneaky.subpath");

		assertEquals("test.dummy.sneaky.subpath", result.getId());
		assertEquals("dummy2", result.getRel());
		assertEquals("GET", result.getHttpMethod());
		assertEquals("/dummy/subpath", result.getMethodPath());
	}

	@Test(expected = IllegalArgumentException.class)
	public void unmappedLinkThrowsException() {
		tested.getLinkableInfo("not.mapped");
	}

	@Test(expected = IllegalArgumentException.class)
	public void duplicateMappingThrowsException() {
		// This class contains a linkable with id already defined in
		// DummyAnnotatedClass, so an exception should be thrown.
		tested.mapClass(DuplicateAnnotatedClass.class);
	}
}
