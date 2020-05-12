package customLogic;

import java.util.List;

public class AbortableExceptions {

   String[] exceptions;

    public AbortableExceptions(String[] exceptions) {
        this.exceptions = exceptions;
    }

    public AbortableExceptions() {
    }

    public String[] getExceptions() {
        return exceptions;
    }

    public void setExceptions(String[] exceptions) {
        this.exceptions = exceptions;
    }
}
