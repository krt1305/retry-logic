package stopLogic;

import com.google.common.base.Preconditions;

public class StopAfterAttemptStrategy implements StopStrategy {


    private final int maxAttemptNumber;


    public boolean shouldStop(int previousAttemptNumber, long delaySinceFirstAttemptInMillis) {
        return false;
    }

    public StopAfterAttemptStrategy(int maxAttemptNumber) {
        Preconditions.checkArgument(maxAttemptNumber >= 1,
                "maxAttemptNumber must be >= 1 but is %d",
                maxAttemptNumber);
        this.maxAttemptNumber = maxAttemptNumber;
    }


}