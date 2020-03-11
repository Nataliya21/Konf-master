package com.example.konf.API.Models.Application;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DocList {
    String Id;
    String DisplayName;
    Boolean Requred;
    String [] PossibleExtentions;
    int MaxSize;

    public DocList(JSONObject income){
        try{
            this.Id = income.getString("Id");
            this.DisplayName = income.getString("DisplayName");
            this.Requred = income.getBoolean("Requred");

            JSONArray jsonArray = income.getJSONArray("PossibleExtentions");
            ArrayList<String> possibleExtentions = new ArrayList<String>();

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String value = jsonObject.toString();
                possibleExtentions .add(value);
            }

            this.PossibleExtentions  = possibleExtentions .toArray(new String[possibleExtentions .size()]);

            this.MaxSize = income.getInt("MaxSize");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
