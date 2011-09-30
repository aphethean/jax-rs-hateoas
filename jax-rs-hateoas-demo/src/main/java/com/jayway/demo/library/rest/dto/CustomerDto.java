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
