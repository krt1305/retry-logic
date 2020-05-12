package customLogic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import customLogic.delays.DelayType;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RetryPolicy {

    // Retry Policy config
    private long fixedDelay;
    private long delayMin;
    private long delayMax;
    private double delayFactor;
    private long maxDelay;
    private long jitter;
    private double jitterFactor;
    private long maxDuration;
    private int maxRetries;
    private List<String> abortConditions;
    private int maxRetry;
    private DelayType delayType;
    private String[] abortableExceptionsList;
    static ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    static AbortableExceptions abortableExceptions;

    public RetryPolicy() {
    }


    public boolean isAbortable(Throwable t) {
        try {
            abortableExceptions = mapper.readValue(new File("src/main/java/customLogic/resources/abortableExceptionsList.yaml"), AbortableExceptions.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(ReflectionToStringBuilder.toString(abortableExceptions, ToStringStyle.MULTI_LINE_STYLE));

        abortableExceptionsList = abortableExceptions.getExceptions();
        for (String s : abortableExceptionsList) {
            if (t.toString().contains(s)) {
                return true;
            }

        }
        return false;
    }


    public void withRetryPolicy(Throwable t, int maxRetry, String inputDelayType,
                                long fixedDelay, long delayMin, long delayMax,
                                long jitter, int incrementingWaitFactor, int attemptNo) {
        Assert.assertTrue(Integer.class.isInstance(maxRetry));
        Assert.assertTrue(Long.class.isInstance(fixedDelay));
        Assert.assertTrue(Long.class.isInstance(delayMin));
        Assert.assertTrue(Long.class.isInstance(delayMax));
        Assert.assertTrue(Long.class.isInstance(jitter));
        Assert.assertTrue(Integer.class.isInstance(incrementingWaitFactor));
        Assert.assertNotNull(t, "Exception reason cannot be null");
        Assert.assertNotNull(maxRetry, "Max retry cannot be null");
        Assert.assertNotNull(inputDelayType, "DelayType cannot be null");
        Assert.assertNotNull(delayMin, "delayMin cannot be null");
        Assert.assertNotNull(delayMax, "delayMax cannot be null");
        Assert.assertNotNull(jitter, "jitter cannot be null");

        delayType = new DelayType();
        switch (inputDelayType) {

            case "fixed":
                delayType.fixedDelay(fixedDelay, TimeUnit.SECONDS);
                break;
            case "random":
                try {
                    delayType.randomDelay(delayMin, delayMax, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case "incrementing":
                delayType.incrementingDelay(fixedDelay, incrementingWaitFactor, attemptNo, TimeUnit.SECONDS);
                break;

        }

    }

    public long getFixedDelay() {
        return fixedDelay;
    }

    public void setFixedDelay(long fixedDelay) {
        this.fixedDelay = fixedDelay;
    }

    public long getDelayMin() {
        return delayMin;
    }

    public void setDelayMin(long delayMin) {
        this.delayMin = delayMin;
    }

    public long getDelayMax() {
        return delayMax;
    }

    public void setDelayMax(long delayMax) {
        this.delayMax = delayMax;
    }

    public double getDelayFactor() {
        return delayFactor;
    }

    public void setDelayFactor(double delayFactor) {
        this.delayFactor = delayFactor;
    }

    public long getMaxDelay() {
        return maxDelay;
    }

    public void setMaxDelay(long maxDelay) {
        this.maxDelay = maxDelay;
    }

    public long getJitter() {
        return jitter;
    }

    public void setJitter(long jitter) {
        this.jitter = jitter;
    }

    public double getJitterFactor() {
        return jitterFactor;
    }

    public void setJitterFactor(double jitterFactor) {
        this.jitterFactor = jitterFactor;
    }

    public long getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(long maxDuration) {
        this.maxDuration = maxDuration;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public List<String> getAbortConditions() {
        return abortConditions;
    }

    public void setAbortConditions(List<String> abortConditions) {
        this.abortConditions = abortConditions;
    }

    public int getMaxRetry() {
        return maxRetry;
    }

    public void setMaxRetry(int maxRetry) {
        this.maxRetry = maxRetry;
    }

    public DelayType getDelayType() {
        return delayType;
    }

    public void setDelayType(DelayType delayType) {
        this.delayType = delayType;
    }
}
