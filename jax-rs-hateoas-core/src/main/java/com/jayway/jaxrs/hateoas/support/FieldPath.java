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
import com.jayway.jaxrs.hateoas.HateoasInjectException;
import com.jayway.jaxrs.hateoas.HateoasLinkInjector;
import com.jayway.jaxrs.hateoas.HateoasVerbosity;
import com.jayway.jaxrs.hateoas.LinkProducer;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * @author Mattias Hellborg Arthursson
 */
public class FieldPath implements Iterable<String> {
    public final static FieldPath EMPTY_PATH = new FieldPath(Collections.<String>emptyList());

    private final Collection<String> path;

    private FieldPath(Collection<String> path) {
        this.path = path;

        for (String s : path) {
            if (StringUtils.isBlank(s)) {
                throw new IllegalArgumentException("Blank elements in field path are not allowed");
            }
        }
    }

    public static FieldPath parse(String path) {
        String[] pathElement = path.split("\\.");
        return new FieldPath(Arrays.asList(pathElement));
    }

    @Override
    public Iterator<String> iterator() {
        return path.iterator();
    }

    public Object injectLinks(Object target, HateoasLinkInjector<Object> injector, LinkProducer linkProducer,
                              HateoasVerbosity verbosity) {
        try {
            return injectLinks(iterator(), target, injector, linkProducer, verbosity);
        } catch (Exception e) {
            throw new HateoasInjectException(e);
        } 
    }

    @SuppressWarnings("unchecked")
    private Object injectLinks(Iterator<String> pathIterator, Object currentTarget,
                               final HateoasLinkInjector<Object> injector, final LinkProducer linkProducer,
                               final HateoasVerbosity verbosity) throws NoSuchFieldException, IllegalAccessException {
        if (!pathIterator.hasNext()) {
            if (Collection.class.isAssignableFrom(currentTarget.getClass())) {
                Collection<Object> targetAsCollection = (Collection<Object>) currentTarget;
                return Collections2.transform(targetAsCollection, new Function<Object, Object>() {
                    @Override
                    public Object apply(Object entry) {
                        return injector.injectLinks(entry, linkProducer, verbosity);
                    }
                });
            }

            return injector.injectLinks(currentTarget, linkProducer, verbosity);
        }

        String currentFieldName = pathIterator.next();
        Field currentField = ReflectionUtils.getField(currentTarget, currentFieldName);

        Object nextTarget = currentField.get(currentTarget);
        Object nextResult = injectLinks(pathIterator, nextTarget, injector, linkProducer, verbosity);
        currentField.set(currentTarget, nextResult);

        return currentTarget;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldPath fieldPath = (FieldPath) o;

        if (path != null ? !path.equals(fieldPath.path) : fieldPath.path != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return path != null ? path.hashCode() : 0;
    }
}
