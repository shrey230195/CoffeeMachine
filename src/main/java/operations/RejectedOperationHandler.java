package operations;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class RejectedOperationHandler implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        System.out.printf("operations.RejectedOperationHandler: The beverage request %s has been rejected by coffee machine", r.toString());
    }
}
