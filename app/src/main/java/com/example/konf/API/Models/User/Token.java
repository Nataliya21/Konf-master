package com.example.konf.API.Models.User;

import org.json.JSONException;
import org.json.JSONObject;

public class Token {
    String Token;
    String Date;

    public Token(JSONObject incom) throws JSONException {
        try{
            this.Token = incom.getString("access_token");
            this.Date = incom.getString(".expires");
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public Token(){}

    public String GetToken(){
        return this.Token;
    }
    public String GetDate(){return this.Date;}
    public void SetToken(Token obj){
        this.Token = obj.Token;
        this.Date = obj.Date;
    }
}
