package stopLogic;

import com.google.common.base.Preconditions;

public class StopAfterDelayStrategy implements StopStrategy {

    private final long maxDelay;

    public StopAfterDelayStrategy(long maxDelay) {
        Preconditions.checkArgument(maxDelay >= 0L, "maxDelay must be >= 0 but is %d", maxDelay);
        this.maxDelay = maxDelay;
    }


    public boolean shouldStop(int previousAttemptNumber, long delaySinceFirstAttemptInMillis) {
        return delaySinceFirstAttemptInMillis >= maxDelay;
    }
}