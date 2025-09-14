package com.bookstore.api.utils;

public class ApiEndpoints {

    public static final String SIGNUP = "/signup";
    public static final String LOGIN = "/login";
    public static final String BOOKS = "/books/";

    public static String bookById(int id) {
        return BOOKS + "/" + id;
    }
}
