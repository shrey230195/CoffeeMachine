package models.messageDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PreparationStatusDto {
    private boolean isPreparable;
    private int amountLeft;
    private String beverage;
    private String ingredient;

    public String message() {
        if(this.isPreparable) {
            return this.beverage + " is prepared";
        }
        return this.beverage + " cannot be prepared because " + this.ingredient + " is not available, only " + this.amountLeft + " left in stock";
    }
}
