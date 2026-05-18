/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.gyr0ptmr;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;


public class GYR0ptMR {

    public static void main(String[] args) throws Exception {
        String studentCode = "B22DCCN718";
        String qCode = "gYR0ptMR";
        String base = "http://36.50.135.242:2230/api/rest/data";
        HttpClient client = HttpClient.newHttpClient();
        
        // lay data
        String url = base + "?studentCode="  + studentCode + "&qCode=" + qCode;
        HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        String resData = res.body();
        //System.out.println(resData);
        
        // parse json
        JSONObject json = new JSONObject(resData);
        String requestID = json.getString("requestId");
        JSONArray array = json.getJSONArray("data");
        
        // cal
        long sum = 0;
        for (int i = 0; i < array.length(); i++) {
            sum += array.getInt(i);
        }
        
        // submit
        JSONObject submit = new JSONObject();
        submit.put("studentCode", studentCode);
        submit.put("qCode", qCode);
        submit.put("requestId", requestID);
        submit.put("answer", sum);
        
        HttpRequest subReq = HttpRequest.newBuilder(URI.create(base + "/submit"))
                .POST(HttpRequest.BodyPublishers.ofString(submit.toString())).build();
        HttpResponse<String> subRes = client.send(subReq, HttpResponse.BodyHandlers.ofString());
        
        System.out.println(subRes.body());
        
    }
}
