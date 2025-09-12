package com.bookstore.api.payloads;

public class UserPayload {
    private String id;
    private String email;
    private String password;

    // ✅ No-args constructor
    public UserPayload() {}

    // ✅ All-args constructor
    public UserPayload(String id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    // ---------- Getters & Setters ----------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
