package com.jayway.jaxrs.hateoas;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.parsing.Parser;
import com.jayway.restassured.path.json.JsonPath;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.InputStream;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.registerParser;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

/**
 * Created by IntelliJ IDEA.
 * User: kallestenflo
 * Date: 2/27/12
 * Time: 6:19 PM
 */
public class HateoasIntegrationTestNew extends JerseyTest {


    @Override
    protected AppDescriptor configure() {
        return new WebAppDescriptor
                        .Builder("com.jayway.demo.library.rest.resources.hateoas")
                        .contextPath("/")
                        .servletPath("api")
                        .initParam("javax.ws.rs.Application", "com.jayway.demo.library.rest.application.hateoas.LibraryApplication")
                        .initParam("com.sun.jersey.api.json.POJOMappingFeature", "true")
                        .build();
    }

    private String customersHref;
    private String loansHref;
    private String booksHref;

    @BeforeClass
    public static void registerParsers() {
        RestAssured.port = 9998;
        RestAssured.registerParser("application/vnd.demo.library.root+json", Parser.JSON);
        RestAssured.registerParser("application/vnd.demo.library.list.book+json", Parser.JSON);
        RestAssured.registerParser("application/vnd.demo.library.book+json", Parser.JSON);
        RestAssured.registerParser("application/vnd.demo.library.list.customer+json", Parser.JSON);
        RestAssured.registerParser("application/vnd.demo.library.customer+json", Parser.JSON);
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
                body("links[0].rel", equalTo("self")).
                body("links[1].rel", equalTo("loans")).
                body("links[1].method", equalTo("POST")).
                statusCode(200).
                when().get(bookHref);

    }
}
