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

package com.jayway.jaxrs.hateoas;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.parsing.Parser;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import groovyx.net.http.ParserRegistry;
import junit.framework.Assert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.InputStream;
import java.util.Map;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.registerParser;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.*;

public class HateoasIntegrationTest {

    private String customersHref;
    private String loansHref;
    private String booksHref;

    @BeforeClass
    public static void registerParsers() {
        registerParser("application/vnd.demo.library.root+json", Parser.JSON);
        registerParser("application/vnd.demo.library.list.book+json", Parser.JSON);
        registerParser("application/vnd.demo.library.book+json", Parser.JSON);
        registerParser("application/vnd.demo.library.list.customer+json", Parser.JSON);
        registerParser("application/vnd.demo.library.customer+json", Parser.JSON);
    }

    @Before
    public void prepareRootLinks() {
        InputStream rootResponse = expect().
                statusCode(200).
                body("links.rel", hasItems("customers", "loans", "books")).
                body("links[0].method", equalTo("GET")).
                body("links[1].method", equalTo("GET")).
                body("links[2].method", equalTo("GET")).
                when().get("/api").asInputStream();

        JsonPath jsonPath = JsonPath.from(rootResponse);
        customersHref = jsonPath.<String, String>getMap("links.find {link -> link.rel == 'customers'}").get("href");
        loansHref = jsonPath.<String, String>getMap("links.find {link -> link.rel == 'loans'}").get("href");
        booksHref = jsonPath.<String, String>getMap("links.find {link -> link.rel == 'books'}").get("href");
    }

    @Test
    public void verifyGetCustomer() {
        InputStream customersResponse = expect().
                body("rows.size()", is(2)).
                body("rows[0].name", equalTo("Mattias")).
                body("rows[0].links.size()", is(1)).
                body("rows[1].name", equalTo("Kalle")).
                body("rows[1].links.size()", is(1)).
                statusCode(200).
                when().get(customersHref).asInputStream();

        JsonPath jsonPath = JsonPath.from(customersResponse);
        String customerHref = jsonPath.getString("rows[0].links[0].href");

        expect().
                body("name", equalTo("Mattias")).
                body("id", is(0)).
                body("links.size()", is(1)).
                body("links[0].rel", equalTo("loans")).
                statusCode(200).
                when().get(customerHref);
    }

    @Test
    public void verifyGetBook() {
        InputStream booksResponse = expect().
                body("rows.size()", is(4)).
                body("rows[0].title", equalTo("Lord of the Rings")).
                body("rows[0].author", equalTo("J.R.R. Tolkien")).
                body("rows[0].links.size()", is(1)).
                statusCode(200).
                when().get(booksHref).asInputStream();

        JsonPath jsonPath = JsonPath.from(booksResponse);
        String bookHref = jsonPath.getString("rows[0].links[0].href");

        expect().
                body("title", equalTo("Lord of the Rings")).
                body("author", equalTo("J.R.R. Tolkien")).
                body("borrowed", is(false)).
                body("id", is(0)).
                body("links.size()", is(2)).
                body("links[0].rel", equalTo("update")).
                body("links[1].rel", equalTo("loans")).
                body("links[1].method", equalTo("POST")).
                statusCode(200).
                when().get(bookHref);
    }
}
