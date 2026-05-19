/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.aq3sk1zs;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
        import java.net.http.HttpRequest;
                import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
public class AQ3sK1zs {

    public static void main(String[] args) throws Exception {
        String studentCode = "B22DCCN718";
        String qCode = "aQ3sK1zs";
        String base = "http://36.50.135.242:2230/api/rest/path";
        HttpClient client = HttpClient.newHttpClient();
        
        String url = base + "?studentCode=" + studentCode + "&qCode=" + qCode;
        HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        JSONObject json = new JSONObject(res.body());
        String requestId = json.getString("requestId");
        JSONArray arr = json.getJSONArray("data");
        int id = (arr.getJSONObject(0)).getInt("id");
        
        HttpRequest reqS = HttpRequest.newBuilder(URI.create(base + "/" + String.valueOf(id) + "?studentCode=" + studentCode + "&qCode=" + qCode + "&requestId=" + requestId + "&currency=USD")).GET().build();
        HttpResponse<String> resS = client.send(reqS, HttpResponse.BodyHandlers.ofString());
        System.err.println(resS.body());
    }
}
