package com.jayway.jaxrs.hateoas.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class ResponseHeadersTest {

	@Test
	public void testGetFirstEmpty() {
		ResponseHeaders<String> headers = new ResponseHeaders<String>();
		assertNull(headers.getFirst("test"));
	}

	@Test
	public void testPutSingle() {
		ResponseHeaders<String> headers = new ResponseHeaders<String>();
		headers.putSingle("test", "abc");
		assertEquals("abc", headers.getFirst("test"));
	}

	@Test
	public void testAdd() {
		ResponseHeaders<String> headers = new ResponseHeaders<String>();
		headers.add("test", "abc");
		assertEquals("abc", headers.getFirst("test"));
	}

	@Test
	public void testPutSinglePlusAdd() {
		ResponseHeaders<String> headers = new ResponseHeaders<String>();
		headers.putSingle("test", "abc");
		headers.add("test", "xyz");
		assertEquals("abc", headers.getFirst("test"));
		assertNotNull(headers.get("test"));
		assertEquals(2, headers.get("test").size());
		assertTrue(headers.get("test").contains("abc"));
		assertTrue(headers.get("test").contains("xyz"));
	}

}
