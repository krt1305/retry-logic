package customLogic.delays;

import org.testng.Assert;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

public class DelayType {


    // static ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    long delayMin;
    long delayMax;
    long jitter;
    long fixedDelay;
    String delayType;
    int maxAttempts;

    public DelayType(long delayMin, long delayMax, long jitter, long fixedDelay, String delayType, int maxAttempts) {
        this.delayMin = delayMin;
        this.delayMax = delayMax;
        this.jitter = jitter;
        this.fixedDelay = fixedDelay;
        this.delayType = delayType;
        this.maxAttempts = maxAttempts;
    }


    public DelayType() {
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

    public long getJitter() {
        return jitter;
    }

    public void setJitter(long jitter) {
        this.jitter = jitter;
    }

    public long getFixedDelay() {
        return fixedDelay;
    }

    public void setFixedDelay(long fixedDelay) {
        this.fixedDelay = fixedDelay;
    }

    public String getDelayType() {
        return delayType;
    }

    public void setDelayType(String delayType) {
        this.delayType = delayType;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public void fixedDelay(long sleepTime, @Nonnull TimeUnit timeUnit) throws IllegalStateException {
        Assert.assertNotNull(timeUnit, "The time unit may not be null");
        // executorService.scheduleAtFixedRate(Retry1 :: ,0,sleepTime, TimeUnit.SECONDS);
        System.out.println("In fixed delay ..Waiting for " + sleepTime);
        try {
            timeUnit.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void randomDelayInRange(long delayMin, long delayMax, double random) {

        Assert.assertNotNull(delayMin, "delayMin may not be null");
        Assert.assertNotNull(delayMax, "delayMax may not be null");
        Assert.assertNotNull(random, "random may not be null");
        try {
            TimeUnit.SECONDS.sleep((long) (random * (delayMax - delayMin)) + delayMin);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //return (long) (random * (delayMax - delayMin)) + delayMin;

    }

    public void randomDelay(long delayMin, long delayMax, TimeUnit timeUnit) throws InterruptedException {
        Assert.assertNotNull(delayMin, "delayMin may not be null");
        Assert.assertNotNull(delayMax, "delayMax may not be null");
        Assert.assertNotNull(timeUnit, "timeUnit may not be null");
        System.out.println("IN random delay");
        System.out.println("delayMin --" + delayMin);
        System.out.println("delayMax --" + delayMax);
        System.out.println("timeUnit --" + timeUnit);
        long random = (int) (delayMax * Math.random() + delayMin);
        System.out.println("Random time for sleep is " + random);
        timeUnit.sleep(random);
    }

    public void randomDelay(long delay, long jitter, double random, TimeUnit timeUnit) throws InterruptedException {
        Assert.assertNotNull(delayMin, "delayMin may not be null");
        Assert.assertNotNull(delayMax, "delayMax may not be null");
        Assert.assertNotNull(timeUnit, "timeUnit may not be null");
        Assert.assertNotNull(jitter, "jitter may not be null");
        double randomAddend = (1 - random * 2) * jitter;
        long totalDelay = (long) (delay + randomAddend);
        System.out.println("Total Random time for sleep is " + totalDelay);
        timeUnit.sleep(totalDelay);
        //  return (long) (delay + randomAddend);
    }

    public long randomDelay(long delay, double jitterFactor, double random, TimeUnit timeUnit) {
        double randomFactor = 1 + (1 - random * 2) * jitterFactor;
        return (long) (delay * randomFactor);
    }


    public void incrementingDelay(long initialSleepTime, int incrementFactor, int attemptNo,
                                  @Nonnull TimeUnit timeUnit) {
        System.out.println("In incrementing delay");
        System.out.println("Attempt no is "+attemptNo);
        Assert.assertNotNull(initialSleepTime, "initialSleepTime may not be null");
        Assert.assertNotNull(incrementFactor, "incrementFactor may not be null");
        Assert.assertNotNull(attemptNo, "attemptNo may not be null");
        Assert.assertNotNull(timeUnit, "timeUnit may not be null");
        long result = initialSleepTime + (incrementFactor * (attemptNo - 1));
        try {
            timeUnit.sleep(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("IN incrementing wait ...sleeping for " + result);
        //return new FixedWaitStrategy(timeUnit.toMillis(sleepTime));
    }


}
