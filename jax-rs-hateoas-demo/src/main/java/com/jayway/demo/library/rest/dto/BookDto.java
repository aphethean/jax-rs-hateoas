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
