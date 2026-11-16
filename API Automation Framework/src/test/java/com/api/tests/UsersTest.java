package com.api.tests;

import com.api.constants.Endpoints;
import com.api.models.User;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Shows two more useful techniques:
 *   1. Deserializing a JSON array into a List of POJOs.
 *   2. Data-driven testing with a TestNG @DataProvider.
 */
public class UsersTest extends BaseTest {

    @Test(description = "GET /users deserializes into a List<User>")
    public void getAllUsersAsList() {
        List<User> users =
                given()
                        .spec(requestSpec)
                .when()
                        .get(Endpoints.USERS)
                .then()
                        .statusCode(200)
                        .extract().jsonPath().getList(".", User.class);

        assertEquals(users.size(), 10);
        // Every user should have a non-empty email.
        users.forEach(u -> assertTrue(u.getEmail().contains("@"), "Invalid email: " + u));
    }

    /**
     * A DataProvider feeds each row into the test as separate arguments,
     * so this one method runs three times - once per user id.
     */
    @DataProvider(name = "userIds")
    public Object[][] userIds() {
        return new Object[][]{
                {1, "Bret"},
                {2, "Antonette"},
                {3, "Samantha"}
        };
    }

    @Test(dataProvider = "userIds",
            description = "GET /users/{id} returns the expected username")
    public void getUserById(int id, String expectedUsername) {
        given()
                .spec(requestSpec)
                .pathParam("id", id)
        .when()
                .get(Endpoints.USER_BY_ID)
        .then()
                .statusCode(200)
                .body("username", equalTo(expectedUsername));
    }
}
