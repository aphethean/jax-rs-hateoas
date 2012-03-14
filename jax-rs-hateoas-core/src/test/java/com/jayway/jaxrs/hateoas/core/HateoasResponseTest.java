package com.jayway.jaxrs.hateoas.core;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.jayway.jaxrs.hateoas.HateoasContext;
import com.jayway.jaxrs.hateoas.HateoasContextProvider;
import com.jayway.jaxrs.hateoas.LinkableInfo;
import com.jayway.jaxrs.hateoas.core.HateoasResponse.HateoasResponseBuilder;
import com.jayway.jaxrs.hateoas.support.AtomRels;
import com.jayway.jaxrs.hateoas.web.RequestContext;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RequestContext.class, HateoasContextProvider.class})
public class HateoasResponseTest {

	@Test
	public void testNewInstance() {
		mockStatic(RequestContext.class);
		RequestContext mockRequestContext = mock(RequestContext.class);
		when(RequestContext.getRequestContext()).thenReturn(mockRequestContext);

		HateoasResponseBuilder builder = HateoasResponse.status(Response.Status.OK);
		assertEquals(200, builder.build().getStatus());
	}

	@Test
	public void testLinkDefaultContext() throws URISyntaxException {
		RequestContext mockRequestContext = mock(RequestContext.class);
		UriBuilder mockURIBuilder = mock(UriBuilder.class);
		when(mockURIBuilder.path(anyString())).thenReturn(mockURIBuilder);
		when(mockURIBuilder.build()).thenReturn(new URI("/test"));
		when(mockRequestContext.getBasePath()).thenReturn(mockURIBuilder);
		
		mockStatic(RequestContext.class);
		when(RequestContext.getRequestContext()).thenReturn(mockRequestContext);

		HateoasContext hc = mock(HateoasContext.class);
		when(hc.getLinkableInfo(anyString())).thenReturn(new LinkableInfo("test", "/test", "GET", new String[] {"*"}, new String[] {"*"}, "label", "description", null));

		mockStatic(HateoasContextProvider.class);
		when(HateoasContextProvider.getDefaultContext()).thenReturn(hc);
		

		HateoasResponseBuilder builder = HateoasResponse.status(Response.Status.OK);
		// try to put a related link in the response, should use the HateoasContextProvider.getDefaultContext()
		builder.link("test", AtomRels.RELATED);
		assertEquals(200, builder.build().getStatus());
		
		// verify that the static method for constructing a HateoasContext was called
		verifyStatic();
		HateoasContextProvider.getDefaultContext();
	}

	@Test
	public void testLinkSuppliedContext() throws URISyntaxException {
		RequestContext mockRequestContext = mock(RequestContext.class);
		UriBuilder mockURIBuilder = mock(UriBuilder.class);
		when(mockURIBuilder.path(anyString())).thenReturn(mockURIBuilder);
		when(mockURIBuilder.build()).thenReturn(new URI("/test"));
		when(mockRequestContext.getBasePath()).thenReturn(mockURIBuilder);
		
		mockStatic(RequestContext.class);
		when(RequestContext.getRequestContext()).thenReturn(mockRequestContext);

		HateoasContext hc = mock(HateoasContext.class);
		when(hc.getLinkableInfo(anyString())).thenReturn(new LinkableInfo("test", "/test", "GET", new String[] {"*"}, new String[] {"*"}, "label", "description", null));


		HateoasResponseBuilder builder = HateoasResponse.status(Response.Status.OK);
		// try to put a related link in the response, should use the supplied HateoasContext
		builder.link(hc, "test", AtomRels.RELATED);
		assertEquals(200, builder.build().getStatus());
	}

	@Test
	public void testSelfLinkDefaultContext() throws URISyntaxException {
		RequestContext mockRequestContext = mock(RequestContext.class);
		UriBuilder mockURIBuilder = mock(UriBuilder.class);
		when(mockURIBuilder.path(anyString())).thenReturn(mockURIBuilder);
		when(mockURIBuilder.build()).thenReturn(new URI("/test"));
		when(mockRequestContext.getBasePath()).thenReturn(mockURIBuilder);
		
		mockStatic(RequestContext.class);
		when(RequestContext.getRequestContext()).thenReturn(mockRequestContext);

		HateoasContext hc = mock(HateoasContext.class);
		when(hc.getLinkableInfo(anyString())).thenReturn(new LinkableInfo("test", "/test", "GET", new String[] {"*"}, new String[] {"*"}, "label", "description", null));

		mockStatic(HateoasContextProvider.class);
		when(HateoasContextProvider.getDefaultContext()).thenReturn(hc);
		

		HateoasResponseBuilder builder = HateoasResponse.status(Response.Status.OK);
		// try to put a related link in the response, should use the HateoasContextProvider.getDefaultContext()
		builder.selfLink("test");
		assertEquals(200, builder.build().getStatus());
		
		// verify that the static method for constructing a HateoasContext was called
		verifyStatic();
		HateoasContextProvider.getDefaultContext();
	}

	@Test
	public void testSelfLinkSuppliedContext() throws URISyntaxException {
		RequestContext mockRequestContext = mock(RequestContext.class);
		UriBuilder mockURIBuilder = mock(UriBuilder.class);
		when(mockURIBuilder.path(anyString())).thenReturn(mockURIBuilder);
		when(mockURIBuilder.build()).thenReturn(new URI("/test"));
		when(mockRequestContext.getBasePath()).thenReturn(mockURIBuilder);
		
		mockStatic(RequestContext.class);
		when(RequestContext.getRequestContext()).thenReturn(mockRequestContext);

		HateoasContext hc = mock(HateoasContext.class);
		when(hc.getLinkableInfo(anyString())).thenReturn(new LinkableInfo("test", "/test", "GET", new String[] {"*"}, new String[] {"*"}, "label", "description", null));


		HateoasResponseBuilder builder = HateoasResponse.status(Response.Status.OK);
		// try to put a related link in the response, should use the supplied HateoasContext
		builder.selfLink(hc, "test");
		assertEquals(200, builder.build().getStatus());
	}

}
