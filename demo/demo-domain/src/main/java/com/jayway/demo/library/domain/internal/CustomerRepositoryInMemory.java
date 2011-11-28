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

package com.jayway.demo.library.domain.internal;

import com.jayway.demo.library.domain.Customer;
import com.jayway.demo.library.domain.CustomerRepository;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomerRepositoryInMemory implements CustomerRepository {
	private final Map<Integer, Customer> allCustomers = new HashMap<Integer, Customer>();

	private final AtomicInteger nextId = new AtomicInteger();

    @PostConstruct
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
