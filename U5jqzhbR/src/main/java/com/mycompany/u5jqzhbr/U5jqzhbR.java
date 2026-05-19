/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.u5jqzhbr;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;
        
public class U5jqzhbR {

    public static void main(String[] args) throws Exception {
        String studentCode = "B22DCCN718";
        String qCode = "U5jqzhbR";
        String base = "http://36.50.135.242:2230/api/rest/method";
        HttpClient client = HttpClient.newHttpClient();
        
        String url = base + "?studentCode=" + studentCode + "&qCode=" + qCode;
        HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        String resData = res.body();
        JSONObject json = new JSONObject(resData);
        String requestId = json.getString("requestId");
        
        JSONObject submit = new JSONObject();
        submit.put("studentCode", studentCode);
        submit.put("qCode", qCode);
        JSONObject ketqua = new JSONObject();
        ketqua.put("status", "done");
        submit.put("answer", ketqua);
        
        HttpRequest reqS = HttpRequest.newBuilder(URI.create(base + "/" + requestId)).PUT(HttpRequest.BodyPublishers.ofString(submit.toString())).build();
        HttpResponse<String> resS = client.send(reqS, HttpResponse.BodyHandlers.ofString());
        System.err.println(resS.body());
    }
}
