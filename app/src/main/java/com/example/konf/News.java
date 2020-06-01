package com.example.konf;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.konf.API.Models.News.Comment;
import com.example.konf.API.Models.News.Comments;
import com.example.konf.API.Models.User.Token;

import static com.example.konf.API.API.ConvertFromBase64;
import static com.example.konf.API.API.DisLike;
import static com.example.konf.API.API.GetCommentsPage;
import static com.example.konf.API.API.GetNews;
import static com.example.konf.API.API.Like;
import static com.example.konf.API.DB.GetPage;
import static com.example.konf.API.DB.GetTokenFromDb;

public class News extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Bundle arguments = getIntent().getExtras();
        String name = arguments.get("NewsId").toString();
        try{
            new GetNewsIn().execute(name);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private class GetNewsIn extends AsyncTask<String,Void, com.example.konf.API.Models.News.News>{
        @Override
        protected com.example.konf.API.Models.News.News doInBackground(String... voids) {

            com.example.konf.API.Models.News.News news1 = null;
            try{
                news1 = GetNews(voids[0]);
            }catch(Exception e){
                e.printStackTrace();
            }
            return news1;
        }

        protected void onPostExecute(final com.example.konf.API.Models.News.News news1){

            Token token = null;
            String tkn = "";
            try{
                token = GetTokenFromDb(getApplicationContext());
                tkn = token.GetToken();
            }catch (Exception e){
                e.printStackTrace();
            }
            ScrollView scrollView = new ScrollView(getApplicationContext());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            scrollView.setLayoutParams(params);
            RelativeLayout layout = new RelativeLayout(getApplicationContext());

           if(news1!=null){
               if(news1.GetImg()!=null){
                   Bitmap fotoBitmap = ConvertFromBase64(news1.GetImg());
                   ImageView imageView = new ImageView(getApplicationContext());
                   imageView.setImageBitmap(fotoBitmap);
                   RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
                           RelativeLayout.LayoutParams.MATCH_PARENT,
                           RelativeLayout.LayoutParams.WRAP_CONTENT
                   );
                   params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                   imageView.setLayoutParams(params1);
                   layout.addView(imageView);
               }

               TextView title = new TextView(getApplicationContext());
               title.setText(news1.GetHeader());
               title.setTextColor(Color.BLACK);
               title.setTypeface(null, Typeface.BOLD);
               RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
                       RelativeLayout.LayoutParams.MATCH_PARENT,
                       RelativeLayout.LayoutParams.WRAP_CONTENT
               );
               params2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
               title.setLayoutParams(params2);


               TextView body = new TextView(getApplicationContext());
               body.setText(news1.GetBody());
               body.setTextColor(Color.BLACK);
               body.setLayoutParams(params2);

               TextView time = new TextView(getApplicationContext());
               time.setText(news1.GetTime());
               time.setTextColor(Color.BLACK);
               RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(
                       RelativeLayout.LayoutParams.MATCH_PARENT,
                       RelativeLayout.LayoutParams.WRAP_CONTENT
               );
               params3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
               time.setLayoutParams(params3);

               int Id = 1;

               ImageButton com = new ImageButton(getApplicationContext());
               com.setImageResource(R.drawable.ic_comment);
               com.setLayoutParams(params3);
               com.setId(Id);
               com.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       onCommentPress(GetPage(getApplicationContext()), news1.GetId());
                   }
               });

               final ImageButton like = new ImageButton(getApplicationContext());
               if(news1.IsLikede()){
                   like.setImageResource(R.drawable.ic_liked);
                   like.setLayoutParams(params3);
                   like.setId(Id+1);
                   final String finalTkn = tkn;
                   like.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           onLikePress(finalTkn,news1.GetId(),news1.IsLikede(), like.getId());
                       }
                   });
               }else {
                   like.setImageResource(R.drawable.ic_like);
                   like.setLayoutParams(params3);
                   like.setId(Id+1);
                   final String finalTkn = tkn;
                   like.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           onLikePress(finalTkn,news1.GetId(),news1.IsLikede(), like.getId());
                       }
                   });
               }
               //число лайков рядом с кнопкой
               //число комментариев рядом с кнопкой

               layout.addView(title);
               layout.addView(body);
               layout.addView(time);
               layout.addView(com);
               layout.addView(like);
           }

        }
    }

    public void onLikePress(String token, String newsId, Boolean isLiked, Integer buttonId){
        if(isLiked==true){
            new dislike().execute(token,newsId);
            ImageButton button = findViewById(buttonId);
            button.setImageResource(R.drawable.ic_like);
        }else{
            new like().execute(token,newsId);
            ImageButton button = findViewById(buttonId);
            button.setImageResource(R.drawable.ic_liked);
        }
    }

    public void onCommentPress(String pageNumber, String newsId){

        Intent commentPage = new Intent(News.this, CommentPage.class);
        commentPage.putExtra("pageNumber", pageNumber);
        commentPage.putExtra("newsId", newsId);
    }

    private class dislike extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... strings) {
            DisLike(strings[0], strings[1]);
            return null;
        }
    }

    private class like extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... strings) {
            Like(strings[0],strings[1]);
            return null;
        }
    }


}
