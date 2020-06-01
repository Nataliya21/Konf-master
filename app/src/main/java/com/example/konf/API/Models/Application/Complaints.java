package com.example.konf.API.Models.Application;

import org.json.JSONObject;

public class Complaints {
    String Complaint;
    String Date;

    public Complaints(JSONObject incom){
        try{
            this.Complaint = incom.getString("Complaint");
            this.Date = incom.getString("Date");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String GetCompl(){return Complaint;}

    public String GetDate(){return Date;}
}
