package com.example.konf.API.Models.News;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsArray {
    int CurrentPage;
    int PagesCount;
    News[] news;

    public NewsArray(JSONObject incom){
        try{
            this.CurrentPage = incom.getInt("CurrentPage");
            this.PagesCount = incom.getInt("PagessCount");

            JSONArray array = incom.getJSONArray("News");
            ArrayList<News> NewsPages = new ArrayList<News>();

            for(int i = 0; i < array.length(); i++){
                JSONObject obj = array.getJSONObject(i);
                News news = new News(obj);
                NewsPages.add(news);
            }

            this.news = NewsPages.toArray(new News[NewsPages.size()]);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
