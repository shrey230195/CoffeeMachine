package models.messageDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PreparationStatusDto {
    private boolean isPreparable;
    private int amountLeft;
    private String beverage;

    public String message() {
        System.out.println(this.isPreparable +" "+ this.beverage);
        if(this.isPreparable) {
            return this.beverage + " can be prepared";
        }
        return this.beverage + "can't be prepared, only " + this.amountLeft + "left in stock";
    }
}
