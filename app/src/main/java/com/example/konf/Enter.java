package com.example.konf;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.konf.API.Models.User.RegSetting;
import com.example.konf.API.Models.User.Token;

import static com.example.konf.API.API.ForgotPass;
import static com.example.konf.API.API.GetRegistSetting;
import static com.example.konf.API.API.GetToken;
import static com.example.konf.API.DB.SetToken;

public class Enter extends AppCompatActivity {

    public Button enter, reg;
    public EditText log, pas;
    public TextView forgot;
    public ProgressBar spiner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        enter = findViewById(R.id.enter);
        reg = findViewById(R.id.reg);
        log = findViewById(R.id.login);
        pas = findViewById(R.id.password);
        forgot = findViewById(R.id.textView2);
        spiner = findViewById(R.id.pg);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Enter(log.getText().toString(), pas.getText().toString());
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registr = new Intent(Enter.this, Registration.class);
                startActivity(registr);
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Enter.this);
                builder.setTitle("Внимание!")
                        .setMessage("Вы забыли пароль!")
                        .setNegativeButton("ОК",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
                new Forgot().execute();
            }
        });
    }

    private class TokenA extends AsyncTask<Void,Void, Token> {

        @Override
        protected void onPreExecute(){
           spiner.setVisibility(View.VISIBLE);
           enter.setVisibility(View.INVISIBLE);
           reg.setVisibility(View.INVISIBLE);
           //super.onPreExecut();
        }

        @Override
        protected Token doInBackground(Void... voids) {
            Token token = GetToken(log.getText().toString(), pas.getText().toString(), Enter.this);
            return token;
        }

        @Override
        protected void onPostExecute(Token aVoid){
            super.onPostExecute(aVoid);

            spiner.setVisibility(View.GONE);
            enter.setVisibility(View.VISIBLE);
            reg.setVisibility(View.VISIBLE);

            if(aVoid!=null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Enter.this);
                builder.setTitle("Внимание!").
                        setMessage("Вход!").
                        setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onPause();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
            else{
                AlertDialog.Builder bldr = new AlertDialog.Builder(Enter.this);
                bldr.setTitle("Внимание!").
                        setMessage("Неудалось выполнить вход!").
                        setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onPause();
                            }
                        });
                AlertDialog alrt = bldr.create();
                alrt.show();
            }

            return;
        }
    }

    private class Forgot extends AsyncTask<Void,Void,Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            return ForgotPass(log.getText().toString());
        }
    }

    public void Enter(String login, String password)
    {
        if((login=="")||(password==""))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(Enter.this);
            builder.setTitle("Внимание!")
                    .setMessage("Вы не ввели логин и/или пароль!")
                    .setNegativeButton("ОК",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else{
            try{
                new TokenA().execute();
                Intent prof = new Intent(Enter.this, Profile.class);
                startActivity(prof);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

    }


}
