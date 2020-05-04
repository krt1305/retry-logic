package retryLogic;

import com.nurkiewicz.asyncretry.AsyncRetryExecutor;
import com.nurkiewicz.asyncretry.RetryExecutor;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class GenericListener implements IInvokedMethodListener {

    public void afterInvocation(IInvokedMethod invokedMethod, ITestResult result) {
        // nothing to do
        if (invokedMethod.isTestMethod()) {
            if (!result.isSuccess()) {
                System.out.println("Throwable " + result.getThrowable().toString());
                String exceptionReason = result.getThrowable().toString();


                if (result.getThrowable() instanceof UnknownHostException) {
                    System.out.println("Instance of UnknownHostException ");
                }
                if (result.getThrowable() instanceof ConnectException) {
                    System.out.println("Instance of ConnectException ");
                    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
                    RetryExecutor executor = new AsyncRetryExecutor(scheduler).
                            retryOn(ConnectException.class).
                            withExponentialBackoff(500, 2).
                            withMaxDelay(2000).
                            withUniformJitter().
                            withMaxRetries(2);
                } else {
                    //retry
                    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
                    RetryExecutor executor = new AsyncRetryExecutor(scheduler).
                            retryOn(SocketException.class).
                            withExponentialBackoff(500, 2).
                            withMaxDelay(2000).
                            withUniformJitter().
                            withMaxRetries(2);
                }



            }
        }

    }


    public void beforeInvocation(IInvokedMethod invokedMethod, ITestResult result) {

      /*  Issue issue = invokedMethod.getTestMethod()
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
    }*/
    }
}
