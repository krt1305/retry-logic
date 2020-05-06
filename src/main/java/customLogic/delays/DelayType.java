package customLogic.delays;

import com.google.common.base.Preconditions;
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
        System.out.println("In fixed delay ..Waiting for "+sleepTime);
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
        long random = (int) (delayMax * Math.random() + delayMin);
        timeUnit.sleep(random);
    }

    public long randomDelay(long delay, long jitter, double random, TimeUnit timeUnit) {
        double randomAddend = (1 - random * 2) * jitter;
        return (long) (delay + randomAddend);
    }

    public long randomDelay(long delay, double jitterFactor, double random, TimeUnit timeUnit) {
        double randomFactor = 1 + (1 - random * 2) * jitterFactor;
        return (long) (delay * randomFactor);
    }


    public void incrementingWait(long sleepTime, @Nonnull TimeUnit timeUnit) throws IllegalStateException {
        Preconditions.checkNotNull(timeUnit, "The time unit may not be null");
        System.out.println("In fixedWait ..waiting for " + sleepTime);
        //return new FixedWaitStrategy(timeUnit.toMillis(sleepTime));
    }


}
