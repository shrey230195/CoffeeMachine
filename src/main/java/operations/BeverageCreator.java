package operations;

import inventory.InventoryManagerImpl;
import models.BeverageDto;
import models.messageDto.PreparationStatusDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeverageCreator implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(CoffeeMachine.class);
    private BeverageDto beverage;

    BeverageCreator(BeverageDto beverage) {
        this.beverage = beverage;
    }

    @Override
    public void run() {
        PreparationStatusDto status = InventoryManagerImpl.getInstance().checkAndUpdateInventorySync(beverage);
        logger.info(status.message());
    }

    @Override
    public String toString() {
        return beverage.getName();
    }
}
