package com.example.konf.API.Models.News;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Comments {
    int CurrentPage;
    int PageCount;
    Comment[] comments;

    public Comments(JSONObject incom){
        try{
            this.CurrentPage = incom.getInt("CurrentPage");
            this.PageCount = incom.getInt("PagesCount");

            JSONArray array = incom.getJSONArray("Comments");
            ArrayList<Comment> comments1 = new ArrayList<Comment>();

            for(int i = 0; i < array.length(); i++){
                JSONObject obj = array.getJSONObject(i);
                Comment comment = new Comment(obj);
                comments1.add(comment);
            }

            this.comments = comments1.toArray(new Comment[comments1.size()]);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Comment[] GetComments(){return comments;}
}
