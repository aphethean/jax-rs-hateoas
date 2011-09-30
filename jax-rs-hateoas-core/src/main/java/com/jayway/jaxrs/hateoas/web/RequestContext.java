package com.jayway.jaxrs.hateoas.web;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.lang.StringUtils;

public class RequestContext {
	private static final String HATEOAS_OPTIONS_HEADER = "x-jax-rs-hateoas-options";
	private final static ThreadLocal<HttpServletRequest> currentRequest = new ThreadLocal<HttpServletRequest>();

	public static void setCurrentRequest(ServletRequest request) {
		currentRequest.set((HttpServletRequest) request);
	}

	public static HttpServletRequest getCurrentRequest() {
		return currentRequest.get();
	}

	public static void clearCurrentRequest() {
		currentRequest.remove();
	}

	public static UriBuilder getBasePath() {
		HttpServletRequest request = getCurrentRequest();

		String requestURI = request.getRequestURI();
		requestURI = StringUtils.removeStart(requestURI,
				request.getContextPath() + request.getServletPath());

		String baseURL = StringUtils.removeEnd(request.getRequestURL()
				.toString(), requestURI);
		return UriBuilder.fromUri(baseURL);
	}

	public static String getVerbosityHeader() {
		HttpServletRequest request = getCurrentRequest();
		return request.getHeader(HATEOAS_OPTIONS_HEADER);
	}
}
