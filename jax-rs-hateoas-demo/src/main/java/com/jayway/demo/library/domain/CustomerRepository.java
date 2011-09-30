package com.jayway.demo.library.domain;

import java.util.Collection;

public interface CustomerRepository {
	Customer getById(Integer id);

	Collection<Customer> getAllCustomers();

	Customer newCustomer(String name);
}
