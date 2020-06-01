package com.example.konf;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.konf.API.Models.Application.Application;

import org.w3c.dom.Text;

import static com.example.konf.API.API.DeleteApplication;
import static com.example.konf.API.API.GetApplication;
import static com.example.konf.API.DB.DeleteDataFromDB;
import static com.example.konf.API.DB.GetTokenFromDb;

public class List extends AppCompatActivity {

    RelativeLayout relativeLayout;
    Application[] userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        relativeLayout = findViewById(R.id.layout);

        new GetList(GetTokenFromDb(getApplicationContext()).GetToken()).execute();

    }

    public void onLayoutPress(String applId){

        Intent info = new Intent(List.this, appInfo.class);
        info.putExtra("appId",applId);
        startActivity(info);

    }

    public void onLayoutHold(final String token, final String applId){
        AlertDialog.Builder builder = new AlertDialog.Builder(List.this);
        builder.setTitle("Внимание!").
                setMessage("Вы действительно хотите выйти из аккаунта?").
                setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       new DeleteAppl(token,applId).execute();
                    }
                }).setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onPause();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    protected class GetList extends AsyncTask<Void,Void,Void>{

        String token;

        public GetList(String token){
            this.token = token;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            userList = GetApplication(token);
            return null;
        }

        protected void onPostExecute(Void aVoid){

            ScrollView scrollView =  new ScrollView(getApplicationContext());
            for(final Application userAppl : userList){
                final RelativeLayout layout = new RelativeLayout(getApplicationContext());
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                layout.setLayoutParams(layoutParams);

                RelativeLayout.LayoutParams paramsName = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                paramsName.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                RelativeLayout.LayoutParams paramsDate = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                paramsDate.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                layout.setTag(userAppl.GetId());
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onLayoutPress(layout.getTag().toString());
                    }
                });
                layout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        onLayoutHold(token, layout.getTag().toString());
                        return false;
                    }
                });

                TextView name  =  new TextView(getApplicationContext());
                name.setText(userAppl.GetName());
                name.setLayoutParams(paramsName);

                TextView date = new TextView(getApplicationContext());
                date.setText(userAppl.GetDate());
                date.setLayoutParams(paramsDate);

                layout.addView(name);
                layout.addView(date);

                scrollView.addView(layout);
            }
            relativeLayout.addView(scrollView);
        }
    }

    protected class DeleteAppl extends AsyncTask<Void,Void,Void>{

        String token;
        String applicId;

        public DeleteAppl(String token, String applicId){
            this.token = token;
            this.applicId = applicId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            DeleteApplication(token,applicId);
            return null;
        }
    }
}
