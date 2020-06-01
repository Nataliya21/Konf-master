package com.example.konf;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.konf.API.Models.User.Params;
import com.example.konf.API.Models.User.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.konf.API.API.ConvertFromBase64;
import static com.example.konf.API.API.ConvertToBase64;
import static com.example.konf.API.API.GetUser;
import static com.example.konf.API.API.ProfileSet;
import static com.example.konf.API.DB.GetTokenFromDb;

public class ProfileSetting extends AppCompatActivity {

    Button save, pass;
    ImageButton foto;
    Boolean flag  = false;
    EditText name, surname, fathername, email, phone, date;
    RadioGroup rg;
    RadioButton m,f;
    ImageView profilePic, getDate;
    HorizontalScrollView hsv;
    LinearLayout llh;
    Calendar date1 = Calendar.getInstance();
    Params [] params;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);

        name = findViewById(R.id.Name);
        surname = findViewById(R.id.Surname);
        fathername = findViewById(R.id.FatherName);
        email =  findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        date = findViewById(R.id.date);

        rg = findViewById(R.id.radioGroup2);
        m = findViewById(R.id.m);
        m.setTag("m");
        f = findViewById(R.id.f);
        f.setTag("f");

        profilePic = findViewById(R.id.foto);

        hsv = findViewById(R.id.hsv);
        llh = findViewById(R.id.llh);

        save = findViewById(R.id.save);
        pass = findViewById(R.id.pass);
        foto = findViewById(R.id.takeFoto);
        getDate =  findViewById(R.id.getDate);

        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pass = new Intent(ProfileSetting.this, Forgot_password.class);
                startActivity(pass);
            }
        });

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = true;
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
            }
        });

        getDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate();//???
            }
        });

        final User chngd = GetChangedUser();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSavePress(GetTokenFromDb(getApplicationContext()).GetToken(), chngd, flag);
            }
        });

        try{
            new FillUserInfo(GetTokenFromDb(getApplicationContext()).GetToken()).execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (resultCode == RESULT_OK) {
            Uri selectedImage = imageReturnedIntent.getData();
            final ImageView img = (ImageView) findViewById(R.id.foto);
            img.setImageURI(selectedImage);
        }
    }

    public void onSavePress(String token, User changed, Boolean changedPick){
        new ChangedUser(token, changed, changedPick).execute();
        Intent profile = new Intent(ProfileSetting.this, Profile.class);
        startActivity(profile);
    }

    public void setDate(){

        new DatePickerDialog(ProfileSetting.this, dateSetListener,
                date1.get(Calendar.YEAR),
                date1.get(Calendar.MONTH),
                date1.get(Calendar.DAY_OF_MONTH))
                .show();

    }
    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener(){
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            date1.set(Calendar.YEAR, year);
            date1.set(Calendar.MONTH, monthOfYear);
            date1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };

    private void setInitialDateTime() {

        date.setText(DateUtils.formatDateTime(this,
                date1.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
                        | DateUtils.FORMAT_SHOW_TIME));
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        date.setTag(sdf.format(date1));
    }

    public User GetChangedUser(){
        User user = null;
        BitmapDrawable drawable = (BitmapDrawable) profilePic.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        int Id = rg.getCheckedRadioButtonId();

        RadioButton btn = findViewById(Id);
        String gender = btn.getTag().toString();

        user = new User(email.getText().toString(),name.getText().toString(), surname.getText().toString(),
         fathername.getText().toString(),phone.getText().toString(),gender, params,
                ConvertToBase64(bitmap), date.getText().toString());

        return user;
    }

    protected class ChangedUser extends AsyncTask<Void, Void, Void>{

        String token;
        User user;
        Boolean flag;

        public ChangedUser(String token, User user, Boolean flag){
            this.token = token;
            this.user = user;
            this.flag = flag;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Boolean rtrn = ProfileSet(token,user,flag);
            return null;
        }
    }

    protected class FillUserInfo extends AsyncTask<Void, Void,Void>{

        String token;
        User user;

        public FillUserInfo(String token){
            this.token = token;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            user = GetUser(token);
            return null;
        }

        protected void onPostExecute(Void aVoid){
            name.setText(user.GetName());
            surname.setText(user.GetSecondName());
            fathername.setText(user.GetFatherName());
            email.setText(user.GetEmail());
            phone.setText(user.GetPhone());
            date.setText(user.GetBirthDate());
            if(user.GetGender().toLowerCase()=="мужской"){
                rg.check(R.id.m);
            }else{
                rg.check(R.id.f);
            }
            Bitmap btmp = ConvertFromBase64(user.GetPic());
            foto.setImageBitmap(btmp);

            params = user.GetParams();
            for(Params param: params){

                RelativeLayout layout = new RelativeLayout(getApplicationContext());
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                TextView name =  new TextView(getApplicationContext());
                name.setText(param.GetName());
                name.setTag(param.GetId());
                name.setTextColor(Color.BLACK);
                name.setLayoutParams(layoutParams);

                TextView value = new TextView(getApplicationContext());
                value.setText(param.GetValue());
                value.setTextColor(Color.BLACK);
                value.setLayoutParams(layoutParams);

                layout.addView(name);
                layout.addView(value);

                llh.addView(layout);
                hsv.addView(llh);
            }

        }
    }
}
