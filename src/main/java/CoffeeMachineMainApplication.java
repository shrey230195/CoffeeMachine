import operations.CoffeeMachine;
import org.apache.commons.io.FileUtils;
import java.io.File;

public class CoffeeMachineMainApplication {

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Arguments are required");
        }
        File file = new File(CoffeeMachine.class.getClassLoader().getResource(args[0]).getFile());
        String jsonInput = FileUtils.readFileToString(file, "UTF-8");
        System.out.println(jsonInput);
        CoffeeMachine coffeeMachine = CoffeeMachine.getInstance(jsonInput);
        coffeeMachine.serve();
        System.out.println("Application started");
    }

}
