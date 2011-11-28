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
