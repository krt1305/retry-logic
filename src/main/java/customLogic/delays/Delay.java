package customLogic.delays;

public class Delay {

    String delayType;
    int maxAttempts;


    public Delay(String delayType, int maxAttempts) {
        this.delayType = delayType;
        this.maxAttempts = maxAttempts;
    }

    public Delay() {
    }

    public String getDelayType() {
        return delayType;
    }

    public void setDelayType(String delayType) {
        this.delayType = delayType;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }
}
