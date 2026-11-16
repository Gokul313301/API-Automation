package com.api.constants;

/**
 * Central list of API paths (relative to base.url).
 *
 * Keeping endpoints in one place means a URL change is a one-line edit,
 * and tests read clearly: get(Endpoints.POST_BY_ID) instead of a raw string.
 */
public final class Endpoints {

    private Endpoints() {
    }

    public static final String POSTS = "/posts";
    public static final String POST_BY_ID = "/posts/{id}";

    public static final String USERS = "/users";
    public static final String USER_BY_ID = "/users/{id}";
}
