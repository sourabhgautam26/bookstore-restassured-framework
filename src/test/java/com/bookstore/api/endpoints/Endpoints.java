package com.bookstore.api.endpoints;

public class Endpoints {

    public static final String BASE_URL = "http://localhost:8080/api";

    // Auth
    public static final String SIGNUP = BASE_URL + "/signup";
    public static final String LOGIN  = BASE_URL + "/login";

    // Books
    public static final String CREATE_BOOK = BASE_URL + "/books";
    public static final String GET_BOOK    = BASE_URL + "/books/{id}";
    public static final String UPDATE_BOOK = BASE_URL + "/books/{id}";
    public static final String DELETE_BOOK = BASE_URL + "/books/{id}";
    public static final String GET_ALL_BOOKS = BASE_URL + "/books";
}
