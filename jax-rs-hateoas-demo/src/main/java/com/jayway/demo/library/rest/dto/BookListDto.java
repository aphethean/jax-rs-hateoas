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
