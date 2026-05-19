/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.w1abelsu;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
        import java.net.http.HttpRequest;
                import java.net.http.HttpResponse;
import org.json.JSONObject;
public class W1abELSU {

    public static void main(String[] args) throws Exception {
        String studentCode = "B22DCCN718";
        String qCode = "w1abELSU";
        String base = "http://36.50.135.242:2230/api/rest/method";
        HttpClient client = HttpClient.newHttpClient();
        
        String url = base + "?studentCode="+ studentCode + "&qCode=" + qCode;
        HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        
        JSONObject json = new JSONObject(res.body());
        JSONObject submit = new JSONObject();
        submit.put("studentCode", studentCode);
        submit.put("qCode", qCode);
        
        JSONObject ketqua = new JSONObject();
        ketqua.put("status", "ACTIVE");
        ketqua.put("activatedBy", studentCode);
        ketqua.put("auditNote", "manual-review-ok");
        submit.put("answer", ketqua);
        
        HttpRequest reqS = HttpRequest.newBuilder(URI.create(base + "/" + json.getString("requestId"))).PUT(HttpRequest.BodyPublishers.ofString(submit.toString())).build();
        HttpResponse<String> resS = client.send(reqS, HttpResponse.BodyHandlers.ofString());
        System.err.println(resS.body());
    }
}
