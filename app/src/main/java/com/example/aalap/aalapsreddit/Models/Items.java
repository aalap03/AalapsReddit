package com.example.aalap.aalapsreddit.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Aalap on 2017-10-14.
 */

public class Items {

    @SerializedName("items")
    @Expose
    private List<Categories> categories;

    public List<Categories> getCategories() {
        return categories;
    }

    public void setCategories(List<Categories> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "Items{" +
                "categories=" + categories +
                '}';
    }
}
