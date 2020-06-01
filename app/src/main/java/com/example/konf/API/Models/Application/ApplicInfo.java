package com.example.konf.API.Models.Application;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ApplicInfo {
    String Id;
    String Status;
    String FiledTime;
    ApplParams[] params;
    String [] Documents;
    Complaints[] complaints;
    Boolean Editable;

    public ApplicInfo(JSONObject income){
        try{
            this.Id = income.getString("Id");
            this.Status = income.getString("Status");
            this.FiledTime = income.getString("FiledTime");

            JSONArray jsonArray = income.getJSONArray("Params");
            ArrayList<ApplParams> applParams = new ArrayList<ApplParams>();

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ApplParams param = new ApplParams(jsonObject);
                applParams.add(param);
            }

            this.params = applParams.toArray(new ApplParams[applParams.size()]);

            jsonArray = income.getJSONArray("Documents");
            ArrayList<String> docs = new ArrayList<String>();

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String docId = jsonObject.getString("Id");
                docs.add(docId);
            }

            this.Documents = docs.toArray(new String[docs.size()]);

            jsonArray = income.getJSONArray("Complaints");
            ArrayList<Complaints> complaints = new ArrayList<Complaints>();

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Complaints complaint = new Complaints(jsonObject);
                complaints.add(complaint);
            }

            this.complaints = complaints.toArray(new Complaints[complaints.size()]);

            this.Editable = income.getBoolean("Editable");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String GetStatus(){return Status;}

    public String GetTime(){return FiledTime;}

    public ApplParams[] GetParams(){return params;}

    public String[] GetDocs(){return Documents;}

    public Complaints[] GetCompl(){return complaints;}

    public Boolean isEditable(){return Editable;}


}
