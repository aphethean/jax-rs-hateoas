package com.jayway.jaxrs.hateoas;

import java.util.Map;

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