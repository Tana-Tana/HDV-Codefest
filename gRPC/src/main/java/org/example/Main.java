package org.example;

import GRPC.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        String studenCode = "B22DCCN718";
        String qCode = "JFFjElgo";
        String host = "36.50.135.242";
        int port = 2240;
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        JudgeServiceGrpc.JudgeServiceBlockingStub stub = JudgeServiceGrpc.newBlockingStub(channel);

        JudgeRequest req = JudgeRequest.newBuilder().setStudentCode(studenCode).setQuestionAlias(qCode).build();
        JudgeResponse res = stub.request(req);

        String requestId = res.getRequestId();
        String data = res.getData();
        String[] arr = data.split(",");
        Arrays.sort(arr, String.CASE_INSENSITIVE_ORDER);
        String ans = String.join(",", arr);

        SubmitRequest submitRequest = SubmitRequest.newBuilder().setRequestId(requestId).setStudentCode(studenCode).setQuestionAlias(qCode).setAnswer(ans).build();
        SubmitResponse ress = stub.submit(submitRequest);

    }

    private static double round2(double number) {
        return BigDecimal.valueOf(number).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}