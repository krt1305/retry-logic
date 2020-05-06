import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Predicates;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import ninjaCore.Retryer;
import ninjaCore.RetryerBuilder;
import ninjaCore.StopStrategies;
import org.testng.*;
import skipDefects.Issue;
import skipDefects.IssueStatus;
import skipDefects.IssueTrackerUtil;
import waitStrategy.WaitStrategies;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Retry1 implements IRetryAnalyzer, IInvokedMethodListener {

    int retryAttempt = 0;
    int retryLimit = 2;
    WaitStrategies waitStrategies;
    StopStrategies stopStrategies;
    int noOfFailedAttempts=0;
    static List<String> abortConditions= new ArrayList<String>();

    static
    {
        abortConditions.add("IllegalStateException");
        abortConditions.add("IOException");
        abortConditions.add("IllegalStateException");

    }


    public boolean retry(ITestResult result) {

        Instant start = Instant.now();
        System.out.println("Retry starts at ------" + start);
        Throwable cause = result.getThrowable();
        System.out.println("Cause is " + cause);
            System.out.println("Was result retried for Retry Attempt " + retryAttempt + " --" + result.wasRetried());

        if (cause instanceof ConnectException) {
            System.out.println("Result was retried " + result.wasRetried());
            //retry forever
            /*if (!result.isSuccess()) {
                System.out.println("Retry attempt is " + retryAttempt);
                retryAttempt++;
                return true;
            }*/
            //healthcheck api and then retry
          /*  if (!isServiceUp("http://localhost:8082/actuator/health")) {
                //define wait strategy and stop strategy
                System.out.println("Retry attempt ---" + retryAttempt);
                waitStrategies.randomWait(5, TimeUnit.SECONDS);
                retryAttempt++;
                return true;
            }*/

        } else if (cause instanceof UnknownHostException) {
            System.out.println("UnknownHostException --skipping");
            result.setWasRetried(false);
            return false;
        } /*else if (cause instanceof AssertionError) {
            result.setWasRetried(false);
            System.out.println("Assertion failure---> Not retrying");
            return false;

        } */ else {

                if (retryAttempt > 0) {
                    System.out.println("Total No of failed attempts for -"+result.getMethod().getMethodName()+"---"+noOfFailedAttempts);
                    System.out.println("Retry attempt is greater than 0 , doing a random wait");
                    Instant startTimeBeforeRandomWait = Instant.now();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Retryer<Boolean> retryer = RetryerBuilder.<Boolean>newBuilder()
                            .withWaitStrategy(ninjaCore.WaitStrategies.fixedWait(50L, TimeUnit.MILLISECONDS))
                            .retryIfResult(Predicates.<Boolean>isNull())
                            .build();



                    Instant endTime = Instant.now();
                    System.out.println("Total random wait time " + Duration.between(startTimeBeforeRandomWait, endTime).toSeconds());
                }

                if (retryAttempt < retryLimit) {
                    System.out.println("Retry attempt ---" + retryAttempt);
                    retryAttempt++;
                    noOfFailedAttempts++;
                    // result.setWasRetried(true);
                    return true;

                }
        }
        Instant end = Instant.now();
        System.out.println("End is " + end);
        long timeElapsed = Duration.between(start, end).toSeconds();
        System.out.println("Total time taken for 3 retries " + timeElapsed);

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
