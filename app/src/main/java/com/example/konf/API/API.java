package com.example.konf.API;

import android.content.Context;

import com.example.konf.API.Models.User.RegSetting;
import com.example.konf.API.Models.User.Token;
import com.example.konf.API.Models.User.User;
import com.example.konf.Enter;
import com.example.konf.Profile;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.example.konf.API.DB.GetTokenFromDb;
import static com.example.konf.API.DB.SetToken;


public class API {

    static String baseUrl = "http://10.0.2.2:80"; ///

    public static User GetUser(String token) {
        User user = null;

        try{
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(baseUrl+"/api/Account/UserInfo");
            request.addHeader("content-type","application/x-www-form-urlencoded");
            request.addHeader("Authorization","Bearer "+token);

            HttpResponse response = httpClient.execute(request);
            JSONObject resp = null;
            resp = new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8"));

            if(response.getStatusLine().getStatusCode()==200){
                user = new User(resp);
            }
            else{
                //getStatusCode
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return user;
    }

    public static Token GetToken(String login, String password, Context context) {

        Token token = null;

        try{
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(baseUrl+"/Token");
            request.addHeader("content-type","application/x-www-form-urlencoded");

            StringEntity params = new StringEntity("grant_type=password&username="+login+"&password="+password, "UTF-8");

            request.setEntity(params);

            HttpResponse response = httpClient.execute(request);
            JSONObject resp = null;
            resp = new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8"));

            if(response.getStatusLine().getStatusCode()==200){
                token = new Token(resp);
            }
            else{
                //alert with status
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        SetToken(token,context);
        return token;
    }

    public static RegSetting[] GetRegistSetting(){

        ArrayList<RegSetting> arrayList = new ArrayList<RegSetting>();

        try{
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(baseUrl+"/api/Account/UserParams");
            request.addHeader("content-type","application/x-www-form-urlencoded");

            HttpResponse response = httpClient.execute(request);
            JSONArray array = null;
            array = new JSONArray(EntityUtils.toString(response.getEntity(), "UTF-8"));

            if(response.getStatusLine().getStatusCode()==200){
                for(int i = 0; i < array.length(); i++){
                    RegSetting reg = new RegSetting(array.getJSONObject(i));
                    arrayList.add(reg);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return arrayList.toArray(new RegSetting[arrayList.size()]);
    }

    public static boolean ForgotPass (String email){

        try{
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(baseUrl+"/api/Account/ForgotPassword");
            request.addHeader("content-type","application/x-www-form-urlencoded");

            StringEntity param = new StringEntity("Email="+email, "UTF-8");
            request.setEntity(param);

            HttpResponse response = httpClient.execute(request);
            //JSONObject object = null;
            //object = new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8"));

            if(response.getStatusLine().getStatusCode()==200){
                return true;
            }
            else return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean ChangePass (String token, String oldPass, String newPass, String confPass){

        try{
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(baseUrl+"/api/Account/ChangePassword");
            request.addHeader("content-type","application/x-www-form-urlencoded");
            request.addHeader("Authorization", "Bearer " + token);

            StringEntity param = new StringEntity("OldPassword="+oldPass+"&NewPassword="+newPass+"&ConfirmPassword="+confPass, "UTF-8");

            request.setEntity(param);

            HttpResponse response = httpClient.execute(request);
            if(response.getStatusLine().getStatusCode()==200){
                return true;
            }
            else return false;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean ProfileSet (String token, User changedUser){

        try{

            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(baseUrl+"/api/Account/ChangeUserData");
            request.addHeader("content-type","application/x-www-form-urlencoded");
            request.addHeader("Authorization", "Bearer "+token);

            //
        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }

}
