import operations.CoffeeMachine;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Field;

public class CoffeeMachineTests {
    CoffeeMachine coffeeMachine;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
        coffeeMachine.resetMachine();
        resetSingleton(CoffeeMachine.class, "coffeeMachine");
    }

    public static void resetSingleton(Class clazz, String fieldName) {
        Field instance;
        try {
            instance = clazz.getDeclaredField(fieldName);
            instance.setAccessible(true);
            instance.set(null, null);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Test
    public void testFor3OutletsValidInput() throws Exception {
        final String filePath = "input_1.json";
        File file = new File(CoffeeMachine.class.getClassLoader().getResource(filePath).getFile());
        String jsonInput = FileUtils.readFileToString(file, "UTF-8");
        coffeeMachine = CoffeeMachine.getInstance(jsonInput);
        coffeeMachine.serve();
        Assert.assertEquals(3, coffeeMachine.coffeeMachineInputDto.getMachine().getOutlets().getCount());
    }

}
