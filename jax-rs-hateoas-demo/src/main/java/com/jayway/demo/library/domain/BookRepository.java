package com.jayway.demo.library.domain;

import java.util.Collection;
import java.util.List;

public interface BookRepository {
	List<Book> getAllBooks();

	Book getBookById(Integer id);

	Book newBook(String author, String title);

    Collection<Book> getLoansForCustomer(Integer customerId);

    Collection<Book> getLoans();
}
