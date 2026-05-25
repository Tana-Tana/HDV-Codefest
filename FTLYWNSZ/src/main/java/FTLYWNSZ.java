/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

import GRPC.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.json.JSONObject;

public class FTLYWNSZ {

    public static void main(String[] args) throws Exception{
        String host = "36.50.135.242";
        int port = 2240;
        String student_code = "B22DCCN718";
        String question_alias = "FTLYWNSZ";
        
        // tao plantext channel
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        JudgeServiceGrpc.JudgeServiceBlockingStub stub = JudgeServiceGrpc.newBlockingStub(channel);

        //request
        JudgeRequest req = JudgeRequest.newBuilder().setStudentCode(student_code).setQuestionAlias(question_alias).build();
        JudgeResponse res = stub.request(req);
        String requestId = res.getRequestId();
        String data = res.getData();

        System.out.println(requestId + " " + data);

        // cal
        String[] arr = data.split(",");
        long sum = 0;
        for (String s : arr) {
            sum += Integer.parseInt(s);
        }

        // submit
        SubmitRequest submitRequest = SubmitRequest.newBuilder().setRequestId(requestId).setQuestionAlias(question_alias).setStudentCode(student_code).setAnswer(String.valueOf(sum)).build();
        SubmitResponse submitResponse = stub.submit(submitRequest);
    }
}
