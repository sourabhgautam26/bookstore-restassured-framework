package com.bookstore.api.builder;

import com.bookstore.api.payloads.UserPayload;

import java.util.UUID;

/**
 * Fluent Builder for UserPayload
 */
public class UserPayloadBuilder {
    private String id = UUID.randomUUID().toString();
    private String email = "user_" + System.currentTimeMillis() + "@test.com";
    private String password = "Password123";

    public UserPayloadBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public UserPayloadBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserPayloadBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserPayload build() {
        return new UserPayload(id, email, password);
    }
}
