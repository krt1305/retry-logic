package ninjaCore;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;


/**
 * A retryer, which executes a call, and retries it until it succeeds, or
 * a stop strategy decides to stop retrying. A wait strategy is used to sleep
 * between attempts. The strategy to decide if the call succeeds or not is
 * also configurable.
 * <p>
 * A retryer can also wrap the callable into a RetryerCallable, which can be submitted to an executor.
 * <p>
 * Retryer instances are better constructed with a {@link RetryerBuilder}. A retryer
 * is thread-safe, provided the arguments passed to its constructor are thread-safe.
 *
 * @author JB Nizet
 * @param <V> the type of the call return value
 */
public final class Retryer<V> {
    private final StopStrategy stopStrategy;
    private final WaitStrategy waitStrategy;
    private final Predicate<Attempt<V>> rejectionPredicate;

    /**
     * Constructor
     * @param stopStrategy the strategy used to decide when the retryer must stop retrying
     * @param waitStrategy the strategy used to decide how much time to sleep between attempts
     * @param rejectionPredicate the predicate used to decide if the attempt must be rejected
     * or not. If an attempt is rejected, the retryer will retry the call, unless the stop
     * strategy indicates otherwise or the thread is interrupted.
     */
    public Retryer(@Nonnull StopStrategy stopStrategy,
                   @Nonnull WaitStrategy waitStrategy,
                   @Nonnull Predicate<Attempt<V>> rejectionPredicate) {
        Preconditions.checkNotNull(stopStrategy, "stopStrategy may not be null");
        Preconditions.checkNotNull(waitStrategy, "waitStrategy may not be null");
        Preconditions.checkNotNull(rejectionPredicate, "waitStrategy may not be null");

        this.stopStrategy = stopStrategy;
        this.waitStrategy = waitStrategy;
        this.rejectionPredicate = rejectionPredicate;
    }

    /**
     * Executes the given callable. If the rejection predicate
     * accepts the attempt, the stop strategy is used to decide if a new attempt
     * must be made. Then the wait strategy is used to decide how must time to sleep,
     * and a new attempt is made.
     * @throws ExecutionException if the given callable throws an exception, and the
     * rejection predicate considers the attempt as successful. The original exception
     * is wrapped into an ExecutionException.
     * @throws RetryException if all the attempts failed before the stop strategy decided
     * to abort, or the thread was interrupted. Note that if the thread is interrupted,
     * this exception is thrown and the thread's interrupt status is set.
     */
    @SuppressWarnings("PMD.AvoidCatchingThrowable")
    public V call(Callable<V> callable) throws ExecutionException, RetryException {
        long startTime = System.currentTimeMillis();
        for (int attemptNumber = 1;; attemptNumber++) {
            Attempt<V> attempt;
            try {
                V result = callable.call();
                attempt = new ResultAttempt<V>(result);
            }
            catch (Throwable t) {
                attempt = new ExceptionAttempt<V>(t);
            }
            if (!rejectionPredicate.apply(attempt)) {
                return attempt.get();
            }
            long delaySinceFirstAttemptInMillis = System.currentTimeMillis() - startTime;
            if (stopStrategy.shouldStop(attemptNumber, delaySinceFirstAttemptInMillis)) {
                throw new RetryException(attemptNumber, attempt);
            }
            else {
                long sleepTime = waitStrategy.computeSleepTime(attemptNumber, System.currentTimeMillis() - startTime);
                try {
                    Thread.sleep(sleepTime);
                }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RetryException(attemptNumber, attempt, e);
                }
            }
        }
    }

    /**
     * Wraps the given callable into a {@link RetryerCallable}, which can be submitted to an executor.
     * The returned callable will use this retryer to call the given callable
     * @param callable the callable to wrap
     */
    public RetryerCallable<V> wrap(Callable<V> callable) {
        return new RetryerCallable<V>(this, callable);
    }

    /**
     * Implementation of Attempt that wraps a result returned by the attempt
     * @author JB Nizet
     *
     * @param <R> the type of result of the attempt
     */
    @Immutable
    private static final class ResultAttempt<R> implements Attempt<R> {
        private final R result;
        public ResultAttempt(R result) {
            this.result = result;
        }

        
        public R get() throws ExecutionException {
            return result;
        }

        
        public boolean hasResult() {
            return true;
        }

        
        public boolean hasException() {
            return false;
        }

        
        public R getResult() throws IllegalStateException {
            return result;
        }

        
        public Throwable getExceptionCause() throws IllegalStateException {
            throw new IllegalStateException("The attempt resulted in a result, not in an exception");
        }
    }

    /**
     * Implementation of Attempt that wraps an exception which was throws during the attempt
     * @author JB Nizet
     *
     * @param <R> the type of result of the attempt
     */
    @Immutable
    private static final class ExceptionAttempt<R> implements Attempt<R> {
        private final ExecutionException e;

        public ExceptionAttempt(Throwable cause) {
            this.e = new ExecutionException(cause);
        }

        
        public R get() throws ExecutionException {
            throw e;
        }

        
        public boolean hasResult() {
            return false;
        }

        
        public boolean hasException() {
            return true;
        }

        
        public R getResult() throws IllegalStateException {
            throw new IllegalStateException("The attempt resulted in an exception, not in a result");
        }

        
        public Throwable getExceptionCause() throws IllegalStateException {
            return e.getCause();
        }
    }

    /**
     * A Callable which wraps another callable in order to make it call by the enclosing retryer
     * @author JB Nizet
     */
    public static final class RetryerCallable<X> implements Callable<X> {
        private Retryer<X> retryer;
        private Callable<X> callable;

        private RetryerCallable(Retryer<X> retryer,
                                Callable<X> callable) {
            this.retryer = retryer;
            this.callable = callable;
        }

        /**
         * Makes the enclosing retryer call the wrapped callable.
         * @see Retryer#call(Callable)
         */
        
        public X call() throws ExecutionException, RetryException {
            return retryer.call(callable);
        }
    }
}
