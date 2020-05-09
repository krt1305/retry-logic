package customLogic;

import customLogic.FinalRetryLogic.RetryLogic;
import customLogic.delays.DelayType;
import org.springframework.util.Assert;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
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

    public RetryPolicy() {
    }

    public boolean isAbortable(Throwable t) {

        if (t instanceof UnknownHostException)
            return true;
       /* else if (t instanceof IOException)
            return true;*/
        else if (t instanceof IllegalStateException)
            return true;

        return false;

    }


    public void withRetryPolicy(Throwable t, int maxRetry, String inputDelayType,
                                long fixedDelay, long delayMin, long delayMax,
                                long jitter,int incrementingWaitFactor,int attemptNo) {
        Assert.notNull(t, "Exception reason cannot be null");
        Assert.notNull(maxRetry, "Max retry cannot be null");
        Assert.notNull(inputDelayType, "DelayType cannot be null");
        Assert.notNull(delayMin, "delayMin cannot be null");
        Assert.notNull(delayMax, "delayMax cannot be null");
        Assert.notNull(jitter, "jitter cannot be null");

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
                delayType.incrementingDelay(fixedDelay, incrementingWaitFactor, attemptNo,TimeUnit.SECONDS);
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
