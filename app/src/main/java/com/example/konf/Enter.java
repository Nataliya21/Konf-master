package com.example.konf;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static com.example.konf.API.API.GetToken;
import static com.example.konf.API.DB.SetToken;

public class Enter extends AppCompatActivity {

    public Button enter, reg;
    public EditText log, pas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        enter = findViewById(R.id.enter);
        reg = findViewById(R.id.reg);
        log = findViewById(R.id.login);
        pas = findViewById(R.id.password);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Enter(log.getText().toString(), pas.getText().toString());
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    ///AsyncTask!!!!!!!!
    public void Enter(String login, String password)
    {
        String token = "";
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
                token =  GetToken(login, password);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            SetToken(token,this);
        }

    }
}
