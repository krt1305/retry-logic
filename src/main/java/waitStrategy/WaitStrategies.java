package waitStrategy;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

public class WaitStrategies {
    public static WaitStrategy fixedWait(long sleepTime, @Nonnull TimeUnit timeUnit) throws IllegalStateException {
        Preconditions.checkNotNull(timeUnit, "The time unit may not be null");
        System.out.println("In fixedWait ..waiting for "+sleepTime);
        return new FixedWaitStrategy(timeUnit.toMillis(sleepTime));
    }

    /**
     * Returns a strategy consisting in sleeping a random amount of time
     * before retrying
     * @param maximumTime the maximum time to sleep
     * @param timeUnit the unit of the maximum time
     * @throws IllegalStateException if the maximum sleep time is &lt;= 0.
     */
    public static WaitStrategy randomWait(long maximumTime, @Nonnull TimeUnit timeUnit) {
        Preconditions.checkNotNull(timeUnit, "The time unit may not be null");
        return new RandomWaitStrategy(0L, timeUnit.toMillis(maximumTime));
    }

    /**
     * Returns a strategy consisting in sleeping a random amount of time
     * before retrying
     * @param minimumTime the minimum time to sleep
     * @param minimumTimeUnit the unit of the minimum time
     * @param maximumTime the maximum time to sleep
     * @param maximumTimeUnit the unit of the maximum time
     * @throws IllegalStateException if the minimum sleep time is &lt; 0, or if the
     * maximum sleep time is less than (or equals to) the minimum.
     */
    public static WaitStrategy randomWait(long minimumTime,
                                          @Nonnull TimeUnit minimumTimeUnit,
                                          long maximumTime,
                                          @Nonnull TimeUnit maximumTimeUnit) {
        Preconditions.checkNotNull(minimumTimeUnit, "The minimum time unit may not be null");
        Preconditions.checkNotNull(maximumTimeUnit, "The maximum time unit may not be null");
        return new RandomWaitStrategy(minimumTimeUnit.toMillis(minimumTime),
                maximumTimeUnit.toMillis(maximumTime));
    }

    /**
     * Returns a strategy consisting in sleeping a fixed amount of time after the first failed attempt,
     * and in incrementing the amount of time after each failed attempt
     * @param initialSleepTime the time to sleep before retrying the first time
     * @param initialSleepTimeUnit the unit of the initial sleep time
     * @param increment the increment added to the previous sleep time after each failed attempt
     * @param incrementTimeUnit the unit of the increment
     */
    public static WaitStrategy incrementingWait(long initialSleepTime,
                                                @Nonnull TimeUnit initialSleepTimeUnit,
                                                long increment,
                                                @Nonnull TimeUnit incrementTimeUnit) {
        Preconditions.checkNotNull(initialSleepTimeUnit, "The initial sleep time unit may not be null");
        Preconditions.checkNotNull(incrementTimeUnit, "The increment time unit may not be null");
        return new IncrementingWaitStrategy(initialSleepTimeUnit.toMillis(initialSleepTime),
                incrementTimeUnit.toMillis(increment));
    }

}



