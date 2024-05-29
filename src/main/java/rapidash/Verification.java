package rapidash;

import de.metanome.algorithms.dcfinder.input.Input;

import java.util.List;

public class Verification {
    public Verification(Input input, DenialConstraint dc){
        int k = dc.getNonEqualityCount();
        List<String> equalityColumns = dc.getEqualityColumns();
        HTable hTable = new HTable();
        List<Tuple> tuples = input.getTuples();

        for (Tuple t : tuples){
            Projection p = t.getProjection(equalityColumns);
            if (!hTable.contains(p)) {
                if (k == 0) {
                    hTable.put(p, 0);
                } else {
                    hTable.put(p, new SearchTree());
                }
            }
            if(k != 0) {
                SearchTree sTree = hTable.getSearchTree(p);
            }
        }
    }

}
