package com.example.konf.API.Models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.jar.Attributes;

public class User {
    private String Email;
    private String FirstName;
    private String SecondName;
    private String FathersName;
    private String Phone;
    private String Gender;
    private Params[] params;
    private String ProfileImage;

    public User(JSONObject incom) throws JSONException {
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

    public String GetName(){return FirstName; }
    public String GetSecondName(){return SecondName; }
    public String GetFatherName(){return FathersName; }
    public String GetEmail(){return Email; }
    public String GetPhone(){return Phone; }
    public String GetGender() {return Gender;}
    public Params[] GetParams(){return params;}
    public String GetPic(){return ProfileImage;}

}
