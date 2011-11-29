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
 * This enum contains all valid values for HATEOAS options.
 * 
 * @author Mattias Hellborg Arthursson
 * @author Kalle Stenflo
 */
public enum HateoasOption {
    /**
     * Includes the Linkable id in the 'id' attribute of the produced link.
     */
	ID {
		@Override
		public void addTo(Map<String, Object> map, HateoasLink link) {
			map.put("id", link.getId());
		}
	},
    /**
     * Includes the rel in the 'rel' attribute of the produced link.
     */
	REL {
		@Override
		public void addTo(Map<String, Object> map, HateoasLink link) {
			map.put("rel", link.getRel());
		}
	},
    /**
     * Includes the href in the 'href' attribute of the produced link.
     */
	HREF {
		@Override
		public void addTo(Map<String, Object> map, HateoasLink link) {
			map.put("href", link.getHref());
		}
	},
    /**
     * Includes the value (if any) of the @Consumes annotation in the 'consumes' attribute of the produced link.
     */
	CONSUMES {
		@Override
		public void addTo(Map<String, Object> map, HateoasLink link) {
			if (!"GET".equals(link.getMethod())
					&& !"DELETE".equals(link.getMethod())) {
				map.put("consumes", link.getConsumes());
			}
		}
	},
    /**
     * Includes the value (if any) of the @Produces annotation in the 'produces' attribute of the produced link.
     */
	PRODUCES {
		@Override
		public void addTo(Map<String, Object> map, HateoasLink link) {
			map.put("produces", link.getProduces());
		}
	},
    /**
     * Alternate attribute for the @Produces annotation value; adds a 'type' attribute instead (more ATOM-like).
     */
	TYPE {
		@Override
		public void addTo(Map<String, Object> map, HateoasLink link) {
			map.put("type", link.getProduces());
		}
	},
    /**
     * Includes the http method in the 'method' attribute of the produced link.
     */
	METHOD {
		@Override
		public void addTo(Map<String, Object> map, HateoasLink link) {
			map.put("method", link.getMethod());
		}
	},
    /**
     * Includes the label of the @Linkable annotation in the 'label' attribute of the produced link.
     */
	LABEL {
		@Override
		public void addTo(Map<String, Object> map, HateoasLink link) {
			map.put("label", link.getLabel());
		}
	},
    /**
     * Includes the description of the @Linkable annotation in the 'description' attribute of the produced link.
     */
	DESCRIPTION {
		@Override
		public void addTo(Map<String, Object> map, HateoasLink link) {
			map.put("description", link.getDescription());
		}
	},
    /**
     * Includes a json template representing the templateClass of the @Linkable annotation in the 'template' attribute
     * of the produced link.
     */
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