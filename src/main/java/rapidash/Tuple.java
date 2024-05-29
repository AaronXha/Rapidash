package rapidash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tuple {
    private int colCount;
    private List<String> columns;
    private List<Integer> values;
    private Map<String, Integer> tupleMap;

    public Tuple(List<String> columns, List<Integer> values, Map<String, Integer> map){
        this.columns = columns;
        this.values = values;
        this.tupleMap = map;
    }

    public Projection getProjection(List<String> cols){
        List<Integer> pValues = new ArrayList<>();
        Map<String, Integer> pMap = new HashMap<>();
        for(String col: cols){
            Integer v = tupleMap.get(col);
            pValues.add(v);
            pMap.put(col, v);
        }
        return new Projection(cols, pValues, pMap);
    }

}
