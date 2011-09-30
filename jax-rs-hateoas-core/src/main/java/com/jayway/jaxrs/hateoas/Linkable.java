package com.jayway.jaxrs.hateoas;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Linkable {
	public final static class NoTemplate {

	}

	String id();

	String rel();

	Class<?> templateClass() default NoTemplate.class;

	String label() default "";

	String description() default "";
}
