package com.example.aalap.aalapsreddit.Models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Aalap on 2017-09-28.
 */

@Root(name = "feed", strict = false)
public class Feed {

    @Element(name = "updated")
    String updated;
    @Element(name = "icon")
    String icon;
    @Element(name = "id")
    String id;
    @Element(name = "title")
    String title;
    @Element(name = "subtitle")
    String subtitle;
    @ElementList(inline = true, name = "entry")
    List<Entry> entry;

    public List<Entry> getEntry() {
        return entry;
    }

    public void setEntry(List<Entry> entry) {
        this.entry = entry;
    }

    public String getCategory() {
        return subtitle;
    }

    public void setCategory(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "updated='" + updated + '\'' +
                ", icon='" + icon + '\'' +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", entry=" + entry +
                '}';
    }
}
