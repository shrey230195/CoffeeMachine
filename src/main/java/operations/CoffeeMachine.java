package operations;

import com.fasterxml.jackson.databind.ObjectMapper;

import models.inputDto.CoffeeMachineInputDto;

import java.io.IOException;

public class CoffeeMachine {
    private static CoffeeMachine coffeeMachine;
    public final CoffeeMachineInputDto coffeeMachineInputDto;

    private CoffeeMachine(String jsonInput) throws IOException {
        System.out.println("New Machine");
        this.coffeeMachineInputDto = new ObjectMapper().readValue(jsonInput, CoffeeMachineInputDto.class);
        int outlet = coffeeMachineInputDto.getMachine().getOutlets().getCount();
        System.out.println("outlets" + outlet);
    }

    public static CoffeeMachine getInstance(final String jsonInput) throws IOException {
        if (coffeeMachine == null) {
            coffeeMachine = new CoffeeMachine(jsonInput);
        }
        return coffeeMachine;
    }
}
