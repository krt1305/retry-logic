package ninjaCore;

public interface WaitStrategy {
    /**
     * Returns the time, in milliseconds, to sleep before retrying
     * @param previousAttemptNumber the number, starting from 1, of the previous (failed) attempt
     * @param delaySinceFirstAttemptInMillis the delay since the start of the first attempt, in milliseconds
     */
    long computeSleepTime(int previousAttemptNumber, long delaySinceFirstAttemptInMillis);
}
