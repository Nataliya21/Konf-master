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
import android.widget.TextView;

import com.example.konf.API.Models.User.RegSetting;

import static com.example.konf.API.API.GetRegistSetting;
import static com.example.konf.API.API.GetToken;
import static com.example.konf.API.DB.SetToken;

public class Enter extends AppCompatActivity {

    public Button enter, reg;
    public EditText log, pas;
    public TextView forgot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        enter = findViewById(R.id.enter);
        reg = findViewById(R.id.reg);
        log = findViewById(R.id.login);
        pas = findViewById(R.id.password);
        forgot = findViewById(R.id.textView2);

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
                //send message to server and do AlertDialog
            }
        });
    }

    ///AsyncTask!!!!!!!!

    private class Token extends AsyncTask<Void,Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String token = null;
            try {
                token = GetToken(log.getText().toString(), pas.getText().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return token;
        }

        @Override
        protected void onPostExecute(String aVoid){
            super.onPostExecute(aVoid);
            AlertDialog.Builder builder = new AlertDialog.Builder(Enter.this);
            builder.setTitle("Внимание!").
                    setMessage("Настраиваемы поля загружены!").
                    setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onPause();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            SetToken(aVoid, Enter.this);
            return;
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
                new Token().execute();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }


        }

    }


}
