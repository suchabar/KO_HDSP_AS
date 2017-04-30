package cz.barush.hdsp.entity;

import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

/**
 * Created by Barbora on 30-Apr-17.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
public class TSPResponse
{
    List<List<Integer>> uniqueFeasibleSets;
    int[][] distances;
    int maxDistance;
}
