package com.example.wastesorting.httptool;

import com.example.wastesorting.searchResult.SearchItemList;
import com.google.gson.Gson;

public class Utility_Search {

    public static SearchItemList parseJsonWithGson(final String requestText){
        Gson gson = new Gson();
        return gson.fromJson(requestText, SearchItemList.class);
    }
}
