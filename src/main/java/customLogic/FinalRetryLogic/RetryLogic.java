package customLogic.FinalRetryLogic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import customLogic.ExceptionList;
import customLogic.RetryConfig;
import customLogic.RetryConfigList;
import customLogic.RetryPolicy;
import customLogic.delays.DelayType;
import customLogic.slackNotification.SlackUtil;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class RetryLogic implements IRetryAnalyzer {

    int retryAttempt = 1;
    int retryLimit = 2;
    int noOfFailedAttempts = 0;
    static List<String> abortConditions = new ArrayList<String>();
    RetryPolicy retryPolicy = new RetryPolicy();
    static RetryConfigList retryConfigList = null;
    DelayType delay;
    static RetryConfig retryConfig = null;
    SlackUtil slackUtil = new SlackUtil();
    List<ExceptionList> exceptions;
    boolean exceptionFoundInConfig = false;
    int maxAttempts, incrementingWaitFactor;
    String delayType;
    long fixedDelay, delayMin, delayMax, jitter;

    static {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {

            retryConfigList = mapper.readValue(new File("src/main/java/customLogic/resources/retryConfigurationList.yaml"), RetryConfigList.class);
            System.out.println(ReflectionToStringBuilder.toString(retryConfig, ToStringStyle.MULTI_LINE_STYLE));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean retry(ITestResult result) {
        if (result.getThrowable() != null) {
            Throwable cause = result.getThrowable();
            System.out.println("Cause is " + cause);
            if (retryPolicy.isAbortable(cause)) {
                System.out.println("Aborting Retry -----");
                return false;

            } else {
                exceptions = retryConfigList.getExceptions();
                System.out.println("Total number of exceptions in configuration " + exceptions.size());
                for (int i = 0; i <= exceptions.size() - 1; i++) {
                    System.out.println("Exception type is " + exceptions.get(i).getExceptionType());
                    if (cause.toString().contains(exceptions.get(i).getExceptionType())) {
                        maxAttempts = (int) exceptions.get(i).getDelayConfigurations().get("maxAttemps");
                        delayType = exceptions.get(i).getDelayConfigurations().get("delayType").toString();
                        fixedDelay = Long.parseLong(exceptions.get(i).getDelayConfigurations().get("fixedDelay").toString());
                        delayMin = Long.parseLong(exceptions.get(i).getDelayConfigurations().get("delayMin").toString());
                        delayMax = Long.parseLong(exceptions.get(i).getDelayConfigurations().get("delayMax").toString());
                        jitter = Long.parseLong(exceptions.get(i).getDelayConfigurations().get("delayMax").toString());
                        incrementingWaitFactor = (int) exceptions.get(i).getDelayConfigurations().get("incrementingWaitFactor");
                        System.out.println("Max attempts " + (int) exceptions.get(i).getDelayConfigurations().get("maxAttemps"));
                        System.out.println("Delay type " + exceptions.get(i).getDelayConfigurations().get("delayType"));
                        System.out.println("found exception in config ...Breaking for loop ----");
                        exceptionFoundInConfig = true;
                        break;

                    }
                }
                if (exceptionFoundInConfig == true) {
                    if (retryAttempt < maxAttempts) {
                        System.out.println("Executing retry policy");
                        //retry policy
                        retryPolicy.withRetryPolicy(cause,
                                maxAttempts, delayType, fixedDelay, delayMin, delayMax, jitter, incrementingWaitFactor, retryAttempt);
                        System.out.println("Retry attempt ---" + retryAttempt);
                        retryAttempt++;
                        noOfFailedAttempts++;
                        return true;


                    }
                } else {
                    if (retryAttempt < retryLimit) {
                        System.out.println("Exception not found in configuration");
                        System.out.println("Picking default Retry attempt ---" + retryAttempt);
                        retryAttempt++;
                        noOfFailedAttempts++;
                        return true;
                    }

                }


            }

        } else {
            if (retryAttempt < retryLimit) {
                System.out.println("Exception not found in configuration");
                System.out.println("Picking default Retry attempt ---" + retryAttempt);
                retryAttempt++;
                noOfFailedAttempts++;
                return true;
            }
        }

        return false;

    }


    public void sendSlackNotification() {
       /* SlackMessage slackMessage = SlackMessage
                .channel("the-channel-name")
                .username("user1")
                .text("just testing")
                .icon_emoji(":twice:")
                .build();
        SlackUtil.sendMessage(slackMessage);*/

    }

    public class TimerTaskDemo extends TimerTask {

        @Override
        public void run() {
            System.out.println("****************************************************");
            System.out.println(System.currentTimeMillis());
            System.out.println("working at fixed rate delay");
        }
    }


}
