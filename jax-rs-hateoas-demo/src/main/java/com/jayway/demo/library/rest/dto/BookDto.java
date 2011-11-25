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

import com.jayway.demo.library.domain.Book;

public class BookDto {
	private Integer id;
	private String author;
	private String title;
	private boolean borrowed;
	private boolean reserved;

	public BookDto() {

	}

	public BookDto(Integer id, String author, String title, boolean borrowed,
			boolean reserved) {
		this.id = id;
		this.author = author;
		this.title = title;
		this.borrowed = borrowed;
		this.reserved = reserved;
	}

	public static BookDto fromBean(Book book) {
		return new BookDto(book.getId(), book.getAuthor(), book.getTitle(),
				book.isBorrowed(), book.isReserved());
	}

	public Integer getId() {
		return id;
	}

	public String getAuthor() {
		return author;
	}

	public String getTitle() {
		return title;
	}

	public boolean isBorrowed() {
		return borrowed;
	}

	public boolean isReserved() {
		return reserved;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setBorrowed(boolean borrowed) {
		this.borrowed = borrowed;
	}

	public void setReserved(boolean reserved) {
		this.reserved = reserved;
	}

}
