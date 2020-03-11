package com.example.konf.API.Models.Application;

import org.json.JSONObject;

public class ApplParams {
    String Id;
    String Value;

    public ApplParams(JSONObject incom){
        try{
            this.Id = incom.getString("Id");
            this.Value = incom.getString("Value");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getId() {
        return Id;
    }

    public String getValue(){return Value;}
}
