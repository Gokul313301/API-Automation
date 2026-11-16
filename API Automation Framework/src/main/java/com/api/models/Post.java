package com.api.models;

/**
 * A POJO ("Plain Old Java Object") that mirrors the JSON shape of a post:
 *   { "userId": 1, "id": 1, "title": "...", "body": "..." }
 *
 * Jackson (via REST Assured) uses these fields to:
 *   - serialize: turn a Post object into a JSON request body
 *   - deserialize: turn a JSON response into a Post object
 *
 * Field names must match the JSON keys.
 */
public class Post {

    private Integer userId;
    private Integer id;
    private String title;
    private String body;

    // A no-args constructor is required by Jackson for deserialization.
    public Post() {
    }

    public Post(Integer userId, String title, String body) {
        this.userId = userId;
        this.title = title;
        this.body = body;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Post{userId=" + userId + ", id=" + id +
                ", title='" + title + "', body='" + body + "'}";
    }
}
