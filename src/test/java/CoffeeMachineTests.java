import operations.CoffeeMachine;
import org.junit.After;
import org.junit.Before;

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
}
