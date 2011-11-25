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

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author Mattias Hellborg Arthursson
 * @author Kalle Stenflo
 */
public final class LinkableInfo {
	private final String id;
	private final String methodPath;
	private final String rel;
	private final String[] produces;
	private final String httpMethod;
	private final String description;
	private final Class<?> templateClass;
	private final String[] consumes;
	private final String label;

	public LinkableInfo(String id, String methodPath, String rel,
			String httpMethod, String[] consumes, String[] produces,
			String label, String description, Class<?> templateClass) {
		this.id = id;
		this.methodPath = methodPath;
		this.rel = rel;
		this.httpMethod = httpMethod;
		this.consumes = consumes;
		this.produces = produces;
		this.label = label;
		this.description = description;
		this.templateClass = templateClass;
	}

	public Class<?> getTemplateClass() {
		return templateClass;
	}

	public String getDescription() {
		return description;
	}

	public String getRel() {
		return rel;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public String[] getConsumes() {
		return consumes;
	}

	public String[] getProduces() {
		return produces;
	}

	public String getMethodPath() {
		return methodPath;
	}

	public String getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}