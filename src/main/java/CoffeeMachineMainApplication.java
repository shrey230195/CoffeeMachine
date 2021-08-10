import operations.CoffeeMachine;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class CoffeeMachineMainApplication {
    private static final Logger logger = LoggerFactory.getLogger(CoffeeMachine.class);

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            logger.error("Arguments are required");
        }
        File file = new File(CoffeeMachine.class.getClassLoader().getResource(args[0]).getFile());
        String jsonInput = FileUtils.readFileToString(file, "UTF-8");
        CoffeeMachine coffeeMachine = CoffeeMachine.getInstance(jsonInput);
        coffeeMachine.serve();

    }

}
