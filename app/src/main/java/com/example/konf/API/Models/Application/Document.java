package com.example.konf.API.Models.Application;

public class Document {
    String DocumentTypeId;
    String Name;
    String Extention;
    String Data;

    public Document(String DocumentTypeId, String Name, String Extention, String Data){
        this.DocumentTypeId = DocumentTypeId;
        this.Name = Name;
        this.Extention = Extention;
        this.Data = Data;
    }

    public String getDocumentTypeId(){return DocumentTypeId;}
    public String getName(){return Name;}
    public String getExtention(){return Extention;}
    public String getData(){return Data;}
}
