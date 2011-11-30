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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marking a method with @Linkable enables it to be linked from elsewhere in the application. Note that the id
 * <b>must</b> be unique in an application.
 *
 * @author Mattias Hellborg Arthursson
 * @author Kalle Stenflo
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Linkable {
	public final static class NoTemplate {

	}

    /**
     * Identifier of this linkable method.
     */
	String value();

    /**
     * The class to use for generating a template in links.
     */
	Class<?> templateClass() default NoTemplate.class;

    /**
     * Label of this link.
     */
	String label() default "";

    /**
     * Description of this link.
     */
	String description() default "";
}
