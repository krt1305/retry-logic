import com.nurkiewicz.asyncretry.AsyncRetryExecutor;
import com.nurkiewicz.asyncretry.RetryExecutor;
import myLogic.stopLogic.StopStrategies;
import myLogic.waitLogic.WaitStrategies;
import org.testng.IInvokedMethod;
import org.testng.IRetryAnalyzer;
import org.testng.ITestContext;
import org.testng.ITestResult;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RetryUtility implements GenericRetryAnalyzer  {

    StopStrategies stopStrategies = new StopStrategies();
    WaitStrategies waitStrategies = new WaitStrategies();
    int retryAttemptNumber = 2;
    int retryLimit = 4;

    @Override
    public boolean retry(ITestResult result, StopStrategies stopStrategies, WaitStrategies waitStrategies) {

      //  System.out.println("In retry");
        if(retryAttemptNumber < retryLimit)
        {
            System.out.println("In retry if loop ---"+"RetryAttemptnumber "+retryAttemptNumber );
            retryAttemptNumber++;
            return true;
        }
        return false;
    }


    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {

    }


    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {

    }


    public void beforeInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {

    }


    public void afterInvocation(IInvokedMethod invokedMethod, ITestResult result, ITestContext context) {
        System.out.println("In afterInvocation");
        if (invokedMethod.isTestMethod()) {
            if (!result.isSuccess()) {
                System.out.println("Throwable " + result.getThrowable().toString());
                String exceptionReason = result.getThrowable().toString();
                if (result.getThrowable() instanceof UnknownHostException) {
                    System.out.println("Instance of UnknownHostException ");
                }
                if (result.getThrowable() instanceof ConnectException) {
                    System.out.println("Instance of ConnectException ");
                    stopStrategies.stopAfterAttempt(2);
                    waitStrategies.fixedWait(1000, TimeUnit.SECONDS);


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


}
