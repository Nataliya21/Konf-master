package com.example.konf;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static com.example.konf.API.API.ChangePass;
import static com.example.konf.API.DB.GetTokenFromDb;


public class Forgot_password extends AppCompatActivity {

    Button save;
    EditText old, newPass, confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        old = findViewById(R.id.first);
        newPass = findViewById(R.id.second);
        confirm = findViewById(R.id.confirm);
        save = findViewById(R.id.save);
        final String[] pass = new String[3];
        if ((old.getText().toString()!="")&&(newPass.getText().toString()!="")&&(confirm.getText().toString()!="")){
            pass[0] = old.getText().toString();
            pass[1] = newPass.getText().toString();
            pass[2] = confirm.getText().toString();
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSavePress(GetTokenFromDb(getApplicationContext()).GetToken(),pass[0], pass[1], pass[2]);
            }
        });
    }

    public void onSavePress(String token, String old, String newPass, String confirm){
        if((old!="")&&(newPass!="")&&(confirm!="")){
            new SaveChange().execute(token, old, newPass, confirm);
            onBackPressed();
        }
    }

    protected class SaveChange extends AsyncTask<String, Void,Void>{

        @Override
        protected Void doInBackground(String... strings) {
            Boolean flag = ChangePass(strings[0], strings[1], strings[2], strings[3]);
            return null;
        }
    }
}
