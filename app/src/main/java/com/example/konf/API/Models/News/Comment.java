package com.example.konf.API.Models.News;

import org.json.JSONObject;

public class Comment {
    String CommentId;
    String UserName;
    String Comment; //text of the comment

    public Comment(JSONObject obj){
        try{
            this.CommentId = obj.getString("CommentId");
            this.UserName = obj.getString("UserName");
            this.Comment = obj.getString("Comment");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String GetCommentId(){return CommentId;}

    public String GetUserName(){return UserName;}

    public String GetComment(){return Comment;}
}
