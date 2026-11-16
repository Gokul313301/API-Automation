package com.api.tests;

import com.api.config.ConfigManager;
import com.api.specs.SpecFactory;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;

/**
 * Parent class for all test classes.
 *
 * Anything shared across tests goes here so individual tests stay focused
 * on the actual scenario. Every test class 'extends BaseTest'.
 */
public abstract class BaseTest {

    // Shared specs, built once per class before any test runs.
    protected RequestSpecification requestSpec;
    protected ResponseSpecification responseSpec;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        requestSpec = SpecFactory.requestSpec();
        responseSpec = SpecFactory.jsonOkSpec();

        if (ConfigManager.logAll()) {
            // Learning mode: print the FULL request and response (headers + pretty
            // JSON body) for EVERY call. These filters run on each request.
            // replaceFiltersWith(...) avoids stacking duplicates if setUp runs again.
            RestAssured.replaceFiltersWith(
                    new RequestLoggingFilter(),
                    new ResponseLoggingFilter());
        } else {
            // Quiet mode: only dump the request AND response when an assertion fails.
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        }
    }
}
