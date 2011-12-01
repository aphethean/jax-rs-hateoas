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
import com.jayway.jaxrs.hateoas.HateoasLink;
import com.jayway.jaxrs.hateoas.HateoasLinkInjector;
import com.jayway.jaxrs.hateoas.HateoasVerbosity;
import com.jayway.jaxrs.hateoas.LinkProducer;
import javassist.*;
import org.dozer.DozerBeanMapper;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link HateoasLinkInjector} implementation that uses javassist to dynamically add a field in the target entities
 * where the links can be injected. This enables usage of the framework without <b>any</b> impact on the actual DTOs.
 *
 * @author Mattias Hellborg Arthursson
 * @author Kalle Stenflo
 */
public class JavassistHateoasLinkInjector implements HateoasLinkInjector<Object> {

	private static final ClassPool CLASS_POOL = ClassPool.getDefault();

	private final static Map<String, Class<?>> transformedClasses = new HashMap<String, Class<?>>();

	static {
		CLASS_POOL.appendClassPath(new LoaderClassPath(
				JavassistHateoasLinkInjector.class.getClassLoader()));
	}

	private HateoasLinkInjector<Object> reflectionBasedDelegate = new ReflectionBasedHateoasLinkInjector();

	@Override
	public Object injectLinks(Object entity, LinkProducer<Object> linkProducer,
			final HateoasVerbosity verbosity) {

		if (entity == null) {
			return null;
		}

		if (ReflectionUtils.hasField(entity, "links")) {
			return reflectionBasedDelegate
					.injectLinks(entity, linkProducer, verbosity);
		}

		String newClassName = entity.getClass().getPackage().getName() + "."
				+ entity.getClass().getSimpleName() + "_generated";

		Class<?> clazz;
		if (!transformedClasses.containsKey(newClassName)) {
			synchronized (this) {
				try {
					CtClass newClass = CLASS_POOL.makeClass(newClassName);
					newClass.setSuperclass(CLASS_POOL.get(entity.getClass()
							.getName()));
					CtConstructor ctConstructor = new CtConstructor(
							new CtClass[0], newClass);
					ctConstructor.setBody("super();");
					newClass.addConstructor(ctConstructor);

					CtField newField = CtField.make(
							"public java.util.Collection links;", newClass);
					newClass.addField(newField);

					URLClassLoader classLoader = new URLClassLoader(new URL[0],
							this.getClass().getClassLoader());
					clazz = newClass.toClass(classLoader, this.getClass()
							.getProtectionDomain());

					transformedClasses.put(newClassName, clazz);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		} else {
			clazz = transformedClasses.get(newClassName);
		}
		DozerBeanMapper mapper = new DozerBeanMapper();

		Object newInstance = mapper.map(entity, clazz);
		ReflectionUtils.setField(newInstance, "links", Collections2.transform(
                linkProducer.getLinks(entity), new Function<HateoasLink, Map<String, Object>>() {
					@Override
					public Map<String, Object> apply(HateoasLink link) {
						return link.toMap(verbosity);
					}
				}));

		return newInstance;
	}

}
