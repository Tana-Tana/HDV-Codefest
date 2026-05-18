/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.cui5pkpr;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.RoundingMode;

public class CUi5pKpr {

    public static void main(String[] args) throws IOException, InterruptedException {
        String studentCode = "B22DCCN718";
        String qCode = "CUi5pKpr";
        String base = "http://36.50.135.242:2230/api/rest/data";
        HttpClient client = HttpClient.newHttpClient();
        
        // laydata
        String url = base + "?studentCode=" + studentCode + "&qCode=" + qCode;
        HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        String resData = res.body();
        System.out.println(resData);
        
        // parse JSON
        JSONObject json = new JSONObject(resData);
        String requestID = json.getString("requestId");
        JSONArray array = json.getJSONArray("data");
        
       // cal
       double capturedTotal = 0;
       double refundedTotal = 0;
       int failedCount = 0;
       
       for (int i=0;i<array.length();i++) {
           JSONObject object = array.getJSONObject(i);
           double amount = object.getDouble("amount");
           String status = object.getString("status");
           if (status.equals("CAPTURED")) capturedTotal+= amount;
           if (status.equals("REFUNDED")) refundedTotal+= amount;
           if (status.equals("FAILED")) failedCount++;
       }
       double netTotal = capturedTotal - refundedTotal;
       
       capturedTotal = round2(capturedTotal);
       refundedTotal = round2(refundedTotal);
       netTotal = round2(netTotal);
       // submit
       JSONObject submit = new JSONObject();
       submit.put("studentCode", studentCode);
       submit.put("qCode", qCode);
       submit.put("requestId", requestID);
       
       JSONObject ketqua = new JSONObject();
       ketqua.put("capturedTotal", capturedTotal);
       ketqua.put("refundedTotal", refundedTotal);
       ketqua.put("netTotal", netTotal);
       ketqua.put("failedCount", failedCount);
       
       submit.put("answer", ketqua);
       
       HttpRequest subReq = HttpRequest.newBuilder(URI.create(base + "/submit")).POST(HttpRequest.BodyPublishers.ofString(submit.toString())).build();
       HttpResponse<String> subRes = client.send(subReq, HttpResponse.BodyHandlers.ofString());
        System.err.println(subRes.body());
    }

    private static double round2(double val) {
        return BigDecimal.valueOf(val).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
