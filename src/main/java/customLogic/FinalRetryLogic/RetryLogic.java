package customLogic.FinalRetryLogic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import customLogic.RetryConfig;
import customLogic.RetryPolicy;
import customLogic.delays.DelayType;
import ninjaCore.StopStrategies;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import waitStrategy.WaitStrategies;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RetryLogic implements IRetryAnalyzer {

    int retryAttempt = 0;
    int retryLimit = 2;
    WaitStrategies waitStrategies;
    StopStrategies stopStrategies;
    int noOfFailedAttempts = 0;
    static List<String> abortConditions = new ArrayList<String>();
    RetryPolicy retryPolicy = new RetryPolicy();
    DelayType delay;
    static RetryConfig retryConfig = null;

    static {
        abortConditions.add("IllegalStateException");
        abortConditions.add("IOException");
        abortConditions.add("IllegalStateException");
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {

            retryConfig = mapper.readValue(new File("src/main/java/customLogic/resources/retryConfiguration.yaml"), RetryConfig.class);
            System.out.println(ReflectionToStringBuilder.toString(retryConfig, ToStringStyle.MULTI_LINE_STYLE));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean retry(ITestResult result) {
        Throwable cause = result.getThrowable();
        if (retryPolicy.isAbortable(cause)) {
            System.out.println("Aborting-----");
            return false;

        } else {
            if (retryAttempt < retryLimit) {
                //delay=new DelayType()
                System.out.println("Delay type " + retryConfig.getDelayConfigurations().get("delayType"));
                retryPolicy.withRetryPolicy(cause,
                        (int) retryConfig.getDelayConfigurations().get("maxAttemps"),
                        retryConfig.getDelayConfigurations().get("delayType").toString(),
                        Long.parseLong(retryConfig.getDelayConfigurations().get("fixedDelay").toString()),
                        Long.parseLong(retryConfig.getDelayConfigurations().get("delayMin").toString()),
                        Long.parseLong(retryConfig.getDelayConfigurations().get("delayMax").toString()),
                        Long.parseLong(retryConfig.getDelayConfigurations().get("jitter").toString()));
                System.out.println("Retry attempt ---" + retryAttempt);
                retryAttempt++;
                noOfFailedAttempts++;
                return true;

            }

        }
        return false;
    }
}
