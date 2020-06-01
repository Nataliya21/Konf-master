package com.example.konf;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.konf.API.API;
import com.example.konf.API.Models.News.Comment;
import com.example.konf.API.Models.News.Comments;
import com.example.konf.API.Models.User.Token;

import static com.example.konf.API.API.DeleteComment;
import static com.example.konf.API.API.GetCommentsPage;
import static com.example.konf.API.DB.GetTokenFromDb;

public class CommentPage extends AppCompatActivity {
    String page, newsId;
    EditText message;
    Button send;
    ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_page);
        Bundle arguments = getIntent().getExtras();
        page = arguments.get("pageNumber").toString();
        newsId = arguments.get("newsId").toString();
        message = findViewById(R.id.comment);

        Token token = null;
        String tkn = "";
        try{
            token =  GetTokenFromDb(getApplicationContext());
            tkn = token.GetToken();
        }catch(Exception e){
            e.printStackTrace();
        }

        send = findViewById(R.id.send);
        final String finalTkn = tkn;
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSendPress(finalTkn,newsId,message.getText().toString());
            }
        });
        scrollView =  findViewById(R.id.sv);

        new getComments().execute(page, newsId);


    }

    private class getComments extends AsyncTask<String,Void, Comments> {

        @Override
        protected Comments doInBackground(String... strings) {
            Comments comments = GetCommentsPage(strings[0], strings[1]);
            return comments;
        }

        @SuppressLint("ResourceAsColor")
        protected void onPostExecute(Comments comments){
            //заполнение лист вью
            Comment [] comments1 = comments.GetComments();
            for(Comment cmnt: comments1){
                RelativeLayout layout =  new RelativeLayout(getApplicationContext());
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                TextView name = new TextView(getApplicationContext());
                name.setText(cmnt.GetUserName());
                name.setTag(cmnt.GetCommentId());
                name.setLayoutParams(params);
                name.setTextColor(R.color.colorPrimaryDark);
                name.setTypeface(name.getTypeface(), Typeface.BOLD);

                TextView comment = new TextView(getApplicationContext());
                comment.setText(cmnt.GetComment());
                comment.setTextColor(R.color.colorPrimaryDark);

                layout.addView(name);
                layout.addView(comment);

                final String comId = name.getTag().toString();

                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onCommentPress(GetTokenFromDb(getApplicationContext()).GetToken(),comId);
                    }
                });

                scrollView.addView(layout);
            }
        }
    }

    private class sendComment extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... strings) {
            API.Comment(strings[0], strings[1],strings[2]);
            return null;
        }
    }

    private class deleteComment extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... strings) {
            DeleteComment(strings[0],strings[1]);
            return null;
        }
    }

    public void onSendPress(String token, String newsId, String comment){
        if(comment!=""){
            new sendComment().execute(token,newsId);
            Intent intent = new Intent(this, this.getClass());
            intent.putExtra("newsId", newsId);
            intent.putExtra("pageNumber", page);
            finish();
            startActivity(intent);
        }
    }

    public Dialog onCommentPress(final String token, final String commentId){
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Удалить комментарий")
                .setMessage("Вы действительно хотите удалить этот комментарий?")
                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // удаляем комментарий
                        new deleteComment().execute(token,commentId);
                    }
                }).setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
        });
        return builder.create();
    }
}
