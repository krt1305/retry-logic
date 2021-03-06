import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestResult;
import stopLogic.StopStrategies;
import waitStrategy.WaitStrategies;

public interface GenericRetryAnalyzer extends IInvokedMethodListener {

    boolean retry(ITestResult result, StopStrategies stopStrategies, WaitStrategies waitStrategies);

    default void beforeInvocation(IInvokedMethod method, ITestResult testResult) {

    }

    default void afterInvocation(IInvokedMethod method, ITestResult testResult) {

    }

    /**
     * To be implemented if the method needs a handle to contextual information.
     */
    default void beforeInvocation(
            IInvokedMethod method, ITestResult testResult, ITestContext context) {

    }

    /**
     * To be implemented if the method needs a handle to contextual information.
     */
    default void afterInvocation(
            IInvokedMethod invokedMethod, ITestResult result, ITestContext context) {


    }

}
