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

package com.jayway.demo.library.rest.dto;

import java.util.Collection;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.jayway.demo.library.domain.Customer;

public class CustomerDto {

	private Integer id;
	private String name;

	public CustomerDto() {

	}

	public CustomerDto(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static CustomerDto fromBean(Customer customer) {
		return new CustomerDto(customer.getId(), customer.getName());
	}

	public static Collection<CustomerDto> fromBeanCollection(
			Collection<Customer> customers) {
		return Collections2.transform(customers,
				new Function<Customer, CustomerDto>() {
					@Override
					public CustomerDto apply(Customer customer) {
						return fromBean(customer);
					}
				});
	}
}
