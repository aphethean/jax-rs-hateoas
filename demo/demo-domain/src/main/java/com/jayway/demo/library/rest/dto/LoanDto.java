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

package com.jayway.demo.library.rest.dto;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.jayway.demo.library.domain.Book;

import java.util.Collection;

public class LoanDto {
    private Integer customerId;
    private Integer bookId;

    public LoanDto() {

    }

    public LoanDto(Integer customerId, Integer bookId) {
        this.customerId = customerId;
        this.bookId = bookId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer userId) {
        this.customerId = userId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public static LoanDto fromBean(Book book) {
        return new LoanDto(book.getBorrowedBy().getId(), book.getId());
    }

    public static Collection<LoanDto> fromBeanCollection(
            Collection<Book> books) {
        return Collections2.transform(books, new Function<Book, LoanDto>() {
            @Override
            public LoanDto apply(Book book) {
                return fromBean(book);
            }
        });
    }

}
