package models.inputDto;

import lombok.Getter;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;

@Getter
public class MachineDto {

    @JsonProperty("outlets")
    private OutletDto outlets;

    @JsonProperty("total_items_quantity")
    private HashMap<String, Integer> ingredientQuantityMap;

    @JsonProperty("beverages")
    private HashMap<String, HashMap<String, Integer>> beverages;
}
