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
