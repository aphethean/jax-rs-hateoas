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

import java.util.Collection;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.jayway.demo.library.domain.Book;

public class BookListDto {
	private String author;
	private String title;
	private Integer id;

	public BookListDto() {

	}

	public BookListDto(Integer id, String author, String title) {
		this.id = id;
		this.author = author;
		this.title = title;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public static BookListDto fromBean(Book book) {
		return new BookListDto(book.getId(), book.getAuthor(), book.getTitle());
	}

	public static Collection<BookListDto> fromBeanCollection(
			Collection<Book> books) {
		return Collections2.transform(books, new Function<Book, BookListDto>() {
			@Override
			public BookListDto apply(Book book) {
				return fromBean(book);
			}
		});
	}

}
