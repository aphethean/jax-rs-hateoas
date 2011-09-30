package com.jayway.jaxrs.hateoas.core;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Maps;
import com.jayway.jaxrs.hateoas.HateoasOption;
import com.jayway.jaxrs.hateoas.HateoasLink;
import com.jayway.jaxrs.hateoas.HateoasVerbosity;
import com.jayway.jaxrs.hateoas.LinkableInfo;
import com.jayway.jaxrs.hateoas.web.RequestContext;

/**
 * Created by IntelliJ IDEA. User: kallestenflo Date: 8/2/11 Time: 9:57 AM
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
		URI requestURI = RequestContext.getBasePath()
				.path(linkableInfo.getMethodPath()).build(params);

		return new DefaultHateoasLink(linkableInfo.getId(), rel,
				requestURI.toASCIIString(), linkableInfo.getConsumes(),
				linkableInfo.getProduces(), linkableInfo.getHttpMethod(),
				linkableInfo.getLabel(), linkableInfo.getDescription(),
				linkableInfo.getTemplateClass());
	}

}
