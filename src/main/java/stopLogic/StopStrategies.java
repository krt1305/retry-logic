package stopLogic;

public class StopStrategies {

    public static StopStrategy stopAfterAttempt(int attemptNumber) {
        System.out.println("stopAfterAttempt...Attempt number "+attemptNumber);
        return new StopAfterAttemptStrategy(attemptNumber);
    }

    /**
     * Returns a stop strategy which consists in stopping after a given delay
     * @param delayInMillis the delay, in milliseconds, starting with the start of the first attempt.
     */
    public static StopStrategy stopAfterDelay(long delayInMillis) {
        return new StopAfterDelayStrategy(delayInMillis);
    }

}
