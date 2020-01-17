package com.example.konf.API;

import android.content.Context;

import com.example.konf.API.Models.User.Token;

import net.rehacktive.waspdb.WaspDb;
import net.rehacktive.waspdb.WaspFactory;
import net.rehacktive.waspdb.WaspHash;

public class DB {

    public static void SetToken(Token token, Context context){
        WaspDb Db = WaspFactory.openOrCreateDatabase(context.getFilesDir().getPath(), "MedDB", "pass");
        WaspHash hash = Db.openOrCreateHash("Token");

        hash.put("Token", token);
    }

}
