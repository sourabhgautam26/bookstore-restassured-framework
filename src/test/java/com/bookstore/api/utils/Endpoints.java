package com.bookstore.api.utils;

public class Endpoints {

    // -------------------- User APIs --------------------
    public static final String SIGN_UP = "/signup";
    public static final String LOGIN = "/login";

    // -------------------- Book APIs --------------------
    public static final String ADD_NEW_BOOK = "books/";
    public static final String GET_ALL_BOOKS = "books/";

    private static final String BOOK_BY_ID = "books/%s";

    public static String getBookById(int id) {
        return String.format(BOOK_BY_ID, id);
    }

    public static String updateBook(int id) {
        return String.format(BOOK_BY_ID, id);
    }

    public static String deleteBook(int id) {
        return String.format(BOOK_BY_ID, id);
    }

    // -------------------- Health Check --------------------
    public static final String HEALTH_CHECK = "health";

    private Endpoints() {}
}
