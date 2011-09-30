package com.jayway.jaxrs.hateoas.support;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.LoaderClassPath;

import org.dozer.DozerBeanMapper;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.jayway.jaxrs.hateoas.HateoasLink;
import com.jayway.jaxrs.hateoas.HateoasLinkInjector;
import com.jayway.jaxrs.hateoas.HateoasVerbosity;

public class JavassistHateoasLinkInjector implements HateoasLinkInjector {

	private static final ClassPool CLASS_POOL = ClassPool.getDefault();

	private final static Map<String, Class<?>> transformedClasses = new HashMap<String, Class<?>>();

	static {
		CLASS_POOL.appendClassPath(new LoaderClassPath(
				JavassistHateoasLinkInjector.class.getClassLoader()));
	}

	private HateoasLinkInjector reflectionBasedDelegate = new ReflectionBasedHateoasLinkInjector();

	@Override
	public Object injectLinks(Object entity, Collection<HateoasLink> links,
			final HateoasVerbosity verbosity) {

        if(entity == null){
            return null;
        }

		if (Collection.class.isAssignableFrom(entity.getClass())) {
			return reflectionBasedDelegate
					.injectLinks(entity, links, verbosity);
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
				links, new Function<HateoasLink, Map<String, Object>>() {
					@Override
					public Map<String, Object> apply(HateoasLink link) {
						return link.toMap(verbosity);
					}
				}));

		return newInstance;
	}

}
