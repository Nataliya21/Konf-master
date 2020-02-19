package com.example.konf;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;

import com.example.konf.API.Models.User.Token;
import com.example.konf.API.Models.User.User;

import static com.example.konf.API.API.GetToken;
import static com.example.konf.API.API.GetUser;
import static com.example.konf.API.DB.GetTokenFromDb;
import static com.example.konf.API.DB.SetToken;

public class Profile extends AppCompatActivity


        implements NavigationView.OnNavigationItemSelectedListener {

    TextView name, surename, fathername, gender, phone;

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



        Button set = findViewById(R.id.setting);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ps = new Intent(Profile.this, ProfileSetting.class);
                startActivity(ps);
            }
        });

        Getter();
    }

    private void Getter(){
        Token token = GetTokenFromDb(Profile.this);
        String tkn = token.GetToken();
        new UserInfo().execute(tkn);
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

                name.setText(name+" "+aVoid.GetName());
                surename.setText(surename+" "+aVoid.GetSecondName());
                fathername.setText(fathername+" "+aVoid.GetFatherName());
                gender.setText(gender+" "+aVoid.GetGender());
                phone.setText(phone+" "+aVoid.GetPhone());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_prof) {
            Intent prof = new Intent(Profile.this, Profile.class);
            startActivity(prof);
        } else if (id == R.id.nav_add) {
            Intent add = new Intent(Profile.this, Add.class);
            startActivity(add);
        } else if (id == R.id.nav_news) {
            Intent main = new Intent(Profile.this, MainActivity.class);
            startActivity(main);

        } else if (id == R.id.nav_ex) {

        } else if (id == R.id.nav_about) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
