package com.example.konf;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.konf.API.Models.User.Params;
import com.example.konf.API.Models.User.Token;
import com.example.konf.API.Models.User.User;

import java.util.Calendar;
import java.util.Date;

import static com.example.konf.API.API.GetToken;
import static com.example.konf.API.API.GetUser;
import static com.example.konf.API.DB.DeleteDataFromDB;
import static com.example.konf.API.DB.GetTokenFromDb;
import static com.example.konf.API.DB.SetToken;

public class Profile extends AppCompatActivity

        implements NavigationView.OnNavigationItemSelectedListener {

    TextView name, surename, fathername, gender, phone;
    HorizontalScrollView hsv;
    LinearLayout llh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        name = findViewById(R.id.name);
        surename = findViewById(R.id.surname);
        fathername = findViewById(R.id.fathername);
        gender = findViewById(R.id.gender);
        phone = findViewById(R.id.phone);
        hsv = findViewById(R.id.hsv);
        llh = findViewById(R.id.llh);


        Button set = findViewById(R.id.setting);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ps = new Intent(Profile.this, ProfileSetting.class);
                startActivity(ps);
            }
        });

        try{
            Token token = GetTokenFromDb(Profile.this);
            String tkn = token.GetToken();
            new UserInfo().execute(tkn);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private class UserInfo extends AsyncTask<String,Void, User> {
        @Override
        protected User doInBackground(String... voids) {

            String bfr = voids[0];
            User user = GetUser(bfr) ;

            return user;
        }

        @Override
        protected void onPostExecute(User aVoid){
            super.onPostExecute(aVoid);

            if(aVoid!=null) {

                name.setText("Имя: "+aVoid.GetName());
                surename.setText("Фамилия: "+aVoid.GetSecondName());
                fathername.setText("Отчество: "+aVoid.GetFatherName());
                gender.setText("Пол: "+aVoid.GetGender());
                phone.setText("Телефон "+aVoid.GetPhone());

                Params[] params = aVoid.GetParams();
                for(Params prms: params){

                    RelativeLayout layout = new RelativeLayout(getApplicationContext());
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                    TextView name =  new TextView(getApplicationContext());
                    name.setText(prms.GetName());
                    name.setTag(prms.GetId());
                    name.setTextColor(Color.BLACK);
                    name.setLayoutParams(layoutParams);

                    TextView value = new TextView(getApplicationContext());
                    value.setText(prms.GetValue());
                    value.setTextColor(Color.BLACK);
                    value.setLayoutParams(layoutParams);

                    layout.addView(name);
                    layout.addView(value);

                    llh.addView(layout);
                    hsv.addView(llh);
                }

            }
            else{
                AlertDialog.Builder bldr = new AlertDialog.Builder(Profile.this);
                bldr.setTitle("Внимание!").
                        setMessage("Неудалось получить данные пользователя!").
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_prof) {
            // Переход к профилю
            Token token = GetTokenFromDb(this);
            Date end = new Date(token.GetDate());
            Date current = Calendar.getInstance().getTime();
            if((token.GetToken()!=null)&&(current.compareTo(end)>=0)){
                Intent profile = new Intent (Profile.this, Profile.class);
                startActivity(profile);
            }else{
                Intent ent = new Intent(Profile.this, Enter.class);
                startActivity(ent);
            }
        } else if (id == R.id.nav_add) {
            // Переход к списку заявок пользователя
            Token token = GetTokenFromDb(this);
            Date end = new Date(token.GetDate());
            Date current = Calendar.getInstance().getTime();
            if((token.GetToken()!=null)&&(current.compareTo(end)>=0)){
                Intent add = new Intent(Profile.this, Add.class);
                startActivity(add);
            }else{
                Intent ent = new Intent(Profile.this, Enter.class);
                startActivity(ent);
            }
        } else if (id == R.id.nav_news) {
            // Обновление мейн окна
            Intent main = new Intent(Profile.this, MainActivity.class);
            startActivity(main);
        } else if (id == R.id.nav_ex) {
            //Удаление данных из бд
            Token token = GetTokenFromDb(this);
            Date end = new Date(token.GetDate());
            Date current = Calendar.getInstance().getTime();
            if((token.GetToken()!=null)&&(current.compareTo(end)>=0)){
                AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
                builder.setTitle("Внимание!").
                        setMessage("Вы действительно хотите выйти из аккаунта?").
                        setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteDataFromDB(getApplicationContext());
                            }
                        }).setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onPause();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }else{
                Intent ent = new Intent(Profile.this, Enter.class);
                startActivity(ent);
            }
        } else if (id == R.id.nav_about) {
            //О приложении
            AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
            builder.setTitle("О приложении").
                    setMessage("Приложение создано в рамках информационной системы 'Конференция ЯГТУ'").
                    setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onPause();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
