/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.rwfip8ha;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;


public class RwFip8hA {

    public static void main(String[] args) throws Exception {
        String student = "B22DCCN718";
        String qCode = "RwFip8hA";
        String base = "http://36.50.135.242:2230/api/rest/object";
        HttpClient client = HttpClient.newHttpClient();
        
        String url = base + "?studentCode=" + student + "&qCode=" + qCode;
        HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        String resData = res.body();
        System.err.println(resData);
        
        JSONObject json = new JSONObject(resData);
        String requestId = json.getString("requestId");
        JSONObject object = json.getJSONObject("data");
        double price = object.getDouble("price");
        double taxRate = object.getDouble("taxRate");
        double discount = object.getDouble("discount");
        double finalPrice = price*(1 + taxRate / 100)*(1 - discount / 100);
        json.put("finalPrice", finalPrice);
        JSONObject submit = new JSONObject();
        submit.put("studentCode", student);
        submit.put("qCode", qCode);
        submit.put("requestId", requestId);
        submit.put("answer", json);
        
        HttpRequest reqS = HttpRequest.newBuilder(URI.create(base + "/submit")).POST(HttpRequest.BodyPublishers.ofString(submit.toString())).build();
        HttpResponse<String> resS = client.send(reqS, HttpResponse.BodyHandlers.ofString());
        System.err.println(resS.body());
    }
}
