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
package com.jayway.jaxrs.hateoas.support;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.jayway.jaxrs.hateoas.LinkProducer;
import com.jayway.jaxrs.hateoas.HateoasLinkInjector;
import com.jayway.jaxrs.hateoas.HateoasVerbosity;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Wrapper class for collection entities to enable root links in addition to the actual collection. Not for use
 * outside of the framework.
 *
 * @author Mattias Hellborg Arthursson
 * @author Kalle Stenflo
 */
public class DefaultCollectionWrapper<T> implements Iterable<T> {
    public final static String ROWS_FIELD_NAME = "rows";
    public final static String LINKS_FIELD_NAME = "links";

	private Collection<T> rows;
	private Collection<Map<String, Object>> links;

	public DefaultCollectionWrapper(Collection<T> originalCollection) {
		rows = originalCollection;
	}

	public Collection<T> getRows() {
		return rows;
	}

	public void setRows(Collection<T> rows) {
		this.rows = rows;
	}

	public Collection<Map<String, Object>> getLinks() {
		return links;
	}

	public void setLinks(Collection<Map<String, Object>> links) {
		this.links = links;
	}

	@Override
	public Iterator<T> iterator() {
		return rows.iterator();
	}

	public void transformRows(final HateoasLinkInjector<T> linkInjector,
			final LinkProducer<T> linkProducer, final HateoasVerbosity verbosity) {
		rows = Collections2.transform(rows, new Function<T, T>() {
			@Override
			public T apply(T from) {
				return linkInjector.injectLinks(from, linkProducer, verbosity);
			}
		});
	}
}
