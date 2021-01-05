package com.example.wastesorting.searchResult;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchItemList {

    public int code;

    public String msg;

    @SerializedName("newslist")
    public List<SearchItem> searchItemList;
}
