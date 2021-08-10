package operations;

import inventory.InventoryManager;
import models.BeverageDto;
import models.messageDto.PreparationStatusDto;

public class BeverageCreator implements Runnable {
    private BeverageDto beverage;

    BeverageCreator(BeverageDto beverage) {
        this.beverage = beverage;
    }

    @Override
    public void run() {
        PreparationStatusDto status = InventoryManager.getInstance().checkAndUpdateInventorySync(beverage);
        System.out.println(status.message());
    }

    @Override
    public String toString() {
        return beverage.getName();
    }
}
