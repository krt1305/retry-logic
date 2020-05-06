package customLogic.delays;

import com.google.common.base.Preconditions;
import waitStrategy.FixedWaitStrategy;
import waitStrategy.WaitStrategy;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

public class FixedDelay {

    public static WaitStrategy fixedWait(long sleepTime, @Nonnull TimeUnit timeUnit) throws IllegalStateException {
        Preconditions.checkNotNull(timeUnit, "The time unit may not be null");
        System.out.println("In fixedWait ..waiting for "+sleepTime);
        return new FixedWaitStrategy(timeUnit.toMillis(sleepTime));
    }

}
