package com.example.konf;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import com.example.konf.API.API;
import com.example.konf.API.Models.User.Params;
import com.example.konf.API.Models.User.RegSetting;
import com.example.konf.API.Models.User.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.example.konf.API.API.ConvertToBase64;
import static com.example.konf.API.API.GetRegistSetting;
import static com.example.konf.API.DB.DeleteDataFromDB;

public class Registration extends AppCompatActivity {

    RegSetting[] registerSetting;
    HorizontalScrollView hsv;
    LinearLayout llh;
    Params[] reg;
    Button save;
    CheckBox ok;
    EditText  name, surname, fathername, phone, email, pass, confirm, date;
    RadioGroup rg;
    ImageButton getFoto, getDate;
    ImageView profilePic;
    Calendar date1 = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        hsv = findViewById(R.id.hsv);
        llh = findViewById(R.id.llh);
        getFoto = findViewById(R.id.getFoto);
        getFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
            }
        });
        getDate = findViewById(R.id.getDate);
        getDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate();
            }
        });
        save = findViewById(R.id.save);

        ok = findViewById(R.id.cb);
        name =  findViewById(R.id.Name);
        surname = findViewById(R.id.Surname);
        fathername = findViewById(R.id.FatherName);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        confirm = findViewById(R.id.confirm);
        date = findViewById(R.id.date);
        rg = findViewById(R.id.radioGroup2);
        profilePic = findViewById(R.id.foto);

        BitmapDrawable drawable = (BitmapDrawable) profilePic.getDrawable();
        Bitmap btmp = drawable.getBitmap();

        final User newUser;

        if(rg.getCheckedRadioButtonId()==R.id.m){
             newUser = new User(email.getText().toString(), name.getText().toString(), surname.getText().toString(),
             fathername.getText().toString(),phone.getText().toString(),"m", reg, ConvertToBase64(btmp),date.getTag().toString());
        }else
             newUser = new User(email.getText().toString(), name.getText().toString(), surname.getText().toString(),
             fathername.getText().toString(),phone.getText().toString(),"f", reg, ConvertToBase64(btmp),date.getTag().toString());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ok.isChecked()){
                    onSavePress(newUser, pass.getText().toString(), confirm.getText().toString());
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
                    builder.setTitle("Внимание!").
                            setMessage("Вы не дали свое согласие на обработку ваших данных!").
                            setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    onPause();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });

        new GetRegisterSet().execute();
    }

    public void onSavePress(User user, String password, String confirm){
        new RegisterUser(user, password,confirm).execute();
    }

    public void setDate(){

        new DatePickerDialog(Registration.this, dateSetListener,
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

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (resultCode == RESULT_OK) {
            Uri selectedImage = imageReturnedIntent.getData();
            final ImageView img = (ImageView) findViewById(R.id.foto);
            img.setImageURI(selectedImage);
        }
    }

    protected class GetRegisterSet extends AsyncTask<Void, Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            registerSetting = GetRegistSetting();
            return null;
        }

        protected void onPostExecute(Void aVoid){

            for(final RegSetting regSetting: registerSetting){

                RelativeLayout layout = new RelativeLayout(getApplicationContext());
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                ArrayList<Params> testParams = new ArrayList<Params>();
                final ArrayList<Params> regParams = new ArrayList<Params>();

                if(regSetting.GetR()){
                    TextView name =  new TextView(getApplicationContext());
                    name.setText(regSetting.GetName()+"*");
                    name.setLayoutParams(params);

                    Params req = new Params(regSetting.GetId(), regSetting.GetName());
                    testParams.add(req);

                    switch(regSetting.GetType()){
                        case "Text":
                            final EditText value =  new EditText(getApplicationContext());
                            value.setLayoutParams(params);
                            value.setText(regSetting.GetDefault());
                            value.setTag(regSetting.GetId());
                            value.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                @Override
                                public void onFocusChange(View view, boolean b) {
                                    Params edit = new Params(regSetting.GetId(), regSetting.GetName(), value.getText().toString());
                                    regParams.add(edit);
                                }
                            });

                            layout.addView(name);
                            layout.addView(value);

                            llh.addView(layout);
                            hsv.addView(llh);
                            break;
                        case "Flag":
                            final CheckBox flag = new CheckBox(getApplicationContext());
                            flag.setLayoutParams(params);
                            flag.setChecked(false);
                            Params edit1 = new Params(regSetting.GetId(), regSetting.GetName(),"false");
                            regParams.add(edit1);
                            flag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                    if(flag.isChecked()){
                                        Params edit = new Params(regSetting.GetId(), regSetting.GetName(),"true");
                                        regParams.add(edit);
                                    }else{
                                        Params edit = new Params(regSetting.GetId(), regSetting.GetName(),"false");
                                        regParams.add(edit);
                                    }
                                }
                            });

                            layout.addView(name);
                            layout.addView(flag);

                            llh.addView(layout);
                            hsv.addView(llh);
                            break;
                        case "Select":
                            RadioGroup radioGroup = new RadioGroup(getApplicationContext());
                            radioGroup.setLayoutParams(params);
                            String [] values = regSetting.GetValueToPick();

                            for(String val : values){
                                final RadioButton rb = new RadioButton(getApplicationContext());
                                rb.setText(val);
                                rb.setLayoutParams(params);
                                rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                        Params edit =  new Params(regSetting.GetId(), regSetting.GetName(), rb.getText().toString());
                                        regParams.add(edit);
                                    }
                                });
                                radioGroup.addView(rb);
                            }

                            layout.addView(name);
                            layout.addView(radioGroup);

                            break;
                        case "Big Text":
                            final TextView text = new TextView(getApplicationContext());
                            text.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                            text.setLayoutParams(params);
                            text.setText(regSetting.GetDefault());
                            text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                @Override
                                public void onFocusChange(View view, boolean b) {
                                    Params edit = new Params(regSetting.GetId(), regSetting.GetName(), text.getText().toString());
                                    regParams.add(edit);
                                }
                            });

                            layout.addView(name);
                            layout.addView(text);

                            llh.addView(layout);
                            hsv.addView(llh);
                            break;
                        default:
                            break;
                    }


                }else{
                    TextView name =  new TextView(getApplicationContext());
                    name.setText(regSetting.GetName());
                    name.setLayoutParams(params);

                    switch(regSetting.GetType()){
                        case "Text":
                            final EditText value =  new EditText(getApplicationContext());
                            value.setLayoutParams(params);
                            value.setText(regSetting.GetDefault());
                            value.setTag(regSetting.GetId());
                            value.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                @Override
                                public void onFocusChange(View view, boolean b) {
                                    Params edit = new Params(regSetting.GetId(), regSetting.GetName(), value.getText().toString());
                                    regParams.add(edit);
                                }
                            });

                            layout.addView(name);
                            layout.addView(value);

                            llh.addView(layout);
                            hsv.addView(llh);
                            break;
                        case "Flag":
                            final CheckBox flag = new CheckBox(getApplicationContext());
                            flag.setLayoutParams(params);
                            flag.setChecked(false);
                            Params edit1 = new Params(regSetting.GetId(), regSetting.GetName(),"false");
                            regParams.add(edit1);
                            flag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                    if(flag.isChecked()){
                                        Params edit = new Params(regSetting.GetId(), regSetting.GetName(),"true");
                                        regParams.add(edit);
                                    }else{
                                        Params edit = new Params(regSetting.GetId(), regSetting.GetName(),"false");
                                        regParams.add(edit);
                                    }
                                }
                            });

                            layout.addView(name);
                            layout.addView(flag);

                            llh.addView(layout);
                            hsv.addView(llh);
                            break;
                        case "Select":
                            RadioGroup radioGroup = new RadioGroup(getApplicationContext());
                            radioGroup.setLayoutParams(params);
                            String [] values = regSetting.GetValueToPick();

                            for(String val : values){
                                final RadioButton rb = new RadioButton(getApplicationContext());
                                rb.setText(val);
                                rb.setLayoutParams(params);
                                rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                        Params edit =  new Params(regSetting.GetId(), regSetting.GetName(), rb.getText().toString());
                                        regParams.add(edit);
                                    }
                                });
                                radioGroup.addView(rb);
                            }

                            layout.addView(name);
                            layout.addView(radioGroup);

                            break;
                        case "Big Text":
                            final TextView text = new TextView(getApplicationContext());
                            text.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                            text.setLayoutParams(params);
                            text.setText(regSetting.GetDefault());
                            text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                @Override
                                public void onFocusChange(View view, boolean b) {
                                    Params edit = new Params(regSetting.GetId(), regSetting.GetName(), text.getText().toString());
                                    regParams.add(edit);
                                }
                            });

                            layout.addView(name);
                            layout.addView(text);

                            llh.addView(layout);
                            hsv.addView(llh);
                            break;
                        default:
                            break;
                    }
                }

                ArrayList<Params> cleanParams = new ArrayList<Params>();

                //обязательные элементы
                for (int i = 0; i < testParams.size() - 1; i++){
                    Params test = testParams.get(i);
                    for(int j = regParams.size() - 1; j >= 0; j--){
                        Params register = regParams.get(j);
                        if(register.GetId()==test.GetId()){
                            cleanParams.add(register);
                            i++;
                        }
                    }
                }

                //необязательные элементы
                for(RegSetting reg : registerSetting){
                    if(reg.GetR()==false){
                        for(int j = regParams.size() - 1; j >= 0; j--){
                            Params register = regParams.get(j);
                            if(register.GetId()==reg.GetId()){
                                cleanParams.add(register);
                            }
                        }
                    }
                }

                reg = cleanParams.toArray(new Params[cleanParams.size()]);

                hsv.addView(llh);
            }
        }
    }

    protected class RegisterUser extends AsyncTask<Void,Void,Void>{

        User user;
        String password, confirm;

        public RegisterUser(User user, String password, String confirm){
            this.user = user;
            this.password = password;
            this.confirm = confirm;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            API.Registration(user, password, confirm);
            return null;
        }
    }
}
