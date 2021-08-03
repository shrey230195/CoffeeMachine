
package models;
import lombok.Getter;
import java.util.Map;

@Getter
public class BeverageDto {
    private String name;

    public BeverageDto(String name, Map<String, Integer> ingredientQuantityMap) {
        this.name = name;
        this.ingredientQuantityMap = ingredientQuantityMap;
    }

    private Map<String, Integer> ingredientQuantityMap;
}
