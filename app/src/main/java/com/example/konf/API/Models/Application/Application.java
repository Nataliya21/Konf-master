package com.example.konf.API.Models.Application;

import org.json.JSONObject;

public class Application {
    String Id;
    String Name;
    String Date;

    public Application(JSONObject income){
        try{
            this.Id = income.getString("Id");
            this.Name = income.getString("Name");
            this.Date = income.getString("Date");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String GetId(){return Id;}

    public String GetName(){return Name;}

    public String GetDate(){return Date;}
}
