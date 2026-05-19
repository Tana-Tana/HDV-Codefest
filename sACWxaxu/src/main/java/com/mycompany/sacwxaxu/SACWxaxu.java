/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.sacwxaxu;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;

public class SACWxaxu {

    public static void main(String[] args) throws Exception {
        String studentCode = "B22DCCN718";
        String qCode = "sACWxaxu";
        String base = "http://36.50.135.242:2230/api/rest/method";
        HttpClient client = HttpClient.newHttpClient();
        
        String url = base + "?studentCode=" + studentCode + "&qCode=" + qCode;
        HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        
        JSONObject json = new JSONObject(res.body());
        System.err.println(res.body());
        JSONObject data = json.getJSONObject("data");
        
        JSONObject submit = new JSONObject();
        submit.put("studentCode", studentCode);
        submit.put("qCode", qCode);
        JSONObject submit1 = new JSONObject();
        submit1.put("status", "RESOLVED");
        submit.put("answer", submit1);
        
        HttpRequest reqS = HttpRequest.newBuilder(URI.create(base + "/" + json.getString("requestId")))
                .header("If-Match", data.getString("etag")).method("PATCH", HttpRequest.BodyPublishers.ofString(submit.toString())).build();
        HttpResponse<String> resS = client.send(reqS, HttpResponse.BodyHandlers.ofString());
        System.err.println(resS.body());
    }
}
