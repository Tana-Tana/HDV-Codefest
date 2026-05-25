/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.ex1gxork;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;

import org.json.JSONArray;
import org.json.JSONObject;

public class Ex1GXorK {

    public static void main(String[] args) throws Exception {
        String studentCode = "B22DCCN718";
        String qCode = "ex1GXorK";
        String base = "http://36.50.135.242:2230/api/rest/header";
        HttpClient client = HttpClient.newHttpClient();
        
        String url = base + "?studentCode=" + studentCode + "&qCode=" + qCode;
        HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        
        System.err.println(res.body());
        JSONObject json = new JSONObject(res.body());
        String requestId = json.getString("requestId");
        JSONObject data = json.getJSONObject("data");
        String nonce = data.getString("nonce");
        String signingKey = data.getString("signingKey");
        JSONArray arr = data.getJSONArray("events");
        
        StringBuilder payloadString = new StringBuilder("");
        for(int i =0;i<arr.length();++i){
            payloadString.append(arr.get(i));
            if (i < arr.length() - 1) payloadString.append("|");
        }
        String payload = nonce + ":" + payloadString.toString() + ":" + studentCode.toUpperCase();
        //hmac
        String signature = hmac256(payload, signingKey);
        
        JSONObject submit = new JSONObject();
        submit.put("studentCode", studentCode);
        submit.put("qCode", qCode);
        submit.put("requestId", requestId);
        HttpRequest reqS = HttpRequest.newBuilder(URI.create(base + "/submit"))
                .header("X-Signature", signature)
                .POST(HttpRequest.BodyPublishers.ofString(submit.toString())).build();
        HttpResponse<String> resS = client.send(reqS, HttpResponse.BodyHandlers.ofString());
    }

    private static String hmac256(String data, String key) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec sks = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), data);
        mac.init(sks);
        
        byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for(byte b : hash) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
