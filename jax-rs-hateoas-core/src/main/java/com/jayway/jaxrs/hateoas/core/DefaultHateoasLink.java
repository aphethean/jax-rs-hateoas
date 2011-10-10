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
package com.jayway.jaxrs.hateoas.core;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Maps;
import com.jayway.jaxrs.hateoas.HateoasOption;
import com.jayway.jaxrs.hateoas.HateoasLink;
import com.jayway.jaxrs.hateoas.HateoasVerbosity;
import com.jayway.jaxrs.hateoas.LinkableInfo;
import com.jayway.jaxrs.hateoas.web.RequestContext;

/**
 * @author Mattias Hellborg Arthursson
 * @author Kalle Stenflo
 */
class DefaultHateoasLink implements HateoasLink {
	private final String id;
	private final String rel;
	private final String href;
	private final String[] produces;
	private final String method;
	private final Class<?> templateClass;
	private final String[] consumes;
	private final String description;
	private final String label;

	DefaultHateoasLink(String id, String rel, String href, String[] consumes,
			String[] produces, String method, String label, String description,
			Class<?> templateClass) {
		this.id = id;
		this.rel = rel;
		this.href = href;
		this.consumes = consumes;
		this.produces = produces;
		this.method = method;
		this.label = label;
		this.description = description;
		this.templateClass = templateClass;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Class<?> getTemplateClass() {
		return templateClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jayway.jaxrs.hateoas.support.HateoasLink#getRel()
	 */
	@Override
	public String getRel() {
		return rel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jayway.jaxrs.hateoas.support.HateoasLink#getMethod()
	 */
	@Override
	public String getMethod() {
		return method;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jayway.jaxrs.hateoas.support.HateoasLink#getId()
	 */
	@Override
	public String getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jayway.jaxrs.hateoas.support.HateoasLink#getHref()
	 */
	@Override
	public String getHref() {
		return href;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jayway.jaxrs.hateoas.support.HateoasLink#getType()
	 */
	@Override
	public String[] getProduces() {
		return produces;
	}

	@Override
	public String[] getConsumes() {
		return consumes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jayway.jaxrs.hateoas.support.HateoasLink#toMap(com.jayway.jaxrs.hateoas
	 * .HateoasVerbosity)
	 */
	@Override
	public Map<String, Object> toMap(HateoasVerbosity verbosity) {
		HashMap<String, Object> result = Maps.newLinkedHashMap();

		for (HateoasOption element : verbosity.getOptions()) {
			element.addTo(result, this);
		}

		return result;
	}

	static DefaultHateoasLink fromLinkableInfo(LinkableInfo linkableInfo,
			Object... params) {
		return fromLinkableInfo(linkableInfo, linkableInfo.getRel(), params);
	}

	static DefaultHateoasLink fromLinkableInfo(LinkableInfo linkableInfo,
			String rel, Object... params) {
		URI requestURI = RequestContext.getRequestContext().getBasePath()
				.path(linkableInfo.getMethodPath()).build(params);

		return new DefaultHateoasLink(linkableInfo.getId(), rel,
				requestURI.toASCIIString(), linkableInfo.getConsumes(),
				linkableInfo.getProduces(), linkableInfo.getHttpMethod(),
				linkableInfo.getLabel(), linkableInfo.getDescription(),
				linkableInfo.getTemplateClass());
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DefaultHateoasLink that = (DefaultHateoasLink) o;

        if (!Arrays.equals(consumes, that.consumes)) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (href != null ? !href.equals(that.href) : that.href != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (label != null ? !label.equals(that.label) : that.label != null) return false;
        if (method != null ? !method.equals(that.method) : that.method != null) return false;
        if (!Arrays.equals(produces, that.produces)) return false;
        if (rel != null ? !rel.equals(that.rel) : that.rel != null) return false;
        if (templateClass != null ? !templateClass.equals(that.templateClass) : that.templateClass != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (rel != null ? rel.hashCode() : 0);
        result = 31 * result + (href != null ? href.hashCode() : 0);
        result = 31 * result + (produces != null ? Arrays.hashCode(produces) : 0);
        result = 31 * result + (method != null ? method.hashCode() : 0);
        result = 31 * result + (templateClass != null ? templateClass.hashCode() : 0);
        result = 31 * result + (consumes != null ? Arrays.hashCode(consumes) : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (label != null ? label.hashCode() : 0);
        return result;
    }
}
