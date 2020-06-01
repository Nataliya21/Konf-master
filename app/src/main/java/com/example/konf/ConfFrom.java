package com.example.konf;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.DocumentsContract;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.konf.API.Models.Application.ApplParams;
import com.example.konf.API.Models.Application.ApplicSetting;
import com.example.konf.API.Models.Application.DocList;
import com.example.konf.API.Models.Application.Document;
import com.example.konf.API.Models.User.Params;
import com.example.konf.API.Models.User.RegSetting;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

import static com.example.konf.API.API.EditApplication;
import static com.example.konf.API.API.GetApplSet;
import static com.example.konf.API.API.GetDocList;
import static com.example.konf.API.API.SubmitApplication;
import static com.example.konf.API.DB.GetTokenFromDb;

public class ConfFrom extends AppCompatActivity {

    HorizontalScrollView hsv, hsvSmall;
    ScrollView sv;
    LinearLayout ll, llh;
    ApplicSetting [] applicSettings;
    ApplParams[] applParams;
    DocList[] docLists;
    Document[] documents;
    ArrayList<Document> docs  = new ArrayList<Document>();
    String text, ext, edit, applId;
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf_from);

        Intent i = getIntent();
        edit = i.getStringExtra("Edit");

        if(edit == "true"){
            applId = i.getStringExtra("applId");
        }

        hsv =  findViewById(R.id.hsv);
        hsvSmall = findViewById(R.id.hsvSmall);
        sv = findViewById(R.id.sv);
        llh = findViewById(R.id.llh);
        ll = findViewById(R.id.ll);
        send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edit == "false") {
                    new Submit(GetTokenFromDb(getApplicationContext()).GetToken(), applParams, documents);
                }else{
                   new Edite(GetTokenFromDb(getApplicationContext()).GetToken(),applId,applParams,documents) ;
                }

            }
        });

        new GetAplSet(GetTokenFromDb(getApplicationContext()).GetToken()).execute();

        new GetDocs(GetTokenFromDb(getApplicationContext()).GetToken()).execute();

    }

    protected class Edite extends AsyncTask<Void,Void,Void>{

        String token, applId;
        ApplParams[] applParams;
        Document[] documents;

        public Edite(String token, String applId, ApplParams[] applParams, Document[] documents){
            this.token = token;
            this.applId = applId;
            this.applParams = applParams;
            this.documents = documents;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            EditApplication(token, applId, applParams, documents);
            return null;
        }

        protected void onPostExecute(Void aVoid){
            Intent applics = new Intent(ConfFrom.this, List.class);
            startActivity(applics);
        }
    }

    protected class Submit extends AsyncTask<Void,Void,Void>{

        ApplParams[] applParams;
        Document[] documents;
        String token;

        public Submit(String token, ApplParams[] applParams, Document[] documents){
            this.token = token;
            this.applParams = applParams;
            this.documents = documents;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SubmitApplication(token, applParams,documents);
            return null;
        }

        protected void onPostExecute(Void aVoid){
            Intent applics = new Intent(ConfFrom.this, List.class);
            startActivity(applics);
        }
    }

    protected class GetAplSet extends AsyncTask<Void,Void,Void>{

        String token;

        public GetAplSet(String token){
            this.token = token;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            applicSettings = GetApplSet(token);
            return null;
        }

        protected void onPostExecute(Void aVoid){

                for(final ApplicSetting apl: applicSettings){

                    RelativeLayout layout = new RelativeLayout(getApplicationContext());
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                    ArrayList<ApplParams> testParams = new ArrayList<ApplParams>();
                    final ArrayList<ApplParams> aplParams = new ArrayList<ApplParams>();

                    if(apl.GetR()){
                        TextView name =  new TextView(getApplicationContext());
                        name.setText(apl.GetName()+"*");
                        name.setLayoutParams(params);

                        ApplParams app = new ApplParams(apl.GetId());
                        testParams.add(app);

                        switch(apl.GetType()){
                            case "Text":
                                final EditText value =  new EditText(getApplicationContext());
                                value.setLayoutParams(params);
                                value.setText(apl.GetDefault());
                                value.setTag(apl.GetId());
                                value.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                    @Override
                                    public void onFocusChange(View view, boolean b) {
                                        ApplParams edit = new ApplParams(apl.GetId(), value.getText().toString());
                                        aplParams.add(edit);
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
                                ApplParams edit1 = new ApplParams(apl.GetId(),"false");
                                aplParams.add(edit1);
                                flag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                        if(flag.isChecked()){
                                            ApplParams edit = new ApplParams(apl.GetId(),"true");
                                            aplParams.add(edit);
                                        }else{
                                            ApplParams edit = new ApplParams(apl.GetId(),"false");
                                            aplParams.add(edit);
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
                                String [] values = apl.GetValueToPick();

                                for(String val : values){
                                    final RadioButton rb = new RadioButton(getApplicationContext());
                                    rb.setText(val);
                                    rb.setLayoutParams(params);
                                    rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                            ApplParams edit =  new ApplParams(apl.GetId(), rb.getText().toString());
                                            aplParams.add(edit);
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
                                text.setText(apl.GetDefault());
                                text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                    @Override
                                    public void onFocusChange(View view, boolean b) {
                                        ApplParams edit = new ApplParams(apl.GetId(), text.getText().toString());
                                        aplParams.add(edit);
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
                        name.setText(apl.GetName());
                        name.setLayoutParams(params);

                        switch(apl.GetType()){
                            case "Text":
                                final EditText value =  new EditText(getApplicationContext());
                                value.setLayoutParams(params);
                                value.setText(apl.GetDefault());
                                value.setTag(apl.GetId());
                                value.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                    @Override
                                    public void onFocusChange(View view, boolean b) {
                                        ApplParams edit = new ApplParams(apl.GetId(), value.getText().toString());
                                        aplParams.add(edit);
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
                                ApplParams edit1 = new ApplParams(apl.GetId(), "false");
                                aplParams.add(edit1);
                                flag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                        if(flag.isChecked()){
                                            ApplParams edit = new ApplParams(apl.GetId(),"true");
                                            aplParams.add(edit);
                                        }else{
                                            ApplParams edit = new ApplParams(apl.GetId(),"false");
                                            aplParams.add(edit);
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
                                String [] values = apl.GetValueToPick();

                                for(String val : values){
                                    final RadioButton rb = new RadioButton(getApplicationContext());
                                    rb.setText(val);
                                    rb.setLayoutParams(params);
                                    rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                            ApplParams edit =  new ApplParams(apl.GetId(), rb.getText().toString());
                                            aplParams.add(edit);
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
                                text.setText(apl.GetDefault());
                                text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                    @Override
                                    public void onFocusChange(View view, boolean b) {
                                        ApplParams edit = new ApplParams(apl.GetId(), text.getText().toString());
                                        aplParams.add(edit);
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

                    ArrayList<ApplParams> cleanParams = new ArrayList<ApplParams>();

                    //обязательные элементы
                    for (int i = 0; i < testParams.size() - 1; i++){
                        ApplParams test = testParams.get(i);
                        for(int j = aplParams.size() - 1; j >= 0; j--){
                            ApplParams register = aplParams.get(j);
                            if(register.getId()==test.getId()){
                                cleanParams.add(register);
                                i++;
                            }
                        }
                    }

                    //необязательные элементы
                    for(ApplicSetting reg : applicSettings){
                        if(reg.GetR()==false){
                            for(int j = aplParams.size() - 1; j >= 0; j--){
                                ApplParams register = aplParams.get(j);
                                if(register.getId()==reg.GetId()){
                                    cleanParams.add(register);
                                }
                            }
                        }
                    }

                    applParams = cleanParams.toArray(new ApplParams[cleanParams.size()]);

                    hsv.addView(llh);
                }
            }

    }

    protected class GetDocs extends AsyncTask<Void,Void,Void>{

        String token;

        public GetDocs(String token){
            this.token = token;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            docLists = GetDocList(token);
            return null;
        }

        public void onPostExecute(Void aVoid){

            for(final DocList docList: docLists){
                RelativeLayout layout = new RelativeLayout(getApplicationContext());
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                if(docList.isRequred()){
                    TextView name =  new TextView(getApplicationContext());
                    name.setLayoutParams(layoutParams);
                    String[] arr = docList.GetExtentions();
                    StringBuilder builder = new StringBuilder();
                    for(String i: arr){
                        builder.append(i+",");
                    }
                    name.setText(docList.GetName()+"*" + "["+builder.toString()+"]");
                    name.setTag(docList.GetId());

                    final ImageButton add = new ImageButton(getApplicationContext());
                    add.setImageResource(R.drawable.ic_addDoc);
                    add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onAddPress(docList.GetId());
                            Document test = docs.get(docs.size()-1);
                            int tst =Search(docList.GetExtentions(),test.getExtention());
                            if(tst==0){
                                add.setImageResource(R.drawable.ic_addedDoc);
                                add.setClickable(false);
                            }else{
                                docs.remove(docs.size()-1);
                            }
                        }
                    });

                    layout.addView(name);
                    layout.addView(add);

                    ll.addView(layout);
                    hsvSmall.addView(ll);
                    sv.addView(hsvSmall);
                }else{
                    TextView name =  new TextView(getApplicationContext());
                    name.setLayoutParams(layoutParams);
                    String[] arr = docList.GetExtentions();
                    StringBuilder builder = new StringBuilder();
                    for(String i: arr){
                        builder.append(i+",");
                    }
                    name.setText(docList.GetName() + "["+builder.toString()+"]");
                    name.setTag(docList.GetId());

                    final ImageButton add = new ImageButton(getApplicationContext());
                    add.setImageResource(R.drawable.ic_addDoc);
                    add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onAddPress(docList.GetId());
                            Document test = docs.get(docs.size()-1);
                            int tst =Search(docList.GetExtentions(),test.getExtention());
                            if(tst==0){
                                add.setImageResource(R.drawable.ic_addedDoc);
                                add.setClickable(false);
                            }else{
                                docs.remove(docs.size()-1);
                            }
                        }
                    });

                    layout.addView(name);
                    layout.addView(add);

                    ll.addView(layout);
                    hsvSmall.addView(ll);
                    sv.addView(hsvSmall);
                }
            }
        }

    }

    public void onAddPress(String DocumentTypeId){

        Intent file = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        file.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        file.putExtra("ID", DocumentTypeId);
        startActivityForResult(file, 0);
    }

    public int Search(String[] arr, String element){
        for(String arrElem: arr){
            if(arrElem==element)
                return 0;
        }
        return -1;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onActivityResult(int requestCode, int resultCode, Intent fileReturned) {
        super.onActivityResult(requestCode, resultCode, fileReturned);
        if (resultCode == RESULT_OK) {
            Uri doc = fileReturned.getData();
            try{
                String id = fileReturned.getStringExtra("ID");
                text = readTextFromUri(doc);
                ext = getfileExtension(doc);
                byte[] b = text.getBytes(StandardCharsets.UTF_8);
                String name = new File(fileReturned.getData().getPath()).getName();
                Document dcmt = new Document(id,name,ext,b.toString());
                docs.add(dcmt);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    private String getfileExtension(Uri uri) {
        String extension;
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        extension= mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
        return extension;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String readTextFromUri(Uri uri) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream =
                     getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(Objects.requireNonNull(inputStream)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        return stringBuilder.toString();
    }
}
