package cz.barush.hdsp.entity;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;

/**
 * Created by Barbora on 21-Apr-17.
 */

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
@EqualsAndHashCode(exclude = {"location", "priceList"})
public class Supermarket {
    //Location location;
    String name;
    HashMap<Food, Double> priceList;

    @Getter
    @AllArgsConstructor
    public enum SupermarketType {
        ALBERT("Albert"),
        BILLA("Billa"),
        KAUFLAND("Kaufland"),
        GLOBUS("Globus"),
        TESCO("Tesco"),
        LIDL("Lidl");

        private final String name;
    }
}
