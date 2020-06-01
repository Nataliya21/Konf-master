package com.example.konf;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.konf.API.Models.User.Token;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.konf.API.DB.DeleteDataFromDB;
import static com.example.konf.API.DB.GetTokenFromDb;


public class Add extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button list, conf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        list = findViewById(R.id.list);
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent list = new Intent(Add.this, List.class);
                startActivity(list);
            }
        });
        conf = findViewById(R.id.conf);
        conf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent conf = new Intent(Add.this, ConfFrom.class);
                conf.putExtra("Edit","false");
                startActivity(conf);
            }
        });
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
                Intent profile = new Intent (Add.this, Profile.class);
                startActivity(profile);
            }else{
                Intent ent = new Intent(Add.this, Enter.class);
                startActivity(ent);
            }
        } else if (id == R.id.nav_add) {
            // Переход к списку заявок пользователя
            Token token = GetTokenFromDb(this);
            Date end = new Date(token.GetDate());
            Date current = Calendar.getInstance().getTime();
            if((token.GetToken()!=null)&&(current.compareTo(end)>=0)){
                Intent add = new Intent(Add.this, Add.class);
                startActivity(add);
            }else{
                Intent ent = new Intent(Add.this, Enter.class);
                startActivity(ent);
            }
        } else if (id == R.id.nav_news) {
            // Обновление мейн окна
            Intent main = new Intent(Add.this, MainActivity.class);
            startActivity(main);
        } else if (id == R.id.nav_ex) {
            //Удаление данных из бд
            Token token = GetTokenFromDb(this);
            Date end = new Date(token.GetDate());
            Date current = Calendar.getInstance().getTime();
            if((token.GetToken()!=null)&&(current.compareTo(end)>=0)){
                AlertDialog.Builder builder = new AlertDialog.Builder(Add.this);
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
                Intent ent = new Intent(Add.this, Enter.class);
                startActivity(ent);
            }
        } else if (id == R.id.nav_about) {
            //О приложении
            AlertDialog.Builder builder = new AlertDialog.Builder(Add.this);
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
