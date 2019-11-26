package com.example.konf;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ProfileSetting extends AppCompatActivity {

    public Button save, pass;
    public ImageButton foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);

        save = findViewById(R.id.save);
        pass = findViewById(R.id.pass);
        foto = findViewById(R.id.takeFoto);

        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //сменить пароль
                Intent pass = new Intent(ProfileSetting.this, Forgot_password.class);
                startActivity(pass);
            }
        });

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //сохранить изменения
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (resultCode == RESULT_OK) {
            Uri selectedImage = imageReturnedIntent.getData();
            final ImageView img = (ImageView) findViewById(R.id.foto);
            img.setImageURI(selectedImage);

        }
    }
}
