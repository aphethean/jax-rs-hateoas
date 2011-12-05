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

import com.jayway.jaxrs.hateoas.CollectionWrapperStrategy;

import java.util.Collection;

/**
 * @author Mattias Hellborg Arthursson
 */
public class DefaultCollectionWrapperStrategy implements CollectionWrapperStrategy {
    @Override
    public Object wrapRootCollection(Collection<Object> rootCollection) {
        return new DefaultCollectionWrapper<Object>(rootCollection);
    }

    @Override
    public String rowsFieldName() {
        return DefaultCollectionWrapper.ROWS_FIELD_NAME;
    }
}
