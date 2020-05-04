package skipDefects;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(MethodIssueStatusListener.class)
public class SampleClass {


    @Issue("JRA-9")
    @Test
    public void launchGooglePage()
    {

    }
}
