import org.testng.*;
import skipDefects.Issue;
import skipDefects.IssueStatus;
import skipDefects.IssueTrackerUtil;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.sql.SQLOutput;

public class RetryUtilityLatest implements IRetryAnalyzer , IInvokedMethodListener {

    int retryAttempt = 0;
    int retryLimit = 4;

    @Override
    public boolean retry(ITestResult result) {

         /*if(retryAttempt < retryLimit)
            {
                retryAttempt++;
                return true;
            }*/
        System.out.println("Host is "+ result.getHost());
        //isOpenDefect(result);
        System.out.println("In retry utilty latest ");
        Throwable cause = result.getThrowable();
        System.out.println("Cause is " + cause);
        if (cause instanceof ConnectException) {
            System.out.println("Result was retried " + result.wasRetried());
            if (!result.isSuccess()) {
                System.out.println("Retry attempt is " + retryAttempt);
                retryAttempt++;
                return true;
            }
        }
        if (cause instanceof UnknownHostException) {
            System.out.println("UnknownHostException --skipping");
            result.setWasRetried(false);
            return false;
        } else {
            System.out.println("Doesnt match any exception...not retrying");
        }

        return false;
    }

    @Override
    public void beforeInvocation(IInvokedMethod invokedMethod, ITestResult result) {
        System.out.println("Checking if test is a defect");

        Issue issue = invokedMethod.getTestMethod()
                .getConstructorOrMethod()
                .getMethod()
                .getAnnotation(Issue.class);

        if (null != issue) {
            if (IssueStatus.OPEN.equals(IssueTrackerUtil.getStatus(issue.value()))) {
                throw new SkipException("Skipping this due to Open Defect - " + issue.value());
            }
            if (IssueStatus.CLOSED.equals(IssueTrackerUtil.getStatus(issue.value()))) {
                System.out.println("Skipping test");
                throw new SkipException("Skipping this due to Open Defect - " + issue.value());
            }
        }
    }


}
