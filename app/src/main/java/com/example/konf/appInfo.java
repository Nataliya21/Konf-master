package com.example.konf;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.konf.API.Models.Application.ApplParams;
import com.example.konf.API.Models.Application.ApplicInfo;
import com.example.konf.API.Models.Application.Complaints;

import static com.example.konf.API.API.GetApplicInfo;
import static com.example.konf.API.API.GetDownloadLink;
import static com.example.konf.API.DB.GetTokenFromDb;

public class appInfo extends AppCompatActivity {

    String applId;
    ApplicInfo info;
    String downloadLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);

        Intent i = getIntent();
        applId = i.getStringExtra("appId");

        new Info().execute();
    }

    public void onButtonPress(String applId){
        Intent edit= new Intent(appInfo.this, ConfFrom.class);
        edit.putExtra("Edit", "true");
        edit.putExtra("applId", applId);
        startActivity(edit);
    }

    public void onLinkPress(String token, String applId, String docId){
        new GetLink(token, applId, docId).execute();
        //open link in browser
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(downloadLink));
        startActivity(browserIntent);
    }

    protected class GetLink extends AsyncTask<Void,Void,Void>{

        String token, applId,docId;

        public GetLink(String token, String applId, String docId){
            this.token = token;
            this.applId = applId;
            this.docId = docId;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            downloadLink = GetDownloadLink(token, applId,docId);
            return null;
        }
    }

    protected class Info extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            info = GetApplicInfo(GetTokenFromDb(getApplicationContext()).GetToken(),applId);
            return null;
        }

        protected void onPostExecute(Void aVoid){
            final TextView stat = findViewById(R.id.Status);
            stat.setText(stat.getText().toString() +" " +info.GetStatus());

            TextView time = findViewById(R.id.Time);
            time.setText(time.getText().toString()+" "+info.GetTime());

            ScrollView sv2 = findViewById(R.id.sv2);
            HorizontalScrollView hsv = findViewById(R.id.hsv);
            LinearLayout ll1 = findViewById(R.id.llh);

            String[] docs = info.GetDocs();
            for(final String dc: docs){
                RelativeLayout layout = new RelativeLayout(getApplicationContext());
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                layout.setLayoutParams(layoutParams);

                RelativeLayout.LayoutParams layoutParamsLeft = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                layoutParamsLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                final TextView id = new TextView(layout.getContext());
                id.setText(dc);
                id.setLayoutParams(layoutParamsLeft);

                RelativeLayout.LayoutParams layoutParamsRight = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                layoutParamsRight.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                ImageButton link = new ImageButton(layout.getContext());
                link.setImageResource(R.drawable.ic_download);
                link.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onLinkPress(GetTokenFromDb(getApplicationContext()).GetToken(), applId, id.getText().toString());
                    }
                });
                link.setLayoutParams(layoutParamsRight);

                layout.addView(id);
                layout.addView(link);

                ll1.addView(layout);
            }
            hsv.addView(ll1);
            sv2.addView(hsv);

            ApplParams[] params = info.GetParams();
            ScrollView sv1 = findViewById(R.id.sv1);
            LinearLayout ll = findViewById(R.id.ll1);
            for(ApplParams appl: params){
                RelativeLayout layout = new RelativeLayout(getApplicationContext());
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                layout.setLayoutParams(layoutParams);

                TextView id = new TextView(getApplicationContext());
                id.setText(appl.getId());
                id.setLayoutParams(layoutParams);

                TextView value = new TextView(getApplicationContext());
                value.setText(appl.getValue());
                value.setLayoutParams(layoutParams);

                layout.addView(id);
                layout.addView(value);

                ll.addView(layout);
            }
            sv1.addView(ll);

            Complaints[] complaints = info.GetCompl();
            ScrollView sv3 = findViewById(R.id.sv3);
            LinearLayout ll3 =  findViewById(R.id.ll3);
            for(Complaints cmp: complaints){
                RelativeLayout layout = new RelativeLayout(getApplicationContext());
                RelativeLayout.LayoutParams paramsLeft = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                paramsLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                RelativeLayout.LayoutParams paramsRight = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                paramsRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                TextView compl = new TextView(getApplicationContext());
                compl.setText(cmp.GetCompl());
                compl.setLayoutParams(paramsLeft);

                TextView date = new TextView(getApplicationContext());
                date.setText(cmp.GetDate());
                date.setLayoutParams(paramsRight);

                layout.addView(compl);
                layout.addView(date);

                ll3.addView(layout);
            }
            sv3.addView(ll3);

            Button edit = findViewById(R.id.EditAppl);
            if(info.isEditable()){
                edit.setEnabled(true);
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent edit = new Intent(appInfo.this, ConfFrom.class);
                        edit.putExtra("Edit", "true");
                        edit.putExtra("applId", applId);
                        startActivity(edit);
                    }
                });
            }else{
                edit.setEnabled(false);
            }
        }
    }
}
