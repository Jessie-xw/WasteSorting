package com.example.wastesorting.httptool;

import com.example.wastesorting.newsitem.NewsList;
import com.google.gson.Gson;

public class Utility {

    public static NewsList parseJsonWithGSON(final String requestText){
        Gson gson = new Gson();
        return gson.fromJson(requestText, NewsList.class);
    }
}
