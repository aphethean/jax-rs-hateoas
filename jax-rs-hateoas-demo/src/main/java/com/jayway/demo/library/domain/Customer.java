package com.jayway.demo.library.domain;

public class Customer {
	private final String name;
	private final Integer id;

	public Customer(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
