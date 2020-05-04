import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import myLogic.waitLogic.WaitStrategies;
import org.testng.*;
import skipDefects.Issue;
import skipDefects.IssueStatus;
import skipDefects.IssueTrackerUtil;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

public class Retry1 implements IRetryAnalyzer, IInvokedMethodListener {

    int retryAttempt = 0;
    int retryLimit = 4;
    WaitStrategies waitStrategies;


    public boolean retry(ITestResult result) {
        String host = result.getHost();
        System.out.println("Host is " + result.getHost());
        System.out.println("In retry utilty latest ");
        Throwable cause = result.getThrowable();
        System.out.println("Cause is " + cause);
        if (cause instanceof ConnectException) {
            System.out.println("Result was retried " + result.wasRetried());
            //retry forever
            /*if (!result.isSuccess()) {
                System.out.println("Retry attempt is " + retryAttempt);
                retryAttempt++;
                return true;
            }*/
            //healthcheck api and then retry
            if (!isServiceUp("http://localhost:8082/actuator/health")) {
                //define wait strategy and stop strategy
                System.out.println("Retry attempt ---" + retryAttempt);
                waitStrategies.randomWait(5, TimeUnit.SECONDS);
                retryAttempt++;
                return true;
            }

        }
        if (cause instanceof UnknownHostException) {
            System.out.println("UnknownHostException --skipping");
            result.setWasRetried(false);
            return false;
        } else {
            System.out.println("Retrying 3 times");
            if (retryAttempt < retryLimit) {
                retryAttempt++;
                return true;
            }
        }

        return false;
    }


    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        System.out.println("In before invocation");

    }


    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        System.out.println("In after invocation");
    }


    public void beforeInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
        Issue issue = method.getTestMethod()
                .getConstructorOrMethod()
                .getMethod()
                .getAnnotation(Issue.class);

        if (null != issue) {
            if (IssueStatus.OPEN.equals(IssueTrackerUtil.getStatus(issue.value()))) {
                System.out.println("Skipping " + testResult.getName() + " due to Open Defect - " + issue.value());
                throw new SkipException("Skipping this due to Open Defect - " + issue.value());
            }
            if (IssueStatus.CLOSED.equals(IssueTrackerUtil.getStatus(issue.value()))) {
                System.out.println("Skipping " + testResult.getName() + " due to Open Defect - " + issue.value());
                throw new SkipException("Skipping this due to Open Defect - " + issue.value());
            }
        }

    }


    public void afterInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {

    }

    public boolean isServiceUp(String host) {
        RequestSpecification httpRequest = RestAssured.given();
        //Makes calls to the server using Method type.
        Response response = httpRequest.request(Method.GET);
        //Checks the Status Code
        int statusCode = response.getStatusCode();
        String respAsString = response.body().asString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonPath jsonPathEvaluator = response.jsonPath();
        String healthStatus = jsonPathEvaluator.get("status");
        if (healthStatus.equalsIgnoreCase("up"))
            return true;
        else
            return false;
    }


}
