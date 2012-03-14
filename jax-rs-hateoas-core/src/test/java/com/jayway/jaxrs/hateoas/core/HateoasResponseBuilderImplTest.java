package com.jayway.jaxrs.hateoas.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import javax.ws.rs.core.Response;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.jayway.jaxrs.hateoas.web.RequestContext;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RequestContext.class})
public class HateoasResponseBuilderImplTest {

	@Test
	public void testStatusIntToStatusType() {
		// mock the static call to RequestContext.getRequestContext() in build()
		mockStatic(RequestContext.class);
		RequestContext mockRequestContext = mock(RequestContext.class);
		when(RequestContext.getRequestContext()).thenReturn(mockRequestContext);

		HateoasResponseBuilderImpl builder = new HateoasResponseBuilderImpl();
		builder.status(500);
		assertEquals(Response.Status.INTERNAL_SERVER_ERROR, Response.Status.fromStatusCode(builder.build().getStatus()));
	}

}
