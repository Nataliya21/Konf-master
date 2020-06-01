package com.example.konf.API.Models.User;

public class Params {
    String Id;
    String Name;
    String Value;

    public Params(String Id, String Name){
        this.Id = Id;
        this.Name = Name;
    }

    public Params(String Id, String Name, String Value){
        this.Id = Id;
        this.Name = Name;
        this.Value = Value;
    }

    public Params(){}

    public String GetId(){return Id;}
    public String GetValue(){return Value;}
    public String GetName(){return Name;}

    public void SetId(String Id){this.Id = Id;}
    public void SetName(String Name){this.Name = Name;}
    public void SetValue(String Value){this.Value = Value;}

}
