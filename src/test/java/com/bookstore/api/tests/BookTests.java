package com.bookstore.api.tests;

import com.bookstore.api.factory.DataFactory;
import com.bookstore.api.payloads.BookPayload;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class BookTests extends BaseTest {

    // ---------------- Helper Methods ----------------
    private int createBook(BookPayload book) {
        return given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + token)
                .body(book)
                .post("/books/")
                .then()
                .statusCode(200)
                .extract()
                .path("id");
    }

    private Response getBookById(int bookId) {
        return given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + token)
                .get("/books/" + bookId)
                .then()
                .extract()
                .response();
    }

    // ---------------- Positive Tests ----------------
    @Test(priority = 1)
    public void createBookPositive() {
        BookPayload book = DataFactory.getValidBook();
        int bookId = createBook(book);

        Response resp = getBookById(bookId);
        Assert.assertEquals(resp.path("name"), book.getName());
        Assert.assertEquals(resp.path("author"), book.getAuthor());
        Assert.assertEquals((int) resp.path("published_year"), book.getPublished_year());
        Assert.assertEquals(resp.path("book_summary"), book.getBook_summary());
    }

    @Test(priority = 2)
    public void getBookByIdPositive() {
        BookPayload book = DataFactory.getValidBook();
        int bookId = createBook(book);

        Response resp = getBookById(bookId);
        Assert.assertEquals(resp.path("name"), book.getName());
        Assert.assertEquals(resp.path("author"), book.getAuthor());
    }

    @Test(priority = 3)
    public void getAllBooksPositive() {
        BookPayload book1 = DataFactory.getValidBook();
        BookPayload book2 = DataFactory.getBookWithFutureYear();

        createBook(book1);
        createBook(book2);

        Response resp = given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + token)
                .get("/books")
                .then()
                .statusCode(200)
                .extract()
                .response();

        List<String> names = resp.jsonPath().getList("name");
        Assert.assertTrue(names.contains(book1.getName()));
        Assert.assertTrue(names.contains(book2.getName()));
    }

    @Test(priority = 4)
    public void updateBookPositive() {
        BookPayload book = DataFactory.getValidBook();
        int bookId = createBook(book);

        // Update only specific fields
        BookPayload updatedBook = new BookPayload(
                bookId,
                "Updated Title",
                "Updated Author",
                2025,
                "Updated summary"
        );

        Response resp = given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + token)
                .body(updatedBook)
                .put("/books/" + bookId)
                .then()
                .statusCode(200)
                .extract()
                .response();

        Assert.assertEquals(resp.path("name"), updatedBook.getName());
        Assert.assertEquals(resp.path("author"), updatedBook.getAuthor());
        Assert.assertEquals((int) resp.path("published_year"), updatedBook.getPublished_year());
        Assert.assertEquals(resp.path("book_summary"), updatedBook.getBook_summary());
    }

    @Test(priority = 5)
    public void deleteBookPositive() {
        BookPayload book = DataFactory.getValidBook();
        int bookId = createBook(book);

        given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + token)
                .delete("/books/" + bookId)
                .then()
                .statusCode(200);

        // Verify deletion
        given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + token)
                .get("/books/" + bookId)
                .then()
                .statusCode(404)
                .body("detail", containsString("Book not found"));
    }

    // ---------------- Negative / Edge Tests ----------------
    @Test(priority = 6)
    public void updateBookNonExistent() {
        BookPayload book = DataFactory.getValidBook();

        given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + token)
                .body(book)
                .put("/books/999999")
                .then()
                .statusCode(404)
                .body("detail", containsString("Book not found"));
    }

    @Test(priority = 7)
    public void deleteBookNonExistent() {
        given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + token)
                .delete("/books/999999")
                .then()
                .statusCode(404)
                .body("detail", containsString("Book not found"));
    }

    @Test(priority = 8)
    public void getBookNonExistent() {
        given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + token)
                .get("/books/999999")
                .then()
                .statusCode(404)
                .body("detail", containsString("Book not found"));
    }

    @Test(priority = 9)
    public void createBookWithInvalidToken() {
        BookPayload book = DataFactory.getValidBook();

        given()
                .spec(requestSpec)
                .header("Authorization", "Bearer INVALID_TOKEN")
                .body(book)
                .post("/books/")
                .then()
                .statusCode(403)
                .body("detail", containsString("Invalid token or expired token"));
    }

    @Test(priority = 10)
    public void getBookWithInvalidToken() {
        BookPayload book = DataFactory.getValidBook();
        int bookId = createBook(book);

        given()
                .spec(requestSpec)
                .header("Authorization", "Bearer INVALID_TOKEN")
                .get("/books/" + bookId)
                .then()
                .statusCode(403)
                .body("detail", containsString("Invalid token or expired token"));
    }

    @Test(priority = 11)
    public void updateBookWithInvalidToken() {
        BookPayload book = DataFactory.getValidBook();
        int bookId = createBook(book);

        BookPayload updatedBook = new BookPayload(
                bookId,
                "Updated Title",
                "Updated Author",
                2025,
                "Updated summary"
        );

        given()
                .spec(requestSpec)
                .header("Authorization", "Bearer INVALID_TOKEN")
                .body(updatedBook)
                .put("/books/" + bookId)
                .then()
                .statusCode(403)
                .body("detail", containsString("Invalid token or expired token"));
    }

    @Test(priority = 12)
    public void deleteBookWithInvalidToken() {
        BookPayload book = DataFactory.getValidBook();
        int bookId = createBook(book);

        given()
                .spec(requestSpec)
                .header("Authorization", "Bearer INVALID_TOKEN")
                .delete("/books/" + bookId)
                .then()
                .statusCode(403)
                .body("detail", containsString("Invalid token or expired token"));
    }
}
