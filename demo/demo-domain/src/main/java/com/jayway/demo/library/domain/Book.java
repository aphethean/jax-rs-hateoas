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

package com.jayway.demo.library.domain;

public class Book {
	private String author;
	private String title;
	private Customer borrowedBy;
	private Customer reservedBy;
	private final Integer id;

	public Book(Integer id, String author, String title) {
		this.id = id;
		this.author = author;
		this.title = title;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setTitle(String title) {
		this.title = title;
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
		return borrowedBy != null;
	}

	public boolean isReserved() {
		return reservedBy != null;
	}

	public Customer getBorrowedBy() {
		return borrowedBy;
	}

	public void setBorrowedBy(Customer borrowedBy) {
		this.borrowedBy = borrowedBy;
	}

	public Customer getReservedBy() {
		return reservedBy;
	}

	public void setReservedBy(Customer reservedBy) {
		this.reservedBy = reservedBy;
	}

	public void returned() {
		setBorrowedBy(null);
	}
}
