package com.example.konf.API.Models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class User {
    String Email;
    String FirstName;
    String SecondName;
    String FathersName;
    String Phone;
    String Gender;
    Params[] params;
    String ProfileImage;

    public User(JSONObject incom) {
        try {
            this.Email = incom.getString("Email");
            this.FirstName = incom.getString("FirstName");
            this.SecondName = incom.getString("SecondName");
            this.FathersName = incom.getString("FatherName");
            this.Phone = incom.getString("Phone");
            this.Gender = incom.getString("Gender");
            this.ProfileImage = incom.getString("ProfileImage");

            JSONArray params = incom.getJSONArray("Params");
            ArrayList<Params> Param = new ArrayList<>();

            for(int i = 0; i < params.length(); i++){
                JSONObject param = params.getJSONObject(i);
                Params p = new Params();

                p.Id = param.getString("Id");
                p.Name = param.getString("Name");
                p.Value = param.getString("Value");

                Param.add(p);
            }

            this.params = Param.toArray(new Params[Param.size()]);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
