package com.example.konf.API;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.example.konf.API.Models.Application.ApplParams;
import com.example.konf.API.Models.Application.ApplicInfo;
import com.example.konf.API.Models.Application.ApplicSetting;
import com.example.konf.API.Models.Application.Application;
import com.example.konf.API.Models.Application.DocList;
import com.example.konf.API.Models.Application.Document;
import com.example.konf.API.Models.News.Comments;
import com.example.konf.API.Models.News.News;
import com.example.konf.API.Models.News.NewsArray;
import com.example.konf.API.Models.User.Params;
import com.example.konf.API.Models.User.RegSetting;
import com.example.konf.API.Models.User.Token;
import com.example.konf.API.Models.User.User;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.example.konf.API.DB.SetToken;


public class API {

    static String baseUrl = "http://10.0.2.2:80";

    //User API

    public static Token GetToken(String login, String password, Context context) {

        Token token = null;

        try{
            URL url = new URL(baseUrl+"/Token");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept","application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            JSONObject params = new JSONObject();
            params.put("grant_type","password");
            params.put("username", login);
            params.put("password",password);

            Log.i("JSON",params.toString());
            DataOutputStream os = new DataOutputStream(connection.getOutputStream());
            os.writeBytes(params.toString());

            os.flush();
            os.close();

            Log.i("STATUS",String.valueOf(connection.getResponseCode()));
            Log.i("MSG", connection.getResponseMessage());

            connection.connect();
            int status=connection.getResponseCode();

            switch (status){
                case 200:
                case 201:
                {
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while((line = br.readLine()) != null){
                        sb.append(line);
                    }
                    br.close();
                    JSONObject response = new JSONObject(sb.toString());
                    token = new Token(response);
                    break;
                }
            }
            connection.disconnect();

        }catch(Exception e){
            e.printStackTrace();
        }

        SetToken(token,context);
        return token;
    }

    public static User GetUser(String token) {
        User user = null;

        try{
            URL url = new URL(baseUrl+"/api/Account/UserInfo");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept","application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            Log.i("STATUS",String.valueOf(connection.getResponseCode()));
            Log.i("MSG", connection.getResponseMessage());

            connection.connect();
            int status=connection.getResponseCode();

            switch (status){
                case 200:
                case 201:
                {
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while((line = br.readLine()) != null){
                        sb.append(line);
                    }
                    br.close();
                    JSONObject response = new JSONObject(sb.toString());
                    user = new User(response);
                    break;
                }
            }
            connection.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }

        return user;
    }

    public static RegSetting[] GetRegistSetting(){

        ArrayList<RegSetting> arrayList = new ArrayList<RegSetting>();

        try{
            URL url = new URL(baseUrl+"/api/Account/UserParams");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept","application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            Log.i("STATUS",String.valueOf(connection.getResponseCode()));
            Log.i("MSG", connection.getResponseMessage());

            connection.connect();
            int status=connection.getResponseCode();

            switch (status){
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while((line = br.readLine()) != null){   ///как записать jsonArray
                        sb.append(line);
                    }
                    br.close();
                    JSONArray array = new JSONArray(sb.toString());
                    for(int i = 0; i < array.length(); i++){
                        RegSetting reg = new RegSetting(array.getJSONObject(i));
                        arrayList.add(reg);
                    }

                    break;
                default:

                    break;
            }
            connection.disconnect();

        }catch (Exception e){
            e.printStackTrace();
        }

        return arrayList.toArray(new RegSetting[arrayList.size()]);
    }

    public static boolean ForgotPass (String email){

        boolean rtn= false;
        try{
            URL url = new URL(baseUrl+"/api/Account/ForgotPassword");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept","application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            JSONObject params = new JSONObject();
            params.put("Email",email);

            Log.i("JSON",params.toString());
            DataOutputStream os = new DataOutputStream(connection.getOutputStream());
            os.writeBytes(params.toString());

            os.flush();
            os.close();

            Log.i("STATUS",String.valueOf(connection.getResponseCode()));
            Log.i("MSG", connection.getResponseMessage());

            connection.connect();
            int status=connection.getResponseCode();

            switch (status){
                case 200:
                case 201:
                    rtn = true;
                    break;
                default:
                    rtn = false;
                    break;
            }
            connection.disconnect();

        }catch (Exception e){
            e.printStackTrace();
        }
        return rtn;
    }

    public static boolean ChangePass (String token, String oldPass, String newPass, String confPass){

        boolean rnt = false;
        try{
            URL url =  new URL(baseUrl+"/api/Account/ChangePassword");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept","application/json");
            connection.setRequestProperty("Authorization","Bearer " + token);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            JSONObject params = new JSONObject();
            params.put("OldPassword",oldPass);
            params.put("NewPassword",newPass);
            params.put("ConfirmPassword",confPass);

            Log.i("JSON",params.toString());
            DataOutputStream os = new DataOutputStream(connection.getOutputStream());
            os.writeBytes(params.toString());

            os.flush();
            os.close();

            Log.i("STATUS",String.valueOf(connection.getResponseCode()));
            Log.i("MSG", connection.getResponseMessage());

            connection.connect();
            int status=connection.getResponseCode();

            switch(status){
                case 200:
                case 201:
                    rnt = true;
                    break;
            }


        }catch (Exception e){
            e.printStackTrace();
        }
        return rnt;
    }

    public static boolean ProfileSet (String token, User changedUser, boolean changePic){

        boolean rtn = false;

        try{
            URL url = new URL(baseUrl+"/api/Account/ChangeUserData");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept","application/json");
            connection.setRequestProperty("Authorization","Bearer " + token);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            JSONObject params = new JSONObject();
            params.put("FirstName",changedUser.GetName());
            params.put("SecondName", changedUser.GetSecondName());
            params.put("FathersName",changedUser.GetFatherName());
            params.put("Phone", changedUser.GetPhone());
            params.put("Gender", changedUser.GetGender());
            params.put("BitrhDate", changedUser.GetBirthDate());
            params.put("ProfilePicture", changedUser.GetPic());
            params.put("PictureChanged", changePic);

            JSONArray array = new JSONArray();
            JSONObject obj = new JSONObject();

            for(int i = 0; i < changedUser.GetParams().length; i++){
                obj.put("ParamId", changedUser.GetParams()[i].GetId());
                obj.put("Value", changedUser.GetParams()[i].GetValue());
                array.put(obj.toString());
            }
            params.put("Params", array.toString());

            Log.i("JSON",params.toString());
            DataOutputStream os = new DataOutputStream(connection.getOutputStream());
            os.writeBytes(params.toString());

            os.flush();
            os.close();

            Log.i("STATUS",String.valueOf(connection.getResponseCode()));
            Log.i("MSG", connection.getResponseMessage());

            connection.connect();
            int status=connection.getResponseCode();

            switch (status){
                case 200:
                case 201:
                    rtn = true;
                    break;

                default:
                    rtn = false;
                    break;
            }
            connection.disconnect();


        }catch(Exception e){
            e.printStackTrace();
        }
        return rtn;
    }

    public static void Registration (User newUser, String newPass, String confirmPass){

        try{
            URL url = new URL(baseUrl+"/api/Account/Register");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept","application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            JSONObject params = new JSONObject();
            params.put("Email",newUser.GetEmail());
            params.put("Password", newPass);
            params.put("ConfirmPassword", confirmPass);
            params.put("FirstName", newUser.GetName());
            params.put("SecondName", newUser.GetSecondName());
            params.put("FathersName", newUser.GetFatherName());
            params.put("Phone", newUser.GetPhone());
            params.put("Gender", newUser.GetGender());
            params.put("BitrhDate", newUser.GetBirthDate());
            params.put("ProfilePicture", newUser.GetPic());

            JSONArray array = new JSONArray();
            JSONObject obj = new JSONObject();

            for(int i = 0; i < newUser.GetParams().length; i++){
                obj.put("ParamId", newUser.GetParams()[i].GetId());
                obj.put("Value", newUser.GetParams()[i].GetValue());
                array.put(obj.toString());
            }
            params.put("Params", array.toString());


            Log.i("JSON",params.toString());
            DataOutputStream os = new DataOutputStream(connection.getOutputStream());
            os.writeBytes(params.toString());

            os.flush();
            os.close();

            Log.i("STATUS",String.valueOf(connection.getResponseCode()));
            Log.i("MSG", connection.getResponseMessage());

            connection.connect();

            connection.disconnect();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //Application API

    public static ApplicSetting[] GetApplSet(String token){
        ArrayList<ApplicSetting> arrayList = new ArrayList<ApplicSetting>();

        try{
            URL url = new URL(baseUrl+"/api/Applications/GetApplicationParamsInfo");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept","application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            Log.i("STATUS",String.valueOf(connection.getResponseCode()));
            Log.i("MSG", connection.getResponseMessage());

            connection.connect();
            int status=connection.getResponseCode();

            switch (status){
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while((line = br.readLine()) != null){   ///как записать jsonArray
                        sb.append(line);
                    }
                    br.close();
                    JSONArray array = new JSONArray(sb.toString());
                    for(int i = 0; i < array.length(); i++){
                        ApplicSetting applicSetting = new ApplicSetting(array.getJSONObject(i));
                        arrayList.add(applicSetting);
                    }
                    break;
                default:
                    break;
            }
            connection.disconnect();

        }catch (Exception e){
            e.printStackTrace();
        }

        return arrayList.toArray(new ApplicSetting[arrayList.size()]);
    }

    public static DocList[] GetDocList(String token){
        ArrayList<DocList> arrayList = new ArrayList<DocList>();

        try{
            URL url = new URL(baseUrl+"api/Applications/GetApplicationDocumentsInfo");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept","application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            Log.i("STATUS",String.valueOf(connection.getResponseCode()));
            Log.i("MSG", connection.getResponseMessage());

            connection.connect();
            int status=connection.getResponseCode();

            switch (status){
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while((line = br.readLine()) != null){   ///как записать jsonArray
                        sb.append(line);
                    }
                    br.close();
                    JSONArray array = new JSONArray(sb.toString());
                    for(int i = 0; i < array.length(); i++){
                        DocList docList = new DocList(array.getJSONObject(i));
                        arrayList.add(docList);
                    }
                    break;
                default:
                    break;
            }
            connection.disconnect();

        }catch (Exception e){
            e.printStackTrace();
        }

        return arrayList.toArray(new DocList[arrayList.size()]);
    }

    public static Application[] GetApplication(String token){
        ArrayList<Application> arrayList = new ArrayList<Application>();

        try{
            URL url = new URL(baseUrl+"/api/Applications/GetUserApplications");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept","application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            Log.i("STATUS",String.valueOf(connection.getResponseCode()));
            Log.i("MSG", connection.getResponseMessage());

            connection.connect();
            int status=connection.getResponseCode();

            switch (status){
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while((line = br.readLine()) != null){   ///как записать jsonArray
                        sb.append(line);
                    }
                    br.close();
                    JSONArray array = new JSONArray(sb.toString());
                    for(int i = 0; i < array.length(); i++){
                        Application application = new Application(array.getJSONObject(i));
                        arrayList.add(application);
                    }
                    break;
                default:
                    break;
            }
            connection.disconnect();

        }catch (Exception e){
            e.printStackTrace();
        }

        return arrayList.toArray(new Application[arrayList.size()]);
    }

    public static ApplicInfo GetApplicInfo(String token, String Id){

        ApplicInfo applicInfo  = null;
        try{
            URL url = new URL(baseUrl+"/api/Applications/GetUserApplication?id="+Id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept","application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            Log.i("STATUS",String.valueOf(connection.getResponseCode()));
            Log.i("MSG", connection.getResponseMessage());

            connection.connect();
            int status=connection.getResponseCode();

            switch (status){
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while((line = br.readLine()) != null){
                        sb.append(line);
                    }
                    br.close();
                    JSONObject object = new JSONObject(sb.toString());
                    applicInfo = new ApplicInfo(object);
                    break;
                default:
                    break;
            }
            connection.disconnect();

        }catch (Exception e){
            e.printStackTrace();
        }
        return applicInfo;
    }

    public static String GetDownloadLink(String token, String applicationId, String documentTypeId){
        String link = null;

        try{
            URL url = new URL(baseUrl+"/api/Applications/GetDownloadLink?applicationId="+applicationId+"&documentTypeId="+documentTypeId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept","application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            Log.i("STATUS",String.valueOf(connection.getResponseCode()));
            Log.i("MSG", connection.getResponseMessage());

            connection.connect();
            int status=connection.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();
                    JSONObject jsonObject = new JSONObject(sb.toString());
                    link = jsonObject.getString("DownloadLink");
                    break;
            }
            connection.disconnect();
        } catch(Exception e){
                e.printStackTrace();
        }
        return link;
    }

    public static void SubmitApplication(String token, ApplParams[] params, Document[] documents){
        try{
            URL url = new URL(baseUrl+"/api/Applications/SubmitUserApplication");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept","application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            JSONObject param = new JSONObject();

            JSONArray array = new JSONArray();

            for(int i = 0; i < params.length; i++){
                JSONObject obj = new JSONObject();
                obj.put("ParamId", params[i].getId());
                obj.put("Value", params[i].getValue());
                array.put(obj.toString());
            }
            param.put("Params", array.toString());

            JSONArray array1 = new JSONArray();

            for(int i = 0; i < documents.length; i++){
                JSONObject obj = new JSONObject();
                obj.put("DocumentTypeId", documents[i].getDocumentTypeId());
                obj.put("Name", documents[i].getName());
                obj.put("Extention", documents[i].getExtention());
                obj.put("Data",documents[i].getData());
                array1.put(obj.toString());
            }

            param.put("Documents", array1.toString());

            Log.i("JSON",params.toString());
            DataOutputStream os = new DataOutputStream(connection.getOutputStream());
            os.writeBytes(params.toString());

            os.flush();
            os.close();

            Log.i("STATUS",String.valueOf(connection.getResponseCode()));
            Log.i("MSG", connection.getResponseMessage());

            connection.connect();

            connection.disconnect();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void EditApplication(String token, String ApplicationId, ApplParams[] params, Document[] documents){
        try{
            URL url = new URL(baseUrl+"/api/Applications/EditUserApplication");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept","application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            JSONObject param = new JSONObject();
            param.put("ApplicationId", ApplicationId);

            JSONArray array = new JSONArray();

            for(int i = 0; i < params.length; i++){
                JSONObject obj = new JSONObject();
                obj.put("ParamId", params[i].getId());
                obj.put("Value", params[i].getValue());
                array.put(obj.toString());
            }
            param.put("Params", array.toString());

            JSONArray array1 = new JSONArray();

            for(int i = 0; i < documents.length; i++){
                JSONObject obj = new JSONObject();
                obj.put("DocumentTypeId", documents[i].getDocumentTypeId());
                obj.put("Name", documents[i].getName());
                obj.put("Extention", documents[i].getExtention());
                obj.put("Data",documents[i].getData());
                array1.put(obj.toString());
            }

            param.put("Documents", array1.toString());

            Log.i("JSON",params.toString());
            DataOutputStream os = new DataOutputStream(connection.getOutputStream());
            os.writeBytes(params.toString());

            os.flush();
            os.close();

            Log.i("STATUS",String.valueOf(connection.getResponseCode()));
            Log.i("MSG", connection.getResponseMessage());
            connection.connect();

            connection.disconnect();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void DeleteApplication (String token, String ApplicationId){
        try{
            URL url = new URL(baseUrl+"/api/Applications/MarkApplicationAsDeleted?id="+ApplicationId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept","application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            connection.connect();
            connection.disconnect();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //Convertors

    public static String ConvertToBase64(Bitmap imageBitmap){
        String convert = null;

        try{
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
            byte [] ba = bao.toByteArray();
            convert = Base64.encodeToString(ba,Base64.DEFAULT);
        }catch (Exception e){
            e.printStackTrace();
        }

        return convert;
    }

    public static Bitmap ConvertFromBase64(String convert){
        Bitmap imageBitmap = null;

        byte[] decodedString = Base64.decode(convert, Base64.URL_SAFE);
        imageBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        return imageBitmap;
    }

    //News API

    public static NewsArray GetNewsArray(int pageNumber){
        NewsArray array = null;

        try{
            URL url =  new URL(baseUrl+"/api/News/GetNews?pageNumber="+pageNumber);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept","application/json");
            //connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            Log.i("STATUS",String.valueOf(connection.getResponseCode()));
            Log.i("MSG", connection.getResponseMessage());

            connection.connect();
            int status=connection.getResponseCode();

            switch (status){
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while((line = br.readLine()) != null){
                        sb.append(line);
                    }
                    br.close();
                    JSONObject object = new JSONObject(sb.toString());
                    array = new NewsArray(object);
                    break;
                default:
                    break;
            }
            connection.disconnect();

        }catch (Exception e){
            e.printStackTrace();
        }

        return array;
    }

    public static NewsArray GetNewsArray(String token, int pageNumber){
        NewsArray array = null;

        try{
            URL url =  new URL(baseUrl+"/api/News/GetNews?pageNumber="+pageNumber);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept","application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            Log.i("STATUS",String.valueOf(connection.getResponseCode()));
            Log.i("MSG", connection.getResponseMessage());

            connection.connect();
            int status=connection.getResponseCode();

            switch (status){
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while((line = br.readLine()) != null){
                        sb.append(line);
                    }
                    br.close();
                    JSONObject object = new JSONObject(sb.toString());
                    array = new NewsArray(object);
                    break;
                default:
                    break;
            }
            connection.disconnect();

        }catch (Exception e){
            e.printStackTrace();
        }

        return array;
    }

    public static News GetNews(String newsId){
        News news = null;

        try{
            URL url = new URL(baseUrl+"/api/News/GetNewsPage?id=" + newsId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept","application/json");
            //connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            Log.i("STATUS",String.valueOf(connection.getResponseCode()));
            Log.i("MSG", connection.getResponseMessage());

            connection.connect();
            int status=connection.getResponseCode();

            switch (status){
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while((line = br.readLine()) != null){
                        sb.append(line);
                    }
                    br.close();
                    JSONObject object = new JSONObject(sb.toString());
                    news = new News(object);
                    break;
                default:
                    break;
            }
            connection.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }
        return news;
    }

    public static News GetNews(String token, String newsId){
        News news = null;

        try{
            URL url = new URL(baseUrl+"/api/News/GetNewsPage?id=" + newsId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept","application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            Log.i("STATUS",String.valueOf(connection.getResponseCode()));
            Log.i("MSG", connection.getResponseMessage());

            connection.connect();
            int status=connection.getResponseCode();

            switch (status){
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while((line = br.readLine()) != null){
                        sb.append(line);
                    }
                    br.close();
                    JSONObject object = new JSONObject(sb.toString());
                    news = new News(object);
                    break;
                default:
                    break;
            }
            connection.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }
        return news;
    }

    public static Comments GetCommentsPage(String pageNumber, String newsId){
        Comments comments = null;

        try{
            URL url = new URL(baseUrl + "/api/News/GetCommentsForPage?pageNumber=" + pageNumber + "&newsId=" + newsId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept","application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            Log.i("STATUS",String.valueOf(connection.getResponseCode()));
            Log.i("MSG", connection.getResponseMessage());

            connection.connect();
            int status=connection.getResponseCode();

            switch (status){
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while((line = br.readLine()) != null){
                        sb.append(line);
                    }
                    br.close();
                    JSONObject object = new JSONObject(sb.toString());
                    comments = new Comments(object);
                    break;
                default:
                    break;
            }
            connection.disconnect();

        }catch (Exception e){
            e.printStackTrace();
        }

        return comments;
    }

    public static void Comment(String token, String newsId){

        try{
            URL url = new URL(baseUrl+"/api/News/Comment");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept","application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            Log.i("STATUS",String.valueOf(connection.getResponseCode()));
            Log.i("MSG", connection.getResponseMessage());

            connection.connect();

            connection.disconnect();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void DeleteComment(String token, String commentId){

        try{
            URL url = new URL(baseUrl+"/api/News/RemoveComment?commentId=" + commentId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept","application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            Log.i("STATUS",String.valueOf(connection.getResponseCode()));
            Log.i("MSG", connection.getResponseMessage());

            connection.connect();
            connection.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void Like(String token, String newsId){

        try{
            URL url = new URL(baseUrl+"/api/News/Like?pageId=" + newsId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept","application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            Log.i("STATUS",String.valueOf(connection.getResponseCode()));
            Log.i("MSG", connection.getResponseMessage());

            connection.connect();
            connection.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void DisLike(String token, String newsId){

        try{
            URL url = new URL(baseUrl+"/api/News/Dislike?pageId=" + newsId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept","application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            Log.i("STATUS",String.valueOf(connection.getResponseCode()));
            Log.i("MSG", connection.getResponseMessage());

            connection.connect();
            connection.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
