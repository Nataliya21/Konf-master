package com.example.konf.API.Models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RegSetting {
    String Id;
    String DisplayName;
    Boolean Requred;
    String Type;
    String DefaultValue;
    String [] ValueToPick;

    public RegSetting(JSONObject regSet){
        try {
            this.Id = regSet.getString("Id");
            this.DisplayName = regSet.getString("DisplayName");
            this.Requred = regSet.getBoolean("Requred");
            this.Type = regSet.getString("Type");
            this.DefaultValue = regSet.getString("DefaultValue");

            JSONArray jsonArray = regSet.getJSONArray("ValuesToPick");
            ArrayList<String> valueToPick = new ArrayList<String>();

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String value = jsonObject.toString();//?????? EntityUtil???
                valueToPick.add(value);
            }

            this.ValueToPick = valueToPick.toArray(new String[valueToPick.size()]);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
