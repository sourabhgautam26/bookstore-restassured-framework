package com.bookstore.api.factory;

import com.bookstore.api.payloads.UserPayload;

import java.util.UUID;

public class UserPayloadFactory {

    /**
     * Generate a valid user with random email each time.
     */
    public static UserPayload validUser() {
        String uniqueEmail = "user_" + UUID.randomUUID().toString().substring(0, 6) + "@test.com";
        return new UserPayload(null, uniqueEmail, "Password123!");
    }

    /**
     * Generate a user with missing email (negative test).
     */
    public static UserPayload userWithoutEmail() {
        return new UserPayload(null, null, "Password123!");
    }

    /**
     * Generate a user with missing password (negative test).
     */
    public static UserPayload userWithoutPassword() {
        String uniqueEmail = "user_" + UUID.randomUUID().toString().substring(0, 6) + "@test.com";
        return new UserPayload(null, uniqueEmail, null);
    }

    /**
     * Generate a user with invalid email format.
     */
    public static UserPayload userWithInvalidEmail() {
        return new UserPayload(null, "invalid-email-format", "Password123!");
    }

    /**
     * Generate a user with weak password.
     */
    public static UserPayload userWithWeakPassword() {
        String uniqueEmail = "user_" + UUID.randomUUID().toString().substring(0, 6) + "@test.com";
        return new UserPayload(null, uniqueEmail, "123");
    }

    /**
     * Generate a user with static known credentials (for repeated login tests).
     */
    public static UserPayload staticUser() {
        return new UserPayload(null, "staticuser@test.com", "Password123!");
    }
}
