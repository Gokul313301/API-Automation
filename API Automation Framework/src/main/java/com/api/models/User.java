package com.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A partial POJO for a user. The real JSON has more fields (address,
 * company, etc.) but we only map the ones we care about.
 *
 * @JsonIgnoreProperties(ignoreUnknown = true) tells Jackson to skip any
 * JSON keys that don't have a matching field, instead of failing.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private Integer id;
    private String name;
    private String username;
    private String email;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name +
                "', username='" + username + "', email='" + email + "'}";
    }
}
