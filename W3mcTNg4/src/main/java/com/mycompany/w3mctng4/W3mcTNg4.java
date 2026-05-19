/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.w3mctng4;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;

public class W3mcTNg4 {

    public static void main(String[] args) throws Exception {
        String studentCode = "B22DCCN718";
        String qCode = "W3mcTNg4";
        String base = "http://36.50.135.242:2230/api/rest/header";
        HttpClient client = HttpClient.newHttpClient();
        String url = base + "?studentCode="  +studentCode
                + "&qCode=" + qCode;
        HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        JSONObject json = new JSONObject(res.body());
        String XChecksum = res.headers().firstValue("X-Checksum").orElse("");
        
        JSONObject submit = new JSONObject();
        submit.put("studentCode", studentCode);
        submit.put("qCode", qCode);
        submit.put("requestId", json.get("requestId"));
        HttpRequest reqS = HttpRequest.newBuilder(URI.create(base + "/submit")).header("X-Checksum", XChecksum).POST(HttpRequest.BodyPublishers.ofString(submit.toString())).build();
        
        HttpResponse<String> resS = client.send(reqS, HttpResponse.BodyHandlers.ofString());
        System.err.println(resS.body());
    }
}
