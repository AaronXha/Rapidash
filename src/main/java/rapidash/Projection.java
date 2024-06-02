package rapidash;

import java.util.List;

public class Projection {
    private List<String> columns;
    private List<Integer> values;

    public Projection(List<String> columns, List<Integer> values){
        this.columns = columns;
        this.values = values;
    }

    public int getSize() {
        return columns.size();
    }

    public List<Integer> getValues() {
        return values;
    }
}
