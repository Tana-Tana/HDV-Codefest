package org.example;

import GRPC.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws Exception {
        String host = "36.50.135.242";
        int port = 2240;
        String msv = "B22DCCN718";
        String qCode = "egupkEdU";

        // plaintext
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        TypedJudgeServiceGrpc.TypedJudgeServiceBlockingStub stub = TypedJudgeServiceGrpc.newBlockingStub(channel);

        //req
        TypedJudgeRequest req = TypedJudgeRequest.newBuilder().setStudentCode(msv).setQuestionAlias(qCode).build();
        TypedJudgeResponse res = stub.requestTyped(req);

        String requestId = res.getRequestId();
        GRPC.TransactionRiskBatchData batch = res.getTransactionRiskBatch();
        double total_high_risk_amount = 0;
        List<String> ids = new ArrayList<>();
        for (TransactionRecord t : batch.getTransactionsList()) {
            if (t.getAmount() >= 5000 || t.getChargebackCount() >= 2 || t.getNewDevice() && t.getCountry() != "VN") {
                ids.add(t.getTransactionId());
                total_high_risk_amount+= t.getAmount();
            }
        }

        total_high_risk_amount = LamTron2So(total_high_risk_amount);
        TransactionRiskAnswer answer = TransactionRiskAnswer.newBuilder().addAllHighRiskTransactionIds(ids).setReviewCount(ids.size()).setTotalHighRiskAmount(total_high_risk_amount).build();
        TypedSubmitRequest submitRequest = TypedSubmitRequest.newBuilder().setRequestId(requestId).setStudentCode(msv).setQuestionAlias(qCode).setTransactionRiskAnswer(answer).build();
        TypedSubmitResponse submitResponse = stub.submitTyped(submitRequest);
    }

    private static double LamTron2So(double number) {
        return BigDecimal.valueOf(number).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}