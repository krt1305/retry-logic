package customLogic;

import java.util.List;

public class RetryConfigList {

    List<ExceptionList> exceptions;

    public RetryConfigList() {
    }

    public List<ExceptionList> getExceptions() {
        return exceptions;
    }

    public void setExceptions(List<ExceptionList> exceptions) {
        this.exceptions = exceptions;
    }

}
