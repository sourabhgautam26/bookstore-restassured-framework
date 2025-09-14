package com.bookstore.api.factory;

import com.bookstore.api.payloads.BookPayload;
import com.bookstore.api.payloads.UserPayload;

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

    public static BookPayload getBookWithoutName() {
        return new BookPayload(
                102,
                "",
                "Unknown Author",
                2023,
                "Book payload with missing name"
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

    public static BookPayload getBookWithEmptySummary() {
        return new BookPayload(
                104,
                "Some Title",
                "Some Author",
                2000,
                ""
        );
    }

    // ----------------- User Payloads -----------------
    public static UserPayload getValidUser() {
        String uniqueEmail = "user_" + UUID.randomUUID().toString() + "@example.com";
        int randomId=new Random().nextInt(100000);
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
                "user@example.com",
                ""
        );
    }

    public static UserPayload getUserWithEmptyEmail() {
        return new UserPayload(
                "104",
                "",
                "Password@123"
        );
    }
}
