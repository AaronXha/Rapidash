package rapidash;

import de.metanome.algorithms.dcfinder.denialconstraints.DenialConstraint;
import de.metanome.algorithms.dcfinder.input.Input;

import de.metanome.algorithms.dcfinder.predicates.Operator;
import de.metanome.algorithms.dcfinder.predicates.Predicate;
import de.metanome.algorithms.dcfinder.predicates.sets.PredicateSet;

import edu.wlu.cs.levy.CG.KeyDuplicateException;
import edu.wlu.cs.levy.CG.KeySizeException;

import java.util.List;

public class Verification {

    public boolean verify(Input input, DenialConstraint dc) throws KeyDuplicateException, KeySizeException {
        //first split dcs with disequality(namely <> or unequal) predicates into inequality
        PredicateSet predicates = dc.getPredicateSet();
        for(Predicate p : predicates){
            if(p.getOperator() == Operator.UNEQUAL){
                predicates.remove(p);
                //split and verify recursively
                //split s.C <> t.D into the conjunction s.C < t.D or s.C > t.D
                PredicateSet predicates1 = new PredicateSet(predicates);
                predicates1.add(new Predicate(Operator.LESS, p.getOperand1(), p.getOperand2()));
                PredicateSet predicates2 = new PredicateSet(predicates);
                predicates2.add(new Predicate(Operator.GREATER, p.getOperand1(), p.getOperand2()));
                DenialConstraint dc1 = new DenialConstraint(predicates1);
                DenialConstraint dc2 = new DenialConstraint(predicates2);
                return verify(input, dc1) || verify(input, dc2);
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
                if(!sTree.booleanRangeSearch(sRange))
                    return false;
                sTree.insert(t.getProjection(dc.getNonEqualityColumns()));
            } else {
                Integer i = hTable.getInt(p);
                if(i > 0)
                    return false;
                hTable.put(p,i + 1);
            }
        }
        return true;
    }


}
