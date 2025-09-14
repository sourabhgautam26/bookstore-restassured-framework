package com.bookstore.api.tests;

import com.bookstore.api.factory.DataFactory;
import com.bookstore.api.payloads.BookPayload;
import com.bookstore.api.utils.Endpoints;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.containsString;

public class BookTests extends BaseTest {

    // ---------------- Helper Methods ----------------
    private Response createBook(BookPayload book) {
        Response resp = executeRequest("POST", Endpoints.ADD_NEW_BOOK, book, token);
        resp.then().statusCode(200); // Ensure status code asserted
        return resp;
    }

    private Response getBookById(int bookId) {
        Response resp = executeRequest("GET", Endpoints.getBookById(bookId), null, token);
        resp.then().statusCode(200);
        return resp;
    }

    private Response updateBook(int bookId, BookPayload book) {
        Response resp = executeRequest("PUT", Endpoints.updateBook(bookId), book, token);
        resp.then().statusCode(200);
        return resp;
    }

    private Response deleteBook(int bookId) {
        Response resp = executeRequest("DELETE", Endpoints.deleteBook(bookId), null, token);
        resp.then().statusCode(200);
        return resp;
    }

    // ---------------- Positive Tests ----------------
    @Test(priority = 1, description = "Verify that a new book can be created successfully",dependsOnMethods = {"com.bookstore.api.tests.HealthCheckTest.validateHealthCheckEndpoint"})
    public void createBookPositive() {
        BookPayload book = DataFactory.getValidBook();
        Response resp = createBook(book);

        Assert.assertEquals(resp.path("name"), book.getName());
        Assert.assertEquals(resp.path("author"), book.getAuthor());
        Assert.assertEquals((int) resp.path("published_year"), book.getPublished_year());
        Assert.assertEquals(resp.path("book_summary"), book.getBook_summary());
    }

    @Test(priority = 2, description = "Verify fetching a book by its ID returns correct details", dependsOnMethods = {"createBookPositive"})
    public void getBookByIdPositive() {
        BookPayload book = DataFactory.getValidBook();
        int bookId = createBook(book).path("id");

        Response resp = getBookById(bookId);
        Assert.assertEquals(resp.path("name"), book.getName());
        Assert.assertEquals(resp.path("author"), book.getAuthor());
    }

    @Test(priority = 3, description = "Verify that all books can be fetched and include recently created books", dependsOnMethods = {"createBookPositive"})
    public void getAllBooksPositive() {
        BookPayload book1 = DataFactory.getValidBook();
        BookPayload book2 = DataFactory.getBookWithFutureYear();

        createBook(book1);
        createBook(book2);

        Response resp = executeRequest("GET", Endpoints.GET_ALL_BOOKS, null, token);
        resp.then().statusCode(200);

        List<String> names = resp.jsonPath().getList("name");
        Assert.assertTrue(names.contains(book1.getName()));
        Assert.assertTrue(names.contains(book2.getName()));
    }

    @Test(priority = 4, description = "Verify that an existing book can be updated successfully", dependsOnMethods = {"createBookPositive"})
    public void updateBookPositive() {
        BookPayload book = DataFactory.getValidBook();
        int bookId = createBook(book).path("id");

        BookPayload updatedBook = new BookPayload(
                bookId,
                "Updated Title",
                "Updated Author",
                2025,
                "Updated summary"
        );

        Response resp = updateBook(bookId, updatedBook);
        Assert.assertEquals(resp.path("name"), updatedBook.getName());
        Assert.assertEquals(resp.path("author"), updatedBook.getAuthor());
        Assert.assertEquals((int) resp.path("published_year"), updatedBook.getPublished_year());
        Assert.assertEquals(resp.path("book_summary"), updatedBook.getBook_summary());
    }

    @Test(priority = 5, description = "Verify that a book can be deleted and cannot be fetched afterwards", dependsOnMethods = {"createBookPositive"})
    public void deleteBookPositive() {
        BookPayload book = DataFactory.getValidBook();
        int bookId = createBook(book).path("id");

        deleteBook(bookId);

        Response checkResp = executeRequest("GET", Endpoints.getBookById(bookId), null, token);
        checkResp.then().statusCode(404)
                .body("detail", containsString("Book not found"));
    }

    // ---------------- Negative / Edge Tests ----------------
    @Test(priority = 6, description = "Verify updating a non-existent book returns 404", dependsOnMethods = {"createBookPositive"})
    public void updateBookNonExistent() {
        BookPayload book = DataFactory.getValidBook();
        Response resp = executeRequest("PUT", Endpoints.updateBook(99999), book, token);
        resp.then().statusCode(404)
                .body("detail", containsString("Book not found"));
    }

    @Test(priority = 7, description = "Verify deleting a non-existent book returns 404", dependsOnMethods = {"createBookPositive"})
    public void deleteBookNonExistent() {
        Response resp = executeRequest("DELETE", Endpoints.deleteBook(99999), null, token);
        resp.then().statusCode(404)
                .body("detail", containsString("Book not found"));
    }

    @Test(priority = 8, description = "Verify fetching a non-existent book returns 404", dependsOnMethods = {"createBookPositive"})
    public void getBookNonExistent() {
        Response resp = executeRequest("GET", Endpoints.getBookById(99999), null, token);
        resp.then().statusCode(404)
                .body("detail", containsString("Book not found"));
    }

    // ---------------- Invalid Token Tests ----------------
    @Test(priority = 9, description = "Verify creating a book with invalid token returns 403", dependsOnMethods = {"createBookPositive"})
    public void createBookWithInvalidToken() {
        BookPayload book = DataFactory.getValidBook();
        Response resp = executeRequest("POST", Endpoints.ADD_NEW_BOOK, book, "INVALID_TOKEN");
        resp.then().statusCode(403)
                .body("detail", containsString("Invalid token or expired token"));
    }

    @Test(priority = 10, description = "Verify fetching a book with invalid token returns 403", dependsOnMethods = {"createBookPositive"})
    public void getBookWithInvalidToken() {
        BookPayload book = DataFactory.getValidBook();
        int bookId = createBook(book).path("id");

        Response resp = executeRequest("GET", Endpoints.getBookById(bookId), null, "INVALID_TOKEN");
        resp.then().statusCode(403)
                .body("detail", containsString("Invalid token or expired token"));
    }

    @Test(priority = 11, description = "Verify updating a book with invalid token returns 403", dependsOnMethods = {"createBookPositive"})
    public void updateBookWithInvalidToken() {
        BookPayload book = DataFactory.getValidBook();
        int bookId = createBook(book).path("id");

        BookPayload updatedBook = new BookPayload(
                bookId,
                "Updated Title",
                "Updated Author",
                2025,
                "Updated summary"
        );

        Response resp = executeRequest("PUT", Endpoints.updateBook(bookId), updatedBook, "INVALID_TOKEN");
        resp.then().statusCode(403)
                .body("detail", containsString("Invalid token or expired token"));
    }

    @Test(priority = 12, description = "Verify deleting a book with invalid token returns 403", dependsOnMethods = {"createBookPositive"})
    public void deleteBookWithInvalidToken() {
        BookPayload book = DataFactory.getValidBook();
        int bookId = createBook(book).path("id");

        Response resp = executeRequest("DELETE", Endpoints.deleteBook(bookId), null, "INVALID_TOKEN");
        resp.then().statusCode(403)
                .body("detail", containsString("Invalid token or expired token"));
    }

    // ---------------- Payload Validation / Negative Tests ----------------
    @Test(priority = 13, description = "POST /books - Missing 'name' field")
    public void createBookMissingName() {

        Response resp = executeRequest("POST", Endpoints.ADD_NEW_BOOK, DataFactory.getBookPayloadMissingName(), token);
        resp.then().statusCode(400);
    }

    @Test(priority = 14, description = "POST /books - Missing 'author' field")
    public void createBookMissingAuthor() {

        Response resp = executeRequest("POST", Endpoints.ADD_NEW_BOOK, DataFactory.getBookPayloadMissingAuthor(), token);
        resp.then().statusCode(400);
    }

    @Test(priority = 15, description = "POST /books - Missing 'published_year' field")
    public void createBookMissingPublishedYear() {

        Response resp = executeRequest("POST", Endpoints.ADD_NEW_BOOK, DataFactory.getBookPayloadMissingPublishedYear(), token);
        resp.then().statusCode(400);
    }

    @Test(priority = 16, description = "POST /books - Missing 'book_summary' field")
    public void createBookMissingSummary() {

        Response resp = executeRequest("POST", Endpoints.ADD_NEW_BOOK, DataFactory.getBookPayloadMissingSummary(), token);
        resp.then().statusCode(400);
    }

    // ---------------- Empty Fields ----------------
    @Test(priority = 17, description = "POST /books - Empty 'name' field")
    public void createBookEmptyName() {

        Response resp = executeRequest("POST", Endpoints.ADD_NEW_BOOK, DataFactory.getBookPayloadEmptyName(), token);
        resp.then().statusCode(400);
    }

    @Test(priority = 18, description = "POST /books - Empty 'author' field")
    public void createBookEmptyAuthor() {

        Response resp = executeRequest("POST", Endpoints.ADD_NEW_BOOK, DataFactory.getBookPayloadEmptyAuthor(), token);
        resp.then().statusCode(400);
    }

    @Test(priority = 19, description = "POST /books - Empty 'book_summary' field")
    public void createBookEmptySummary() {

        Response resp = executeRequest("POST", Endpoints.ADD_NEW_BOOK, DataFactory.getBookPayloadEmptySummary(), token);
        resp.then().statusCode(400);
    }

    // ---------------- Wrong Type / Invalid ----------------
    @Test(priority = 20, description = "POST /books - Invalid type for 'published_year'")
    public void createBookWrongPublishedYearType() {
        Response resp = executeRequest("POST", Endpoints.ADD_NEW_BOOK, DataFactory.getBookPayloadWrongPublishedYear(), token);
        resp.then().statusCode(400);
    }

    @Test(priority = 21, description = "POST /books - Negative 'published_year'")
    public void createBookNegativePublishedYear() {
        Response resp = executeRequest("POST", Endpoints.ADD_NEW_BOOK, DataFactory.getBookPayloadNegativePublishedYear(), token);
        resp.then().statusCode(400);
    }


}



