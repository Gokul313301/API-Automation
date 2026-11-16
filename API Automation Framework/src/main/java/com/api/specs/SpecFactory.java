package com.api.specs;

import com.api.config.ConfigManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static org.hamcrest.Matchers.lessThan;

/**
 * Builds reusable request/response "specifications".
 *
 * A RequestSpecification bundles the things every request shares
 * (base URL, headers, logging) so individual tests stay short.
 * A ResponseSpecification bundles common expectations
 * (e.g. "response should be JSON and reasonably fast").
 */
public final class SpecFactory {

    private SpecFactory() {
    }

    /** Common request setup: base URI, JSON content type, and logging. */
    public static RequestSpecification requestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigManager.baseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                // Note: request/response logging is configured globally in BaseTest,
                // driven by the log.all switch in config.properties.
                .build();
    }

    /** Common expectations you can attach to any successful JSON response. */
    public static ResponseSpecification jsonOkSpec() {
        return new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectResponseTime(lessThan(5000L)) // fail if slower than 5s
                .build();
    }
}
