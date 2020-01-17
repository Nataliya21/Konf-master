package com.example.konf.API;


import com.example.konf.API.Models.User.RegSetting;
import com.example.konf.API.Models.User.Token;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class API {

    static String baseUrl ="http://127.0.0.1:80"; ///

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

    public static Token GetToken(String login, String password) throws Exception {
        Token token = null;
        URL object = new URL(baseUrl+"/Token");

        try{

            HttpURLConnection con = (HttpURLConnection) object.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("POST");

            JSONObject jsonParams = new JSONObject();

            jsonParams.put("grant_type", "password");
            jsonParams.put("username", login);
            jsonParams.put("password", password);

            OutputStream writer = con.getOutputStream();
            String buffer = jsonParams.toString();
            writer.write(buffer.getBytes("UTF-8"));
            writer.close();

            if(con.getResponseCode() != HttpURLConnection.HTTP_CREATED){
                throw new RuntimeException("Failed: HTTP error code : "
                        + con.getResponseCode());
            }

            InputStream inputStream = new BufferedInputStream(con.getInputStream());
            String result = org.apache.commons.io.IOUtils.toString(inputStream, "UTF-8");
            JSONObject res = new JSONObject(result);

            token = new Token(res);

            inputStream.close();
            con.disconnect();
        }
        catch(Exception e){
            e.printStackTrace();
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
