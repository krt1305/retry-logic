package waitStrategy;

import com.google.common.base.Preconditions;

public class FixedWaitStrategy implements WaitStrategy {

    private final long sleepTime;

    public FixedWaitStrategy(long sleepTime) {
        Preconditions.checkArgument(sleepTime >= 0L, "sleepTime must be >= 0 but is %d", sleepTime);
        this.sleepTime = sleepTime;
    }


    public long computeSleepTime(int previousAttemptNumber, long delaySinceFirstAttemptInMillis) {
        return sleepTime;
    }
}
