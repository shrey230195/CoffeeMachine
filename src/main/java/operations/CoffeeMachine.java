package operations;

import com.fasterxml.jackson.databind.ObjectMapper;

import inventory.InventoryManager;
import models.BeverageDto;
import models.inputDto.CoffeeMachineInputDto;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CoffeeMachine {
    private static CoffeeMachine coffeeMachine;
    public CoffeeMachineInputDto coffeeMachineInputDto;
    private InventoryManager inventoryManager;
    private static final int MAX_QUEUED_REQUEST = 100;
    private ThreadPoolExecutor executor;

    private CoffeeMachine(String jsonInput) throws IOException {
        System.out.println("New Machine");
        this.coffeeMachineInputDto = new ObjectMapper().readValue(jsonInput, CoffeeMachineInputDto.class);
        int outlet = coffeeMachineInputDto.getMachine().getOutlets().getCount();
        System.out.println("outlets" + outlet);
        executor = new ThreadPoolExecutor(outlet, outlet, 5000L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(MAX_QUEUED_REQUEST));
        executor.setRejectedExecutionHandler(new RejectedOperationHandler());
    }

    public static CoffeeMachine getInstance(final String jsonInput) throws IOException {
        if (coffeeMachine == null) {
            coffeeMachine = new CoffeeMachine(jsonInput);
        }
        return coffeeMachine;
    }

    public void serve() {
        this.inventoryManager = InventoryManager.getInstance();

        Map<String, Integer> ingredients = coffeeMachineInputDto.getMachine().getIngredientQuantityMap();
        System.out.println(ingredients.toString());
        for (String key : ingredients.keySet()) {
            inventoryManager.addInventory(key, ingredients.get(key));
        }

        HashMap<String, HashMap<String, Integer>> beverages = coffeeMachineInputDto.getMachine().getBeverages();
        System.out.println(beverages.toString());

        for (String key : beverages.keySet()) {
            BeverageDto beverage = new BeverageDto(key, beverages.get(key));
            System.out.println(key);
            coffeeMachine.addBeverageRequest(beverage);
        }
    }

    public void addBeverageRequest(BeverageDto beverage) {
        BeverageCreator task = new BeverageCreator(beverage);
        System.out.println(task.toString());
        executor.execute(task);
    }
}
