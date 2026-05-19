/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.frrfrci6;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

public class FrrFRcI6 {

    public static void main(String[] args) throws Exception {
        String studentCode = "B22DCCN718";
        String qCode = "FrrFRcI6";
        String base = "http://36.50.135.242:2230/api/rest/object";
        HttpClient client = HttpClient.newHttpClient();
        
        String url = base + "?studentCode=" + studentCode + "&qCode=" + qCode;
        HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        String resData = res.body();
        System.err.println(resData);
        JSONObject json = new JSONObject(resData);
        String requestId = json.getString("requestId");
        
        JSONObject json1 = json.getJSONObject("data");
        int maxEtaDays = json1.getInt("maxEtaDays");
        double weightKg = json1.getDouble("weightKg");
        JSONArray arrJson = json1.getJSONArray("quotes");
        
        String carrier = "";
        double totalFee = 1000000;
        int etaDays = 0;
        double reliTmp = 0;
        
        //solve
        for (int i=0;i<arrJson.length();++i) {
            JSONObject jo = arrJson.getJSONObject(i);
            
            int eta = jo.getInt("etaDays");
            if (eta <= maxEtaDays) {
                double baseFee = jo.getDouble("baseFee");
                double perKgFee = jo.getDouble("perKgFee");
                double tmpTotalFee = baseFee + weightKg * perKgFee;
                if (totalFee > tmpTotalFee) {
                    totalFee = tmpTotalFee;
                    carrier = jo.getString("carrier");
                    reliTmp = jo.getDouble("reliability");
                    etaDays = eta;
                }
                else if (totalFee == tmpTotalFee) {
                    if (jo.getDouble("reliability") > reliTmp) {
                        totalFee = tmpTotalFee;
                        carrier = jo.getString("carrier");
                        reliTmp = jo.getDouble("reliability");
                        etaDays = eta;
                    }
                }
            }
        }
        totalFee = round2(totalFee);
        //
        JSONObject kq = new JSONObject();
        kq.put("carrier", carrier);
        kq.put("totalFee", totalFee);
        kq.put("etaDays",  etaDays);
        //
        JSONObject submit = new JSONObject();
        submit.put("studentCode", studentCode);
        submit.put("qCode", qCode);
        submit.put("requestId", requestId);
        submit.put("answer", kq);
        
        // 
        HttpRequest reqS = HttpRequest.newBuilder(URI.create(base + "/submit")).POST(HttpRequest.BodyPublishers.ofString(submit.toString())).build();
        HttpResponse<String> resS = client.send(reqS, HttpResponse.BodyHandlers.ofString());
        System.err.println(resS.body());
    }

    private static double round2(double totalFee) {
        return BigDecimal.valueOf(totalFee).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
