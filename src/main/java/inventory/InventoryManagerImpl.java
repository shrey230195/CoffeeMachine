package inventory;

import models.BeverageDto;
import models.messageDto.PreparationStatusDto;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is a thread-safe singleton implementation for an inventory management system.
 * This class takes care of handling all the inventory.
 */

public class InventoryManagerImpl {
    private final ConcurrentHashMap<String, Integer> inventory = new ConcurrentHashMap<>();

    private InventoryManagerImpl() {
    }

    //Using Holder pattern ensures thread safe initialisation of the object,
    private static class InventoryManagerHolder {
        public static final InventoryManagerImpl instance = new InventoryManagerImpl();
    }

    public static InventoryManagerImpl getInstance() {
        return InventoryManagerHolder.instance;
    }

    // using concurrent hashmap to use serve beverages that have uncommon ingredients,
    // and not block the entire hashmap for each serving
    // caveats:
    // 1. We'll have to revert the ingredient count manually if any one ingredient is lacking.
    // 2. This will Leave the system in false promise state for some time and might not serve the beverage which could be served
    public PreparationStatusDto concurrentUpdateInventory(BeverageDto beverage) {
        Map<String, Integer> requiredIngredientMap = beverage.getIngredientQuantityMap();
        HashMap<String, Integer> updatedIngredients = new HashMap<>();
        PreparationStatusDto status = new PreparationStatusDto();
        status.setBeverage(beverage.getName());
        status.setPreparable(true);
        for (String ingredient : requiredIngredientMap.keySet()) {
            inventory.compute(ingredient, (key, value) -> {
                if(value== null) {
                    status.setAmountLeft(0);
                    status.setPreparable(false);
                    status.setIngredient(ingredient);
                    return value;
                }
                if (value == -1 || value == 0) {
                    status.setAmountLeft(0);
                    status.setPreparable(false);
                    status.setIngredient(ingredient);
                }
                status.setPreparable(value >= requiredIngredientMap.get(ingredient));
                status.setAmountLeft(value);
                status.setIngredient(ingredient);
                if(status.isPreparable()) {
                    value -= requiredIngredientMap.get(ingredient);
                    updatedIngredients.put(ingredient, requiredIngredientMap.get(ingredient));
                }
                return value;
            });
        }

        if(!status.isPreparable()) {
            for (String ingredient : updatedIngredients.keySet()) {
                restoreInventory(updatedIngredients);
            };
        }
        return status;
    }



    public void addInventory(String ingredient, int quantity) {
        int existingInventory = inventory.getOrDefault(ingredient, 0);
        inventory.put(ingredient, existingInventory + quantity);
    }

    // Making the entire operation synchronized
    public synchronized PreparationStatusDto checkAndUpdateInventorySync(BeverageDto beverage) {
        Map<String, Integer> requiredIngredientMap = beverage.getIngredientQuantityMap();
        PreparationStatusDto status = new PreparationStatusDto();
        status.setBeverage(beverage.getName());
        status.setPreparable(true);
        for (String ingredient : requiredIngredientMap.keySet()) {
            int ingredientInventoryCount = inventory.getOrDefault(ingredient, -1);
            if (ingredientInventoryCount == -1 || ingredientInventoryCount == 0) {
                status.setAmountLeft(0);
                status.setPreparable(false);
                status.setIngredient(ingredient);
                break;
            } else if (requiredIngredientMap.get(ingredient) > ingredientInventoryCount) {
                status.setAmountLeft(ingredientInventoryCount);
                status.setPreparable(false);
                status.setIngredient(ingredient);
                break;
            }
        }

        if (status.isPreparable()) {
            for (String ingredient : requiredIngredientMap.keySet()) {
                int existingInventory = inventory.getOrDefault(ingredient, 0);
                inventory.put(ingredient, existingInventory - requiredIngredientMap.get(ingredient));
            }
        }

        return status;
    }

    public void resetInventory() {
        inventory.clear();
    }

    private void restoreInventory(HashMap<String, Integer> updatedIngredients) {
        for (String ingredient : updatedIngredients.keySet()) {
            inventory.compute(ingredient, (key, value) -> {
                value += updatedIngredients.get(ingredient);
                return value;
            });
        };
    }
}
