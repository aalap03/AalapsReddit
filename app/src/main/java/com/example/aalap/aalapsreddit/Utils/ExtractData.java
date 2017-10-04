package com.example.aalap.aalapsreddit.Utils;

import java.util.List;

/**
 * Created by Aalap on 2017-10-03.
 */

public class ExtractData {

    String xmlText, tag;

    public ExtractData(String xmlText, String tag) {
        this.xmlText = xmlText;
        this.tag = tag;
    }

    public String extract() {
        String[] split = xmlText.split(tag + "\"");
        if (split != null) {
            if (split.length > 1)
                return split[1].split("\"")[0];
            else
                return split[0].split("\"")[0];
        } else {
            return "";
        }

    }
}
