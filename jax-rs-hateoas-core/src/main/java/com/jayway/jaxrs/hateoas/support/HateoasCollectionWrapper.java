package com.jayway.jaxrs.hateoas.support;

import java.util.Collection;
import java.util.Iterator;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.jayway.jaxrs.hateoas.EachCallback;
import com.jayway.jaxrs.hateoas.HateoasLink;
import com.jayway.jaxrs.hateoas.HateoasLinkInjector;
import com.jayway.jaxrs.hateoas.HateoasVerbosity;

public class HateoasCollectionWrapper implements Iterable<Object> {
	private Collection<Object> rows;
	private Collection<HateoasLink> links;

	public HateoasCollectionWrapper(Collection<Object> originalCollection) {
		rows = originalCollection;
	}

	public Collection<Object> getRows() {
		return rows;
	}

	public void setRows(Collection<Object> rows) {
		this.rows = rows;
	}

	public Collection<HateoasLink> getLinks() {
		return links;
	}

	public void setLinks(Collection<HateoasLink> links) {
		this.links = links;
	}

	@Override
	public Iterator<Object> iterator() {
		return rows.iterator();
	}

	public void transformRows(final HateoasLinkInjector linkInjector,
			final EachCallback eachCallback, final HateoasVerbosity verbosity) {
		rows = Collections2.transform(rows, new Function<Object, Object>() {
			@Override
			public Object apply(Object from) {
				return linkInjector.injectLinks(from,
						eachCallback.getLinks(from), verbosity);
			}
		});
	}
}
