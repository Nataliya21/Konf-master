package com.example.konf.API;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class API {

    public  static void GetUser (String baseUrl, String token) throws Exception{


        HttpClient httpClient = HttpClientBuilder.create().build();

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

    public static String GetToken(String baseUrl, String login, String password) throws Exception {
        String token = "";

        HttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost request = new HttpPost(baseUrl + "/Token");//задаем URL
            request.addHeader("content-type", "application/json; charset=UTF-8"); //задаем content-type, очень важно!

            JSONObject jsonParams = new JSONObject();//Создаем и заполняем объект параметров
            jsonParams.put("grant_type", "password");
            jsonParams.put("username", login);
            jsonParams.put("password", password);

            StringEntity params = new StringEntity(jsonParams.toString());
            request.setEntity(params);
            //request.setHeader("data", jsonParams.toString());

            HttpResponse response = httpClient.execute(request);
            JSONObject resp = null;
            resp = new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8"));


        switch (response.getStatusLine().getStatusCode()) {
            case 200:
                if (!resp.getBoolean("error")) {
                    JSONObject data = resp.getJSONObject("resp");
                    token = data.getString("access_token");
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
}
