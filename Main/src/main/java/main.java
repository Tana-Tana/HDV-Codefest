import GRPC.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class main {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        int port = 2240;
        String studentCode = "B22DCCN718";
        String qCode = "PYBuBWOc";

        ManagedChannel channel = ManagedChannelBuilder.forAddress(host,port).usePlaintext().build();
        TypedJudgeServiceGrpc.TypedJudgeServiceBlockingStub stub = TypedJudgeServiceGrpc.newBlockingStub(channel);

        // cal
        TypedJudgeRequest req = TypedJudgeRequest.newBuilder().setStudentCode(studentCode).setQuestionAlias(qCode).build();
        TypedJudgeResponse res = stub.requestTyped(req);
        String requestId = res.getRequestId();

        EnrollmentData data = res.getEnrollment();
        List<String> completeds = data.getCompletedCoursesList();
        List<String> requireds = data.getRequiredCoursesList();
        List<String> missings = new ArrayList<>();

        for (String s1 : requireds) {
            if (!completeds.contains(s1)) {
                missings.add(s1);
            }
        }

        Collections.sort(missings);
        double gpa_gap = data.getMinGpa() - data.getGpa();
        gpa_gap = round2(gpa_gap);
        if (gpa_gap < 0) gpa_gap = 0;

        boolean eligible = (gpa_gap != 0 && missings.isEmpty());

        EnrollmentAnswer answer = EnrollmentAnswer.newBuilder().setEligible(eligible).setGpaGap(gpa_gap).addAllMissingCourses(missings).build();
        TypedSubmitRequest request = TypedSubmitRequest.newBuilder().setStudentCode(studentCode).setQuestionAlias(qCode)
                .setRequestId(requestId).setEnrollmentAnswer(answer).build();
        TypedSubmitResponse resSS = stub.submitTyped(request);
    }

    private static double round2(double data) {
        return BigDecimal.valueOf(data).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
