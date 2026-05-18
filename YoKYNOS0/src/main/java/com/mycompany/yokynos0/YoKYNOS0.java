/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.yokynos0;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import org.json.JSONObject;

public class YoKYNOS0 {

    public static void main(String[] args) throws Exception {
        String studentCode = "B22DCCN718";
        String qCode = "YoKYNOS0";
        String base = "http://36.50.135.242:2230/api/rest/character";
        HttpClient client = HttpClient.newHttpClient();
        
        // laydata
        String url = base + "?studentCode=" + studentCode + "&qCode=" + qCode;
        HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        String data = res.body();
        
        // parse
        JSONObject json = new JSONObject(data);
        String requestID = json.getString("requestId");
        String string = json.getString("data");
        
        // cal
        String[] s = string.split("\\s+");
        Arrays.sort(s);
        String ketqua = String.join(" ", s);
        
        // submit
        JSONObject submit = new JSONObject();
        submit.put("studentCode", studentCode);
        submit.put("qCode", qCode);
        submit.put("requestId", requestID);
        submit.put("answer", ketqua);
        
        HttpRequest reqS = HttpRequest.newBuilder(URI.create(base + "/submit")).POST(HttpRequest.BodyPublishers.ofString(submit.toString())).build();
        HttpResponse<String> resS = client.send(reqS, HttpResponse.BodyHandlers.ofString());
        System.out.println(resS.body());
    }
}
