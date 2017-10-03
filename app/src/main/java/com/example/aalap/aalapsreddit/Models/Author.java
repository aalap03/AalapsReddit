package com.example.aalap.aalapsreddit.Models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Aalap on 2017-10-01.
 */

@Root(name = "author", strict = false)
public class Author {

    @Element(name = "name")
    String name;
    @Element(name = "uri")
    String uri;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "Author{" +
                "name='" + name + '\'' +
                ", uri='" + uri + '\'' +
                '}';
    }
}
