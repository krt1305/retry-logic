package customLogic.delays;

public final class RandomDelay {
    private RandomDelay() {
    }

    public static long randomDelayInRange(long delayMin, long delayMax, double random) {
        return (long) (random * (delayMax - delayMin)) + delayMin;
    }

    public static long randomDelay(long delay, long jitter, double random) {
        double randomAddend = (1 - random * 2) * jitter;
        return (long) (delay + randomAddend);
    }

    public static long randomDelay(long delay, double jitterFactor, double random) {
        double randomFactor = 1 + (1 - random * 2) * jitterFactor;
        return (long) (delay * randomFactor);
    }
}