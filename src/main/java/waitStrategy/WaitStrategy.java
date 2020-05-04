package waitStrategy;

public interface WaitStrategy {
    long computeSleepTime(int previousAttemptNumber, long delaySinceFirstAttemptInMillis);
}


