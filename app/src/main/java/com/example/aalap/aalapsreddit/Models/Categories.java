package com.example.aalap.aalapsreddit.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Aalap on 2017-10-14.
 */

public class Categories {

    @SerializedName("categories")
    @Expose
    private List<String> categories = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public Categories() {
    }

    /**
     *
     * @param categories
     */
    public Categories(List<String> categories) {
        super();
        this.categories = categories;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "Categories{" +
                "categories=" + categories +
                '}';
    }
}
