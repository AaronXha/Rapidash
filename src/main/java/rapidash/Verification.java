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

    public static boolean verify(Input input, DenialConstraint dc) throws KeyDuplicateException, KeySizeException {
        //first split dcs with disequality(namely <> or unequal) predicates into inequality
        for(Predicate predicate : dc.getPredicateSet()){
            if(predicate.getOperator() == Operator.UNEQUAL){
                return verify(input, dc.modifyPredicate(predicate, Operator.LESS)) || verify(input, dc.modifyPredicate(predicate.setOperator(Operator.LESS), Operator.GREATER));
            }
        }

        int k = dc.getNonEqualityCount();
        HTable hTable = new HTable();
        List<Tuple> tuples = input.buildTuples();

        for (Tuple t : tuples){
            Projection p = t.getProjection(dc.getEqualityColumns());
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
                    return false;}
                sTree.insert(t.getProjection(dc.getNonEqualityColumns()));
            } else {
                Integer i = hTable.getInt(p);
                if(i > 0){
                    return false;}
                hTable.put(p,i + 1);
            }
        }
        return true;
    }



}
