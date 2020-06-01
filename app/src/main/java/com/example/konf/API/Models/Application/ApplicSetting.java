package com.example.konf.API.Models.Application;

import android.graphics.Typeface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ApplicSetting {
    String Id;
    String DisplayName;
    Boolean Requred;
    String Type;
    String DefaultValue;
    String [] ValueToPik;

    public ApplicSetting(JSONObject income){
        try {
            this.Id = income.getString("Id");
            this.DisplayName = income.getString("DisplayName");
            this.Requred = income.getBoolean("Requred");
            this.Type = income.getString("Type");
            this.DefaultValue = income.getString("DefaultValue");

            JSONArray jsonArray = income.getJSONArray("ValuesToPick");
            ArrayList<String> valueToPick = new ArrayList<String>();

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String value = jsonObject.toString();
                valueToPick.add(value);
            }

            this.ValueToPik = valueToPick.toArray(new String[valueToPick.size()]);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Boolean GetR(){return Requred;}

    public String GetName(){return DisplayName;}

    public String GetId(){return Id;}

    public String GetType(){return Type;}

    public String GetDefault(){return DefaultValue;}

    public String[] GetValueToPick(){return ValueToPik;}
}
