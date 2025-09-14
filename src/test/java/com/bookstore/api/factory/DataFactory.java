package com.bookstore.api.factory;

import com.bookstore.api.payloads.BookPayload;
import com.bookstore.api.payloads.UserPayload;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class DataFactory {

    // ----------------- Book Payloads -----------------
    public static BookPayload getValidBook() {
        return new BookPayload(
                new Random().nextInt(100000),
                "The Alchemist",
                "Paulo Coelho",
                1988,
                "A shepherd boy’s spiritual journey."
        );
    }

    public static BookPayload getBookWithFutureYear() {
        return new BookPayload(
                new Random().nextInt(100000),
                "Future Book",
                "Author X",
                2050,
                "Testing invalid published year"
        );
    }

    // Missing 'name'
    public static Map<String, Object> getBookPayloadMissingName() {
        Map<String, Object> map = new HashMap<>();
        map.put("author", "Author Name");
        map.put("published_year", 2023);
        map.put("book_summary", "Some summary");
        return map;
    }

    // Missing 'author'
    public static Map<String, Object> getBookPayloadMissingAuthor() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Book Title");
        map.put("published_year", 2023);
        map.put("book_summary", "Some summary");
        return map;
    }

    // Missing 'published_year'
    public static Map<String, Object> getBookPayloadMissingPublishedYear() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Book Title");
        map.put("author", "Author Name");
        map.put("book_summary", "Some summary");
        return map;
    }

    // Missing 'book_summary'
    public static Map<String, Object> getBookPayloadMissingSummary() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Book Title");
        map.put("author", "Author Name");
        map.put("published_year", 2023);
        return map;
    }

    // Empty 'name'
    public static Map<String, Object> getBookPayloadEmptyName() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "");
        map.put("author", "Author Name");
        map.put("published_year", 2023);
        map.put("book_summary", "Some summary");
        return map;
    }

    // Empty 'author'
    public static Map<String, Object> getBookPayloadEmptyAuthor() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Book Title");
        map.put("author", "");
        map.put("published_year", 2023);
        map.put("book_summary", "Some summary");
        return map;
    }

    // Empty 'book_summary'
    public static Map<String, Object> getBookPayloadEmptySummary() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Book Title");
        map.put("author", "Author Name");
        map.put("published_year", 2023);
        map.put("book_summary", "");
        return map;
    }

    // published_year as string
    public static Map<String, Object> getBookPayloadWrongPublishedYear() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Book Title");
        map.put("author", "Author Name");
        map.put("published_year", "abcd"); // ❌ wrong type
        map.put("book_summary", "Some summary");
        return map;
    }

    // published_year as negative number
    public static Map<String, Object> getBookPayloadNegativePublishedYear() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Book Title");
        map.put("author", "Author Name");
        map.put("published_year", -2023);
        map.put("book_summary", "Some summary");
        return map;
    }


    // ----------------- User Payloads -----------------
    public static UserPayload getValidUser() {
        String uniqueEmail = "user_" + UUID.randomUUID().toString() + "@example.com";
        int randomId = new Random().nextInt(100000);
        String uniqueId = String.valueOf(randomId);
        return new UserPayload(
                uniqueId,
                uniqueEmail,
                "Password123"
        );
    }

    public static UserPayload getInvalidUser() {
        return new UserPayload(
                "102",
                "invalidEmail",
                "123"
        );
    }

    public static UserPayload getUserWithEmptyPassword() {
        return new UserPayload(
                "103",
                "user_" + UUID.randomUUID().toString() + "@example.com",
                ""
        );
    }
    public static UserPayload getUserWithEmptyId() {
        return new UserPayload(
                "",
                "user_" + UUID.randomUUID().toString() + "@example.com",
                "password"
        );
    }

    public static UserPayload getUserWithEmptyEmail() {
        return new UserPayload(
                "104",
                "",
                "Password@123"
        );
    }
    public static Map<String, Object> getUserWithMissingEmail() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", "106");
        map.put("password", "Password@123");
        return map; // ❌ no "email" key → missing email
    }

    public static Map<String, Object> getUserWithMissingPassword() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", "107");
        map.put("email", "user_" + UUID.randomUUID().toString() + "@example.com");
        return map; // ❌ no "password"
    }

    public static Map<String, Object> getUserWithMissingId() {
        Map<String, Object> map = new HashMap<>();
        map.put("email", "user_" + UUID.randomUUID().toString() + "@example.com");
        map.put("password", "Password@123");
        return map; // ❌ no "id"
    }
}
