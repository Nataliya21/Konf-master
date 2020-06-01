package com.example.konf.API.Models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.jar.Attributes;

public class User {
    String Email;
    String FirstName;
    String SecondName;
    String FathersName;
    String Phone;
    String Gender;
    Params[] params;
    String ProfileImage;
    String BirthDate;

    public User(JSONObject incom) throws JSONException {
        try {
            this.Email = incom.getString("Email");
            this.FirstName = incom.getString("FirstName");
            this.SecondName = incom.getString("SecondName");
            this.FathersName = incom.getString("FatherName");
            this.Phone = incom.getString("Phone");
            this.Gender = incom.getString("Gender");
            this.ProfileImage = incom.getString("ProfileImage");
            this.BirthDate = incom.getString("BirthDate");

            JSONArray params = incom.getJSONArray("Params");
            ArrayList<Params> Param = new ArrayList<>();

            for(int i = 0; i < params.length(); i++){
                JSONObject param = params.getJSONObject(i);
                Params p = new Params();

                p.SetId(param.getString("Id"));
                p.SetName(param.getString("Name"));
                p.SetValue(param.getString("Value"));

                Param.add(p);
            }

            this.params = Param.toArray(new Params[Param.size()]);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public User(String Email, String FirstName, String SecondName, String FatherName, String Phone,
                String Gender, Params[] params, String ProfilePic, String BirthDate){
        this.Email = Email;
        this.FirstName = FirstName;
        this.SecondName = SecondName;
        this.FathersName = FatherName;
        this.Phone = Phone;
        this.Gender = Gender;
        this.params = params;
        this.ProfileImage = ProfilePic;
        this.BirthDate = BirthDate;
    }

    public String GetName(){return FirstName; }
    public String GetSecondName(){return SecondName; }
    public String GetFatherName(){return FathersName; }
    public String GetEmail(){return Email; }
    public String GetPhone(){return Phone; }
    public String GetGender() {return Gender;}
    public Params[] GetParams(){return params;}
    public String GetPic(){return ProfileImage;}
    public String GetBirthDate(){return BirthDate;}

}
