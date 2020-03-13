package com.example.konf.API.Models.News;

import org.json.JSONObject;

public class News {

    String NewsId;
    String Header;
    String Image;
    Boolean IsLikedyMe;
    int LikesCount;
    String PreviewText;
    String TimePosted;
    Comments comments;
    String Body;

    public News(JSONObject incom){
        try{
            this.NewsId = incom.getString("NewsId");
            this.Header = incom.getString("Header");
            this.Image = incom.getString("Image");
            this.IsLikedyMe = incom.getBoolean("IsLikedByMe");
            this.LikesCount = incom.getInt("LikesCount");
            this.PreviewText = incom.getString("PreviewText");
            this.TimePosted = incom.getString("TimePosted");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public News(JSONObject incom, String body, Comments comments){
        try{
            this.NewsId = incom.getString("NewsId");
            this.Header = incom.getString("Header");
            this.Image = incom.getString("Image");
            this.IsLikedyMe = incom.getBoolean("IsLikedByMe");
            this.LikesCount = incom.getInt("LikesCount");
            this.PreviewText = incom.getString("PreviewText");
            this.TimePosted = incom.getString("TimePosted");
            this.comments = comments;
            this.Body = body;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
