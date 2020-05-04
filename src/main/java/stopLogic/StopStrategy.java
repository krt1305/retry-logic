package stopLogic;

public interface StopStrategy {

    boolean shouldStop(int previousAttemptNumber,
                       long delaySinceFirstAttemptInMillis);
}
