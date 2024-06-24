package rapidash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tuple {
    private int colCount;
    private List<String> columns;
    private List<Integer> values;

    public Tuple(List<String> columns, List<Integer> values){
        this.columns = columns;
        this.values = values;
    }

    public int getColCount() {
        return colCount;
    }

    public List<String> getColumns() {
        return columns;
    }

    public List<Integer> getValues() {
        return values;
    }


    public Projection getProjection(List<String> cols){
        long t0 = System.currentTimeMillis();
        List<Integer> pValues = new ArrayList<>();
        for(String col: cols){
            pValues.add(values.get(cols.indexOf(col)));
        }
        Benchmark.time_projection += System.currentTimeMillis() - t0;
        return new Projection(cols, pValues);
    }

    public Integer getColValue(String col){
        return values.get(columns.indexOf(col));
    }

}
