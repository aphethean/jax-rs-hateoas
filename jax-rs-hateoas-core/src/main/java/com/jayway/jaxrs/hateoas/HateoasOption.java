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

import java.util.Map;

/**
 * @author Mattias Hellborg Arthursson
 * @author Kalle Stenflo
 */
public enum HateoasOption {
	ID {
		@Override
		public void addTo(Map<String, Object> map, HateoasLink link) {
			map.put("id", link.getId());
		}
	},
	REL {
		@Override
		public void addTo(Map<String, Object> map, HateoasLink link) {
			map.put("rel", link.getRel());
		}
	},
	HREF {
		@Override
		public void addTo(Map<String, Object> map, HateoasLink link) {
			map.put("href", link.getHref());
		}
	},
	CONSUMES {
		@Override
		public void addTo(Map<String, Object> map, HateoasLink link) {
			if (!"GET".equals(link.getMethod())
					&& !"DELETE".equals(link.getMethod())) {
				map.put("consumes", link.getConsumes());
			}
		}
	},
	PRODUCES {
		@Override
		public void addTo(Map<String, Object> map, HateoasLink link) {
			map.put("produces", link.getProduces());
		}
	},
	TYPE {
		@Override
		public void addTo(Map<String, Object> map, HateoasLink link) {
			map.put("type", link.getProduces());
		}
	},
	METHOD {
		@Override
		public void addTo(Map<String, Object> map, HateoasLink link) {
			map.put("method", link.getMethod());
		}
	},
	LABEL {
		@Override
		public void addTo(Map<String, Object> map, HateoasLink link) {
			map.put("label", link.getLabel());
		}
	},
	DESCRIPTION {
		@Override
		public void addTo(Map<String, Object> map, HateoasLink link) {
			map.put("description", link.getDescription());
		}
	},
	TEMPLATE {
		@Override
		public void addTo(Map<String, Object> map, HateoasLink link) {
			Class<?> templateClass = link.getTemplateClass();
			if (!"GET".equals(link.getMethod())
					&& !"DELETE".equals(link.getMethod())) {
				if (!templateClass.equals(Linkable.NoTemplate.class)) {
					try {
						Object instance = templateClass.newInstance();
						map.put("template", instance);
					} catch (Exception e) {
						throw new RuntimeException(
								"Failed to instantiate class", e);
					}
				} else {
					map.put("template", "NOT_DEFINED");
				}
			}
		}
	};

	public abstract void addTo(Map<String, Object> map, HateoasLink link);
}