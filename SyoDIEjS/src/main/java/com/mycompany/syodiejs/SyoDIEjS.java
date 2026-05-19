/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.syodiejs;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;

public class SyoDIEjS {

    public static void main(String[] args) throws Exception {
        String student = "B22DCCN718";
        String qCode = "SyoDIEjS";
        String base = "http://36.50.135.242:2230/api/rest/character";
        HttpClient client = HttpClient.newHttpClient();
        
        String url = base + "?studentCode="  +student
                + "&qCode=" + qCode;
        HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        String resData = res.body();
        System.err.println(res.body());
        JSONObject json = new JSONObject(resData);
        String requestId = json.getString("requestId");
        String string = json.getString("data");
        
        // 
        //System.err.println(string);
        String[] arr = string.split(" ");
        for(int i =0;i<arr.length;++i) {
            if (arr[i].contains("user=")) arr[i] = "user=[EMAIL]";
            if (arr[i].contains("phone=")) arr[i] = "phone=[PHONE]";
            if (arr[i].contains("token=")) arr[i] = "token=[TOKEN]";
        }
        
        String ketqua = String.join(" ", arr);
        System.out.println(ketqua);
        JSONObject submit = new JSONObject();
        submit.put("studentCode", student);
        submit.put("qCode", qCode);
        submit.put("requestId", requestId);
        submit.put("answer", ketqua);
        HttpRequest reqS = HttpRequest.newBuilder(URI.create(base + "/submit")).POST(HttpRequest.BodyPublishers.ofString(submit.toString())).build();
        HttpResponse<String> resS = client.send(reqS, HttpResponse.BodyHandlers.ofString());
        System.out.println(resS.body());
    }
}
