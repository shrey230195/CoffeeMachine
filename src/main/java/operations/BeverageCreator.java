package operations;

import models.BeverageDto;

public class BeverageCreator implements Runnable {
    private BeverageDto beverage;

    BeverageCreator(BeverageDto beverage) {
        this.beverage = beverage;
    }

    @Override
    public void run() {
        System.out.println("task ran successfully");

    }

    @Override
    public String toString() {
        return beverage.getName();
    }
}
