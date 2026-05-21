/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.bhiqtmgu;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

public class BHiQtMgu {

    public static void main(String[] args) throws Exception {
        String studentCode ="B22DCCN718";
        String qCode = "bHiQtMgu";
        String base = "http://36.50.135.242:2230/api/rest/path";
        HttpClient client = HttpClient.newHttpClient();
        
        String url = base + "?studentCode=" + studentCode + "&qCode=" + qCode;
        HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        System.err.println(res.body());
        
        JSONObject json = new JSONObject(res.body());
        String requestId = json.getString("requestId");
        JSONArray arr = json.getJSONArray("data");
        int invoiceId = arr.getJSONObject(0).getInt("id");
        
        HttpRequest reqS = HttpRequest.newBuilder(URI.create(base + "/" + invoiceId + "?studentCode=" + studentCode + "&qCode=" + qCode + "&requestId=" + requestId + "&currency=USD")).GET().build();
        HttpResponse<String> resS = client.send(reqS, HttpResponse.BodyHandlers.ofString());
        System.err.println(resS.body());
    }
}
