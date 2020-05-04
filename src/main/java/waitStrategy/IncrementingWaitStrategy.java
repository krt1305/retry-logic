package waitStrategy;

import com.google.common.base.Preconditions;

public class IncrementingWaitStrategy implements WaitStrategy {
    private final long initialSleepTime;
    private final long increment;

    public IncrementingWaitStrategy(long initialSleepTime,
                                    long increment) {
        Preconditions.checkArgument(initialSleepTime >= 0L,
                "initialSleepTime must be >= 0 but is %d",
                initialSleepTime);
        this.initialSleepTime = initialSleepTime;
        this.increment = increment;
    }


    public long computeSleepTime(int previousAttemptNumber, long delaySinceFirstAttemptInMillis) {
        long result = initialSleepTime + (increment * (previousAttemptNumber - 1));
        return result >= 0L ? result : 0L;
    }
}