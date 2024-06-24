package rapidash;

import de.metanome.algorithms.dcfinder.denialconstraints.DenialConstraint;
import de.metanome.algorithms.dcfinder.input.Input;

import de.metanome.algorithms.dcfinder.predicates.Operator;
import de.metanome.algorithms.dcfinder.predicates.Predicate;
import de.metanome.algorithms.dcfinder.predicates.operands.ColumnOperand;
import de.metanome.algorithms.dcfinder.predicates.sets.PredicateSet;

import edu.wlu.cs.levy.CG.KeyDuplicateException;
import edu.wlu.cs.levy.CG.KeySizeException;

import java.util.HashMap;
import java.util.List;

public class Verification {

    public static boolean verify(List<Tuple> tuples, DenialConstraint dc) throws KeyDuplicateException, KeySizeException {
        Benchmark.verify++;
        //first split dcs with disequality(namely <> or unequal) predicates into inequality
        for(Predicate predicate : dc.getPredicateSet()){
            if(predicate.getOperator() == Operator.UNEQUAL){
                return verify(tuples, dc.modifyPredicate(predicate, Operator.LESS)) || verify(tuples, dc.modifyPredicate(predicate.setOperator(Operator.LESS), Operator.GREATER));
            }
        }

        int k = dc.getNonEqualityCount();
        HTable hTable = new HTable();

        for (Tuple t : tuples){
            long t1 = System.currentTimeMillis();
            List<String> equalityColumns = dc.getEqualityColumns();
            Benchmark.time_getColumns += System.currentTimeMillis() - t1;
            Projection p = t.getProjection(equalityColumns);
            if (!hTable.contains(p)) {
                if (k == 0) {//zero inequality case
                    hTable.put(p,0);
                } else {//inequality case
                    hTable.put(p, new SearchTree(k));
                }
            }
            if(k != 0) {
                SearchTree sTree = hTable.getSearchTree(p);
                SearchRange sRange = new SearchRange(k, t, dc);
                if(!sTree.booleanRangeSearch(sRange)){
                    return false;
                }
                long t2 = System.currentTimeMillis();
                List<String> nonEqualityColumns = dc.getNonEqualityColumns();
                Benchmark.time_getColumns += System.currentTimeMillis() - t2;
                sTree.insert(t.getProjection(nonEqualityColumns));
            } else {
                Integer i = hTable.getInt(p);
                if(i > 0){
                    return false;
                }
                hTable.put(p,i + 1);
            }
        }
        return true;
    }



}
