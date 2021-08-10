package operations;

import com.fasterxml.jackson.databind.ObjectMapper;

import inventory.InventoryManagerImpl;
import models.BeverageDto;
import models.inputDto.CoffeeMachineInputDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CoffeeMachine {
    private static final Logger logger = LoggerFactory.getLogger(CoffeeMachine.class);
    private static CoffeeMachine coffeeMachine;
    public CoffeeMachineInputDto coffeeMachineInputDto;
    private InventoryManagerImpl inventoryManagerImpl;
    private static final int MAX_QUEUED_REQUEST = 100;
    private ThreadPoolExecutor executor;

    private CoffeeMachine(String jsonInput) throws IOException {
        logger.info("Initializing New Machine");
        this.coffeeMachineInputDto = new ObjectMapper().readValue(jsonInput, CoffeeMachineInputDto.class);
        int outlets = coffeeMachineInputDto.getMachine().getOutlets().getCount();
        logger.info("Total outlets in the Machine: " + outlets);
        logger.info("Creating " + outlets + " threads");
        executor = new ThreadPoolExecutor(outlets, outlets, 5000L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(MAX_QUEUED_REQUEST));
        executor.setRejectedExecutionHandler(new RejectedOperationHandler());
    }

    public static CoffeeMachine getInstance(final String jsonInput) throws IOException {
        if (coffeeMachine == null) {
            coffeeMachine = new CoffeeMachine(jsonInput);
        }
        return coffeeMachine;
    }

    public void serve() {
        this.inventoryManagerImpl = InventoryManagerImpl.getInstance();
        Map<String, Integer> ingredients = coffeeMachineInputDto.getMachine().getIngredientQuantityMap();
        for (String key : ingredients.keySet()) {
            inventoryManagerImpl.addInventory(key, ingredients.get(key));
        }

        HashMap<String, HashMap<String, Integer>> beverages = coffeeMachineInputDto.getMachine().getBeverages();

        for (String key : beverages.keySet()) {
            BeverageDto beverage = new BeverageDto(key, beverages.get(key));
            coffeeMachine.addBeverageRequest(beverage);
        }
    }

    public void addBeverageRequest(BeverageDto beverage) {
        BeverageCreator task = new BeverageCreator(beverage);
        executor.execute(task);
    }

    public void stopMachine() {
        executor.shutdown();
    }

    /**
     * Resetting inventory and stopping coffee machine.
     * This is only used for testing. In real world, no need for resetting unless machine is stopped.
     * */
    public void resetMachine() {
        this.stopMachine();
        this.inventoryManagerImpl.resetInventory();
    }
}
