package com.example.konf.API.Models.User;

import org.json.JSONException;
import org.json.JSONObject;

public class Token {
    String Token;
    String Date;

    public Token(JSONObject incom) throws JSONException {
        try{
            this.Token = incom.getString("access_token"); //посмотреть в вики
            this.Date = incom.getString(".issued"); //посмотреть в вики
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public String getToken(){
        return Token;
    }

    public String getDate(){
        return Date;
    }
}
