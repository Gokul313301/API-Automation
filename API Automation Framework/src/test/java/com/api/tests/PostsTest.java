package com.api.tests;

import com.api.constants.Endpoints;
import com.api.models.Post;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Demonstrates the four common HTTP verbs against /posts.
 *
 * Read these top-to-bottom - they go from simplest (GET) to a full
 * round trip (serialize a POJO -> POST -> deserialize the response).
 */
public class PostsTest extends BaseTest {

    /* ----------------------------------------------------------------
       GET - read a single resource and assert on the JSON body.
       This uses Hamcrest matchers inside .body(...) - the classic
       REST Assured style.
       ---------------------------------------------------------------- */
    @Test(description = "GET /posts/1 returns the expected post")
    public void getSinglePost() {
        given()
                .spec(requestSpec)
                .pathParam("id", 1)
        .when()
                .get(Endpoints.POST_BY_ID)
        .then()
                .spec(responseSpec)
                .statusCode(200)
                .body("id", equalTo(1))
                .body("userId", equalTo(1))
                .body("title", not(emptyOrNullString()));
    }

    /* ----------------------------------------------------------------
       GET a collection - assert on list size and contents.
       ---------------------------------------------------------------- */
    @Test(description = "GET /posts returns 100 posts")
    public void getAllPosts() {
        given()
                .spec(requestSpec)
        .when()
                .get(Endpoints.POSTS)
        .then()
                .statusCode(200)
                .body("size()", equalTo(100))
                .body("[0].id", equalTo(1));
    }

    /* ----------------------------------------------------------------
       POST - build a POJO, send it as the JSON body, and read the
       response back into a POJO. This is the pattern you'll use most
       in a real framework.
       ---------------------------------------------------------------- */
    @Test(description = "POST /posts creates a resource and echoes it back")
    public void createPost() {
        Post newPost = new Post(1, "Learning REST Assured", "Step by step");

        Post created =
                given()
                        .spec(requestSpec)
                        .body(newPost)               // POJO -> JSON automatically
                .when()
                        .post(Endpoints.POSTS)
                .then()
                        .statusCode(201)             // 201 Created
                        .extract().as(Post.class);   // JSON -> POJO automatically

        // Now assert on the returned object using plain TestNG asserts.
        assertEquals(created.getTitle(), "Learning REST Assured");
        assertEquals(created.getUserId(), Integer.valueOf(1));
        assertTrue(created.getId() != null, "A new id should be assigned");
    }

    /* ----------------------------------------------------------------
       PUT - update an existing resource.
       ---------------------------------------------------------------- */
    @Test(description = "PUT /posts/1 updates the resource")
    public void updatePost() {
        Post update = new Post(1, "Updated title", "Updated body");

        given()
                .spec(requestSpec)
                .pathParam("id", 1)
                .body(update)
        .when()
                .put(Endpoints.POST_BY_ID)
        .then()
                .statusCode(200)
                .body("title", equalTo("Updated title"));
    }

    /* ----------------------------------------------------------------
       DELETE - remove a resource.
       ---------------------------------------------------------------- */
    @Test(description = "DELETE /posts/1 succeeds")
    public void deletePost() {
        given()
                .spec(requestSpec)
                .pathParam("id", 1)
        .when()
                .delete(Endpoints.POST_BY_ID)
        .then()
                .statusCode(200);
    }

    /* ----------------------------------------------------------------
       Capturing the Response object lets you inspect it programmatically
       (status, headers, timing, parsed JSON) instead of chaining .then().
       ---------------------------------------------------------------- */
    @Test(description = "Inspect the raw Response object")
    public void inspectResponse() {
        Response response =
                given()
                        .spec(requestSpec)
                        .pathParam("id", 2)
                .when()
                        .get(Endpoints.POST_BY_ID);

        assertEquals(response.statusCode(), 200);
        assertEquals(response.jsonPath().getInt("id"), 2);
        System.out.println("Response time: " + response.time() + " ms");
    }
}
