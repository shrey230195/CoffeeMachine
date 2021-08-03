package operations;

import com.fasterxml.jackson.databind.ObjectMapper;

import inventory.InventoryManager;
import models.inputDto.CoffeeMachineInputDto;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CoffeeMachine {
    private static CoffeeMachine coffeeMachine;
    public CoffeeMachineInputDto coffeeMachineInputDto;
    private InventoryManager inventoryManager;

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

    public void process() {
        this.inventoryManager = InventoryManager.getInstance();

        Map<String, Integer> ingredients = coffeeMachineInputDto.getMachine().getIngredientQuantityMap();
        System.out.println(ingredients.toString());
        for (String key : ingredients.keySet()) {
            inventoryManager.addInventory(key, ingredients.get(key));
        }

        HashMap<String, HashMap<String, Integer>> beverages = coffeeMachineInputDto.getMachine().getBeverages();
        System.out.println(beverages.toString());
    }
}
