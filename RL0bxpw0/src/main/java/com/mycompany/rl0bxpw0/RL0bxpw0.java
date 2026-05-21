/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.rl0bxpw0;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

public class RL0bxpw0 {

    public static void main(String[] args) throws Exception {
        String studentCode = "B22DCCN718";
        String qCode = "RL0bxpw0";
        String base = "http://36.50.135.242:2230/api/rest/path";
        HttpClient client = HttpClient.newHttpClient();
        
        String url = base + "?studentCode=" + studentCode + "&qCode=" + qCode;
        HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        
        JSONObject json = new JSONObject(res.body());
        String requestId = json.getString("requestId");
        JSONArray arr = json.getJSONArray("data");
        
        long overdueAmount = 0;
        String customerId = "";
        int page = 0;
        for (int i =0;i<arr.length();++i){
            JSONObject tmp = arr.getJSONObject(i);
            if (tmp.getString("status").equals("OVERDUE") && overdueAmount < tmp.getLong("overdueAmount")) {
                overdueAmount = tmp.getLong("overdueAmount");
                customerId = tmp.getString("customerId");
                page = tmp.getInt("page");
            }
        }
        
        HttpRequest reqS = HttpRequest.newBuilder(URI.create(base + "/" + customerId + "?studentCode=" + studentCode + "&qCode=" + qCode + "&requestId=" + requestId + "&status=OVERDUE&page=" + page)).GET().build();
        HttpResponse<String> resS = client.send(reqS, HttpResponse.BodyHandlers.ofString());
        
    }
}
