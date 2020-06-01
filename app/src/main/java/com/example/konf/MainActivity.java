package com.example.konf;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.Layout;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.konf.API.Models.News.News;
import com.example.konf.API.Models.News.NewsArray;
import com.example.konf.API.Models.User.Token;

import java.util.Calendar;
import java.util.Date;

import static com.example.konf.API.API.ConvertFromBase64;
import static com.example.konf.API.API.GetNewsArray;
import static com.example.konf.API.DB.DeleteDataFromDB;
import static com.example.konf.API.DB.GetPage;
import static com.example.konf.API.DB.GetTokenFromDb;
import static com.example.konf.API.DB.SetPage;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    LinearLayout ll;
    ScrollView sv;
    TextView back, next, page;

    //получение страницы новости по нажатию кнопки (мб надо в бд записывать номер стр)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView icon = findViewById(R.id.icon);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ll = findViewById(R.id.ll);
        sv = findViewById(R.id.sv);
        page = findViewById(R.id.page);
        page.setText("0");
        SetPage(page.getText().toString(),MainActivity.this);
        new GetCards().execute(Integer.getInteger(page.getText().toString()));
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPress();
            }
        });
        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNextPress();
            }
        });
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

    }

    private class GetCards extends AsyncTask<Integer,Void,NewsArray>{ //cardView, получение списка новостей

        @Override
        protected NewsArray doInBackground(Integer... pages) {
            int page = pages[0];
            NewsArray array = GetNewsArray(page);// по номеру страницы
            return array;
        }

        protected void onPostExecute(NewsArray array){
            //создание и заполнение cardView from NewsArray
            News [] news = array.GetNews();
            for(int i = 0; i< news.length; i++){

                final CardView cardView = new CardView(MainActivity.this);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(20,20,20,20);
                cardView.setLayoutParams(params);
                cardView.setRadius(16F);
                cardView.setContentPadding(25,25,25,25);
                cardView.setCardBackgroundColor(Color.WHITE);
                cardView.setTag(news[i].GetId());
                RelativeLayout layout = new RelativeLayout(getApplicationContext());

                if(news[i].GetImg()!=null){
                    Bitmap fotoBitmap = ConvertFromBase64(news[i].GetImg());
                    ImageView imageView = new ImageView(getApplicationContext());
                    imageView.setImageBitmap(fotoBitmap);
                    RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    imageView.setLayoutParams(params1);
                    layout.addView(imageView);
                    //нарисовать картинку в круге, если будет красиво
                }

                TextView title = new TextView(getApplicationContext());
                title.setText(news[i].GetHeader());
                title.setTextColor(Color.BLACK);
                title.setTypeface(null, Typeface.BOLD);
                RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                params2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                title.setLayoutParams(params2);

                TextView preview = new TextView(getApplicationContext());
                preview.setText(news[i].GetPreview());
                preview.setTextColor(Color.BLACK);
                preview.setLayoutParams(params2);

                ImageButton com = new ImageButton(getApplicationContext());
                com.setImageResource(R.drawable.ic_comment);
                RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                params3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                com.setLayoutParams(params3);

                final ImageButton like = new ImageButton(getApplicationContext());
                if(news[i].IsLikede()){
                    like.setImageResource(R.drawable.ic_liked);
                    like.setLayoutParams(params3);
                }else {
                    like.setImageResource(R.drawable.ic_like);
                    like.setLayoutParams(params3);
                }
                //рядом с лайками их число
                //рядом с коментариями их число

                layout.addView(title);
                layout.addView(preview);
                layout.addView(com);
                layout.addView(like);
                cardView.addView(layout);

                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cardViewOnClick(cardView.getTag().toString());
                    }
                });
            }

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
                Intent profile = new Intent (MainActivity.this, Profile.class);
                startActivity(profile);
            }else{
                Intent ent = new Intent(MainActivity.this, Enter.class);
                startActivity(ent);
            }
        } else if (id == R.id.nav_add) {
            // Переход к списку заявок пользователя
            Token token = GetTokenFromDb(this);
            Date end = new Date(token.GetDate());
            Date current = Calendar.getInstance().getTime();
            if((token.GetToken()!=null)&&(current.compareTo(end)>=0)){
                Intent add = new Intent(MainActivity.this, Add.class);
                startActivity(add);
            }else{
                Intent ent = new Intent(MainActivity.this, Enter.class);
                startActivity(ent);
            }
        } else if (id == R.id.nav_news) {
            // Обновление мейн окна
            Intent main = new Intent(MainActivity.this, MainActivity.class);
            startActivity(main);
        } else if (id == R.id.nav_ex) {
            //Удаление данных из бд
            Token token = GetTokenFromDb(this);
            Date end = new Date(token.GetDate());
            Date current = Calendar.getInstance().getTime();
            if((token.GetToken()!=null)&&(current.compareTo(end)>=0)){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Внимание!").
                        setMessage("Вы действительно хотите выйти из аккаунта?").
                        setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteDataFromDB(MainActivity.this);
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
                Intent ent = new Intent(MainActivity.this, Enter.class);
                startActivity(ent);
            }
        } else if (id == R.id.nav_about) {
            //О приложении
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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

    public void cardViewOnClick(String NewsId){
        Intent news = new Intent(MainActivity.this, News.class);
        news.putExtra("NewsId", NewsId);
        startActivity(news);
    }

    public void onBackPress(){
        int number = -2;
        try{
            number = Integer.getInteger(GetPage(MainActivity.this));
        }catch (Exception e){
            e.printStackTrace();
        }
        if(number<=0){
            number = 0;
            SetPage(String.valueOf(number), MainActivity.this);
            new GetCards().execute(number);
            page.setText(String.valueOf(number));
        }else{
            number--;
            SetPage(String.valueOf(number), MainActivity.this);
            new GetCards().execute(number);
            page.setText(String.valueOf(number));
        }
    }

    public void onNextPress(){
        int number = -2;
        try{
            number = Integer.getInteger(GetPage(MainActivity.this));
        }catch(Exception e){
            e.printStackTrace();
        }
        if(number>=0){
            number++;
            SetPage(String.valueOf(number), MainActivity.this);
            new GetCards().execute(number);
            page.setText(String.valueOf(number));
        }
    }

}
