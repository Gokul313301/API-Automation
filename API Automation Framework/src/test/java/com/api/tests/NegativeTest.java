package com.api.tests;

import com.api.constants.Endpoints;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

/**
 * Negative tests = deliberately asking for things that should FAIL, and
 * asserting that the API fails *correctly* (right status code, right shape).
 *
 * Testing the unhappy path is just as important as the happy path - real bugs
 * often hide in how an API responds to bad input.
 */
public class NegativeTest extends BaseTest {

    /* ----------------------------------------------------------------
       Asking for a resource that doesn't exist should return 404.
       Note we assert statusCode(404) - so a 200 here would FAIL the test.
       ---------------------------------------------------------------- */
    @Test(description = "GET a non-existent post returns 404 Not Found")
    public void getMissingPostReturns404() {
        given()
                .spec(requestSpec)
                .pathParam("id", 99999)
        .when()
                .get(Endpoints.POST_BY_ID)
        .then()
                .statusCode(404);
    }

    @Test(description = "GET a non-existent user returns 404 Not Found")
    public void getMissingUserReturns404() {
        given()
                .spec(requestSpec)
                .pathParam("id", 99999)
        .when()
                .get(Endpoints.USER_BY_ID)
        .then()
                .statusCode(404);
    }

    /* ----------------------------------------------------------------
       A wrong/unknown path should also not return 200.
       ---------------------------------------------------------------- */
    @Test(description = "GET an unknown endpoint returns 404")
    public void unknownEndpointReturns404() {
        given()
                .spec(requestSpec)
        .when()
                .get("/this-path-does-not-exist")
        .then()
                .statusCode(404);
    }

    /* ----------------------------------------------------------------
       A single post has exactly these 4 keys. Asserting the shape catches
       the API silently adding/removing fields - a common real-world break.
       ---------------------------------------------------------------- */
    @Test(description = "A post has exactly the expected fields")
    public void postHasExpectedShape() {
        given()
                .spec(requestSpec)
                .pathParam("id", 1)
        .when()
                .get(Endpoints.POST_BY_ID)
        .then()
                .statusCode(200)
                .body("size()", equalTo(4))   // userId, id, title, body
                .body("$", hasKey("userId"))  // "$" = the whole JSON object
                .body("$", hasKey("title"));
    }
}
