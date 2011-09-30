package com.jayway.demo.library.domain.factory;

import com.jayway.demo.library.domain.BookRepository;
import com.jayway.demo.library.domain.CustomerRepository;
import com.jayway.demo.library.domain.internal.BookRepositoryInMemory;
import com.jayway.demo.library.domain.internal.CustomerRepositoryInMemory;

public class RepositoryFactory {
	private final static BookRepository bookRepository;
	private final static CustomerRepository customerRepository;

	static {
		BookRepositoryInMemory br = new BookRepositoryInMemory();
		br.init();
		bookRepository = br;

		CustomerRepositoryInMemory cr = new CustomerRepositoryInMemory();
		cr.init();
		customerRepository = cr;
	}

	public static BookRepository getBookRepository() {
		return bookRepository;
	}

	public static CustomerRepository getCustomerRepository() {
		return customerRepository;
	}
}
