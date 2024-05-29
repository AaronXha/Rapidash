package rapidash;

import de.metanome.algorithms.dcfinder.predicates.Predicate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Projection {
    private List<String> columns;
    private List<Integer> values;
    private Map<String, Integer> projectionMap;

    public Projection(List<String> columns, List<Integer> values, Map<String, Integer> projectionMap){
        this.columns = columns;
        this.values = values;
        this.projectionMap = projectionMap;
    }

}
