/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jayway.jaxrs.hateoas.web;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.lang.StringUtils;

/**
 * @author Mattias Hellborg Arthursson
 * @author Kalle Stenflo
 */
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
