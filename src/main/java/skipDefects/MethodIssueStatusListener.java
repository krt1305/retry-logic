package skipDefects;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.SkipException;

public class MethodIssueStatusListener implements IInvokedMethodListener {
    public void afterInvocation(IInvokedMethod invokedMethod, ITestResult result) {
        // nothing to do
        System.out.println("Cause "+result.getThrowable().getCause());
        System.out.println("Message "+result.getThrowable().getMessage());
        System.out.println("Stack trace "+result.getThrowable().getStackTrace());

    }

    public void beforeInvocation(IInvokedMethod invokedMethod, ITestResult result) {

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
