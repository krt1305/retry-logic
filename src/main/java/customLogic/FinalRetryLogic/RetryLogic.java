package customLogic.FinalRetryLogic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import customLogic.ExceptionList;
import customLogic.RetryConfig;
import customLogic.RetryConfigList;
import customLogic.RetryPolicy;
import customLogic.delays.DelayType;
import customLogic.slackNotification.SlackUtil;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
    static List<ExceptionList> exceptions;
    boolean exceptionFoundInConfig = false;
    static int maxAttempts, incrementingWaitFactor;
    static String delayType;
    static long fixedDelay, delayMin, delayMax, jitter;
    static Map<String, Map<String, Object>> delayConfigDetails = new HashMap<String, Map<String, Object>>();

    static {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            retryConfigList = mapper.readValue(new File("src/main/java/customLogic/resources/retryConfigurationList.yaml"), RetryConfigList.class);
            exceptions = retryConfigList.getExceptions();
            for (int i = 0; i <= exceptions.size() - 1; i++) {
                System.out.println("Exception type is " + exceptions.get(i).getExceptionType());
                Map<String, Object> delayDetails = new HashMap<>();
                maxAttempts = (int) exceptions.get(i).getDelayConfigurations().get("maxAttemps");
                delayType = exceptions.get(i).getDelayConfigurations().get("delayType").toString();
                fixedDelay = Long.parseLong(exceptions.get(i).getDelayConfigurations().get("fixedDelay").toString());
                delayMin = Long.parseLong(exceptions.get(i).getDelayConfigurations().get("delayMin").toString());
                delayMax = Long.parseLong(exceptions.get(i).getDelayConfigurations().get("delayMax").toString());
                jitter = Long.parseLong(exceptions.get(i).getDelayConfigurations().get("delayMax").toString());
                incrementingWaitFactor = (int) exceptions.get(i).getDelayConfigurations().get("incrementingWaitFactor");
                System.out.println("Max attempts " + (int) exceptions.get(i).getDelayConfigurations().get("maxAttemps"));
                System.out.println("Delay type " + exceptions.get(i).getDelayConfigurations().get("delayType"));
                delayDetails.put("maxAttempts", maxAttempts);
                delayDetails.put("delayType", delayType);
                delayDetails.put("fixedDelay", fixedDelay);
                delayDetails.put("delayMin", delayMin);
                delayDetails.put("delayMax", delayMax);
                delayDetails.put("jitter", jitter);
                delayDetails.put("incrementingWaitFactor", incrementingWaitFactor);
                delayConfigDetails.put(exceptions.get(i).getExceptionType(), delayDetails);
                //  exceptionFoundInConfig = true;
            }

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
                for (Map.Entry<String, Map<String, Object>> exceptionEntry : delayConfigDetails.entrySet()) {
                    System.out.println("For ------------------------------------------------");
                    String exceptiontype = exceptionEntry.getKey();
                    if (cause.toString().contains(exceptiontype)) {
                        System.out.println("Exception found in config");
                        exceptionFoundInConfig = true;
                        for (Map.Entry<String, Object> delayEntries : exceptionEntry.getValue().entrySet()) {
                            String key = delayEntries.getKey();
                            Object value = delayEntries.getValue();
                            System.out.println("Key-----" + key + "Value -----------" + value);
                            if (delayEntries.getKey().contains("maxAttempts"))
                                maxAttempts = (int) delayEntries.getValue();
                            if (delayEntries.getKey().contains("delayType"))
                                delayType = delayEntries.getValue().toString();
                            if (delayEntries.getKey().contains("fixedDelay"))
                                fixedDelay = (Long) delayEntries.getValue();
                            if (delayEntries.getKey().contains("delayMin"))
                                delayMin = (Long) delayEntries.getValue();
                            if (delayEntries.getKey().contains("delayMax"))
                                delayMax = (Long) delayEntries.getValue();
                            if (delayEntries.getKey().contains("jitter"))
                                jitter = (Long) delayEntries.getValue();
                            if (delayEntries.getKey().contains("incrementingWaitFactor"))
                                incrementingWaitFactor = (int) delayEntries.getValue();

                        }


                    }
                    if (exceptionFoundInConfig == true)
                        break;


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
