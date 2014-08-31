package com.hackaton.ne4istb.antifastfood;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ne4istb on 31.08.2014.
 */
public class HttpHelper {

    public static JSONObject HttpGet(String urlString) {

        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpGet httpGet = new HttpGet(urlString);

        JSONObject result = null;

        try {
            HttpResponse response = httpClient.execute(httpGet, localContext);

            HttpEntity entity = response.getEntity();
            String jsonString = EntityUtils.toString(entity);
            result = new JSONObject(jsonString);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return result;
    }
}
