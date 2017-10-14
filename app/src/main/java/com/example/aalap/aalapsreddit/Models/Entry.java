package com.example.aalap.aalapsreddit.Models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Aalap on 2017-09-28.
 */


@Root(name = "entry", strict = false)
public class Entry {

    @Element(name = "title")
    String title;

    @Element(name = "updated")
    String updated;

    @Element(required = false, name = "author")
    Author author;

    @Element(name = "content")
    String content;

//    @Element(name = "link", required = false)
//    String link;

    String imageLink;
    String commentLink;
    @Element(name = "id")
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommentLink() {
        return commentLink;
    }

    public void setCommentLink(String commentLink) {
        this.commentLink = commentLink;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    //    public String getLink() {
//        return link;
//    }
//
//    public void setLink(String link) {
//        this.link = link;
//    }

    @Override
    public String toString() {
        return "Entry{" +
                "title='" + title + '\n' +
                ", updated='" + updated + '\n' +
                ", author=" + author + "\n"+
                ", content='" + content + "\n" + ", id='" + id + '\n' +
                "------------------------------------------------------------"+
                '}';
    }
}
