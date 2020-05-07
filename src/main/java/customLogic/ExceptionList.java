package customLogic;

import java.util.Map;

public class ExceptionList {

    String exceptionType ;
    Map<String,Object> delayConfigurations;

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    public Map<String, Object> getDelayConfigurations() {
        return delayConfigurations;
    }

    public void setDelayConfigurations(Map<String, Object> delayConfigurations) {
        this.delayConfigurations = delayConfigurations;
    }

    public ExceptionList() {
    }

    public ExceptionList(String exceptionType, Map<String, Object> delayConfigurations) {
        this.exceptionType = exceptionType;
        this.delayConfigurations = delayConfigurations;
    }
}
