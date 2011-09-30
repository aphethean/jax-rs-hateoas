package com.jayway.demo.library.domain.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.jayway.demo.library.domain.Customer;
import com.jayway.demo.library.domain.CustomerRepository;

public class CustomerRepositoryInMemory implements CustomerRepository {
	private final Map<Integer, Customer> allCustomers = new HashMap<Integer, Customer>();

	private final AtomicInteger nextId = new AtomicInteger();

	public void init() {
		newCustomer("Mattias");
		newCustomer("Kalle");
	}

	@Override
	public Customer newCustomer(String name) {
		int id = nextId.getAndIncrement();
		Customer customer = new Customer(id, name);
		allCustomers.put(id, customer);
		return customer;
	}

	@Override
	public Collection<Customer> getAllCustomers() {
		return allCustomers.values();
	}

	@Override
	public Customer getById(Integer id) {
		return allCustomers.get(id);
	}
}
