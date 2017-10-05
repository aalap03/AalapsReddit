package com.example.aalap.aalapsreddit.Models;

/**
 * Created by Aalap on 2017-10-04.
 */

public class Comments {

    String comment;
    String updatedAt;
    String author;
    String id;

    public Comments(String comment, String updatedAt, String author, String id) {
        this.comment = comment;
        this.updatedAt = updatedAt;
        this.author = author;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Comments{" +
                "comment='" + comment + '\n' +
                ", updatedAt='" + updatedAt + '\n' +
                ", author='" + author + '\n' + "-------------------------------------------------->"+
                '}';
    }
}
