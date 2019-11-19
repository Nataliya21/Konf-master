package com.example.konf.API;


import com.example.konf.API.Models.User.RegSetting;

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

import java.util.ArrayList;


public class API {

    static String baseUrl ="https://10.0.2.2:44375";

    public  static void GetUser (String token) throws Exception{


        HttpClient httpClient = null;
        try
        {
            SchemeRegistry Current_Scheme = new SchemeRegistry();
            Current_Scheme.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            Current_Scheme.register(new Scheme("https", new Naive_SSLSocketFactory(), 443));
            HttpParams Current_Params = new BasicHttpParams();
            ThreadSafeClientConnManager Current_Manager = new ThreadSafeClientConnManager(Current_Params,Current_Scheme);
            httpClient = new DefaultHttpClient(Current_Manager, Current_Params);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        try {
            HttpPost request =  new HttpPost(baseUrl +  "/api/Account/UserInfo" );//задаем URL
            request.addHeader("content-type", "application/json; charset=UTF-8"); //задаем content-type, очень важно!
            request.addHeader("Authorization","Bearer " + token);

            HttpResponse response = httpClient.execute(request);

            JSONObject resp =  new JSONObject( EntityUtils.toString(response.getEntity(), " UTF-8"));

            switch (response.getStatusLine().getStatusCode()){
                case 200:
                    if( !resp.getBoolean("error")){
                       // result =  ParsePoll(resp.getJSONObject("poll"));
                        //вызов конструктора
                    } else {

                        throw new Exception("Ошибка выполнения запроса");
                    }

                    break;

                default:
                    throw new Exception("Ошибка выполнения запроса");
            }

        }
        catch(Exception ex) {
            throw new Exception("Ошибка выполнения запроса");
        }

    }

    public static String GetToken(String login, String password) throws Exception {
        String token;

        HttpClient httpClient = null;
        try
        {
            SchemeRegistry Current_Scheme = new SchemeRegistry();
            Current_Scheme.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            Current_Scheme.register(new Scheme("https", new Naive_SSLSocketFactory(), 443));
            HttpParams Current_Params = new BasicHttpParams();
            ThreadSafeClientConnManager Current_Manager = new ThreadSafeClientConnManager(Current_Params,Current_Scheme);
            httpClient = new DefaultHttpClient(Current_Manager, Current_Params);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        try {
            HttpPost request = new HttpPost(baseUrl + "/Token");//задаем URL
            request.addHeader("content-type", "application/json; charset=UTF-8"); //задаем content-type, очень важно!

            JSONObject jsonParams = new JSONObject();//Создаем и заполняем объект параметров
            jsonParams.put("grant_type", "password");
            jsonParams.put("username", login);
            jsonParams.put("password", password);

            StringEntity params = new StringEntity(jsonParams.toString());
            request.setEntity(params);
            //request.setHeader("data", jsonParams.toString());????

            HttpResponse response = httpClient.execute(request);
            JSONObject resp = null;
            resp = new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8"));


        switch (response.getStatusLine().getStatusCode()) {
            case 200:
                if (!resp.getBoolean("error")) {
                    token = resp.getString("access_token");

                } else {

                    throw new Exception("Ошибка выполнения запроса");
                }

                break;

            default:
                throw new Exception("Ошибка выполнения запроса");

            }
        }
        catch(Exception ex){
            throw new Exception("Ошибка выполнения запроса");
        }
        return token;
    }

    public static RegSetting[] GetRegistSetting() throws Exception {

        ArrayList<RegSetting> arrayList = new ArrayList<RegSetting>();

        HttpClient httpClient = null;
        try
        {
            SchemeRegistry Current_Scheme = new SchemeRegistry();
            Current_Scheme.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            Current_Scheme.register(new Scheme("https", new Naive_SSLSocketFactory(), 443));
            HttpParams Current_Params = new BasicHttpParams();
            ThreadSafeClientConnManager Current_Manager = new ThreadSafeClientConnManager(Current_Params,Current_Scheme);
            httpClient = new DefaultHttpClient(Current_Manager, Current_Params);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        try {
            //посмотреть правильно ли делается запрос у меня, потом попробовать разобраться с ервером и бд

            HttpPost request =  new HttpPost("https://10.0.2.2:44375/api/Account/UserParams" );//задаем URL
            request.addHeader("content-type", "application/json; charset=UTF-8"); //задаем content-type, очень важно!

            HttpResponse response = httpClient.execute(request);

            JSONObject resp =  new JSONObject( EntityUtils.toString(response.getEntity(), " UTF-8"));

            switch (response.getStatusLine().getStatusCode()){
                case 200:
                    if( !resp.getBoolean("error")){
                        JSONArray array = new JSONArray(resp.toString());
                        for(int i = 0; i < array.length(); i++){
                            JSONObject jsonObject = array.getJSONObject(i);
                            RegSetting rg = new RegSetting(jsonObject);

                            arrayList.add(rg);
                        }


                    } else {

                        throw new Exception("Ошибка выполнения запроса");
                    }

                    break;

                default:
                    throw new Exception("Ошибка выполнения запроса");
            }

        }
        catch(Exception ex) {
            throw new Exception("Ошибка выполнения запроса");
        }

        return arrayList.toArray( new RegSetting[arrayList.size()]);
    }
}
