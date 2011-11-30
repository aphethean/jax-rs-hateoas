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
package com.jayway.jaxrs.hateoas;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link HateoasContext}. Not intended for external use. This class is configured by the
 * Application classes in the core package.

 * @author Mattias Hellborg Arthursson
 * @author Kalle Stenflo
 */
public class DefaultHateoasContext implements HateoasContext {

	private static final String[] DEFAULT_MEDIA_TYPE = { "*/*" };

	private final static Logger logger = LoggerFactory
			.getLogger(DefaultHateoasContext.class);

	private final Map<String, LinkableInfo> linkableMapping = new LinkedHashMap<String, LinkableInfo>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jayway.jaxrs.hateoas.HateoasContext#mapClass(java.lang.Class)
	 */
	@Override
	public void mapClass(Class<?> clazz) {
		if (clazz.isAnnotationPresent(Path.class)) {
			logger.info("Mapping class {}", clazz);

			String rootPath = clazz.getAnnotation(Path.class).value();
			if (rootPath.endsWith("/")) {
				rootPath = StringUtils.removeEnd(rootPath, "/");
			}

			Method[] methods = clazz.getMethods();
			for (Method method : methods) {
				mapMethod(clazz, rootPath, method);
			}

		} else {
			logger.debug("Class {} is not annotated with @Path", clazz);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jayway.jaxrs.hateoas.HateoasContext#getLinkableInfo(java.lang.String)
	 */
	@Override
	public LinkableInfo getLinkableInfo(String link) {
		LinkableInfo linkableInfo = linkableMapping.get(link);
		Validate.notNull(linkableInfo, "Invalid link: " + link);

		return linkableInfo;
	}

	private void mapMethod(Class<?> clazz, String rootPath, Method method) {
		String httpMethod = findHttpMethod(method);

		if (httpMethod != null) {
			String path = getPath(rootPath, method);
			String[] consumes = getConsumes(method);
			String[] produces = getProduces(method);

			if (method.isAnnotationPresent(Linkable.class)) {
				Linkable linkAnnotation = method.getAnnotation(Linkable.class);
				String id = linkAnnotation.id();
				if (linkableMapping.containsKey(id)) {
					throw new IllegalArgumentException("Id '" + id
							+ "' mapped in class " + clazz
							+ " is already mapped from another class");
				}
				LinkableInfo linkableInfo = new LinkableInfo(id, path,
                                                             httpMethod, consumes, produces,
						linkAnnotation.label(), linkAnnotation.description(),
						linkAnnotation.templateClass());
				linkableMapping.put(id, linkableInfo);
			} else {
				logger.warn("Method {} is missing Link annotation", method);
			}
		}
	}

	private String[] getConsumes(Method method) {
		if (method.isAnnotationPresent(Consumes.class)) {
			return method.getAnnotation(Consumes.class).value();
		}

		return DEFAULT_MEDIA_TYPE;
	}

	private String[] getProduces(Method method) {
		if (method.isAnnotationPresent(Produces.class)) {
			return method.getAnnotation(Produces.class).value();
		}

		return DEFAULT_MEDIA_TYPE;
	}

	private String getPath(String rootPath, Method method) {
		if (method.isAnnotationPresent(Path.class)) {
			Path pathAnnotation = method.getAnnotation(Path.class);
			return rootPath + pathAnnotation.value();
		}

		return rootPath.isEmpty() ? "/" : rootPath;
	}

	private String findHttpMethod(Method method) {
		if (method.isAnnotationPresent(GET.class)) {
			return HttpMethod.GET;
		}
		if (method.isAnnotationPresent(POST.class)) {
			return HttpMethod.POST;
		}
		if (method.isAnnotationPresent(PUT.class)) {
			return HttpMethod.PUT;
		}
		if (method.isAnnotationPresent(DELETE.class)) {
			return HttpMethod.DELETE;
		}
		if (method.isAnnotationPresent(OPTIONS.class)) {
			return HttpMethod.OPTIONS;
		}
		return null;
	}

	@Override
	public String toString() {
		Set<Entry<String, LinkableInfo>> entrySet = linkableMapping.entrySet();
		StringBuilder sb = new StringBuilder();
		for (Entry<String, LinkableInfo> relInfo : entrySet) {
			sb.append(relInfo.toString()).append("<br/>");
		}

		return sb.toString();
	}
}
