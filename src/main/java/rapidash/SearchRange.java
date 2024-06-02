package rapidash;

import de.metanome.algorithms.dcfinder.denialconstraints.DenialConstraint;
import de.metanome.algorithms.dcfinder.predicates.Operator;
import de.metanome.algorithms.dcfinder.predicates.Predicate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchRange {
    public int k;
    public List<Integer> U;
    public List<Integer> L;
    public List<Integer> invertU;
    public List<Integer> invertL;

    public SearchRange(int k, Tuple t, DenialConstraint dc){
        U = new ArrayList<>(k);
        L = new ArrayList<>(k);
        invertL = new ArrayList<>();
        invertU = new ArrayList<>();
        Collections.fill(U, Integer.MAX_VALUE);
        Collections.fill(L, Integer.MIN_VALUE);
        List<Predicate> nonEqualityPredicates = dc.getNonEqualityPredicates();
        for(Predicate p : nonEqualityPredicates){
            int index = nonEqualityPredicates.indexOf(p);
            if(p.getOperator() == Operator.LESS || p.getOperator() == Operator.LESS_EQUAL){
                U.set(index, Math.min(U.get(index), t.getColValue(p.getOperand1().getColumn().getColumnName())));
            }
            if(p.getOperator() == Operator.GREATER || p.getOperator() == Operator.GREATER_EQUAL){
                L.set(index, Math.max(L.get(index), t.getColValue(p.getOperand1().getColumn().getColumnName())));
            }
        }
        invertU.addAll(L);
        for(Integer i : invertU)
            if(i == Integer.MIN_VALUE)
                invertU.set(U.indexOf(i), Integer.MAX_VALUE);

        invertL.addAll(U);
        for(Integer i : invertL)
            if(i == Integer.MAX_VALUE)
                invertL.set(L.indexOf(i), Integer.MIN_VALUE);
    }

    public int getK() {
        return k;
    }

    public List<Integer> getU() {
        return U;
    }

    public List<Integer> getL() {
        return L;
    }

    public List<Integer> getInvertU() {
        return invertU;
    }

    public List<Integer> getInvertL() {
        return invertL;
    }
}
