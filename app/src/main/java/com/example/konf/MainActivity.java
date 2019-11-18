package com.example.konf;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.example.konf.API.Models.User.RegSetting;

import java.net.ConnectException;

import static com.example.konf.API.API.GetRegistSetting;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView icon = findViewById(R.id.icon);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //проверка наличия подключения к интернету, алерт. При нажатии на ок закрытие приложения
        Context context = this;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork.isConnectedOrConnecting();

        if(!isConnected)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Внимание!").
                        setMessage("Подключение к интернету отсутствует. Проверьте подключение к сети и перезапустите приложение.").
                        setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onPause();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                return;
        }


        Test test = new Test();
        test.execute();
        //загрузка информации о конференции

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

private class Test extends AsyncTask<Void,Void,RegSetting []>  {
    @Override
    protected RegSetting [] doInBackground(Void... voids) {
        RegSetting [] regSettings = null;
        try {
            regSettings = GetRegistSetting();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return regSettings;
    }

    @Override
    protected void onPostExecute(RegSetting [] aVoid){
        super.onPostExecute(aVoid);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
        return;
    }
}


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_prof) {
            // Переход к профилю
            Intent ent = new Intent(MainActivity.this, Enter.class);
            startActivity(ent);
            /*Intent profile = new Intent (MainActivity.this, Profile.class);
            startActivity(profile);*/
        } else if (id == R.id.nav_add) {
            // Переход к списку заявок пользователя
            Intent add = new Intent(MainActivity.this, Add.class);
            startActivity(add);
        } else if (id == R.id.nav_news) {
            // Обновление мейн окна
            Intent main = new Intent(MainActivity.this, MainActivity.class);
            startActivity(main);
        } else if (id == R.id.nav_ex) {
            //Удаление данных из бд
        } else if (id == R.id.nav_about) {
            //О приложении
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
