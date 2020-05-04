import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.SkipException;
import skipDefects.Issue;
import skipDefects.IssueStatus;
import skipDefects.IssueTrackerUtil;

public class JiraStatusListener implements IInvokedMethodListener {
    public void afterInvocation(IInvokedMethod invokedMethod, ITestResult result) {

        System.out.println("In after invocation");
    }

    public void beforeInvocation(IInvokedMethod invokedMethod, ITestResult result) {

        System.out.println("In before invocation");
        Issue issue = invokedMethod.getTestMethod()
                .getConstructorOrMethod()
                .getMethod()
                .getAnnotation(Issue.class);

        if (null != issue) {
            if (IssueStatus.OPEN.equals(IssueTrackerUtil.getStatus(issue.value()))) {
                System.out.println("Skipping this due to Open Defect - " + issue.value());
                throw new SkipException("Skipping this due to Open Defect - " + issue.value());
            }
            if (IssueStatus.CLOSED.equals(IssueTrackerUtil.getStatus(issue.value()))) {
                System.out.println("Skipping this due to Open Defect - " + issue.value());
                throw new SkipException("Skipping this due to Open Defect - " + issue.value());
            }
        }
    }
}
