package rapidash;

import de.metanome.algorithms.dcfinder.denialconstraints.DenialConstraint;
import de.metanome.algorithms.dcfinder.predicates.Operator;
import de.metanome.algorithms.dcfinder.predicates.Predicate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchRange {

    public int k;//dimension
    public List<Integer> U;
    public List<Integer> L;
    public List<Integer> invertU;
    public List<Integer> invertL;

    public SearchRange(int k){//default search range
        this.k = k;
        U = new ArrayList<>(k);
        L = new ArrayList<>(k);
        invertL = new ArrayList<>();
        invertU = new ArrayList<>();
        for(int i = 0; i < k; i++){
            U.add(Integer.MAX_VALUE);
            L.add(Integer.MIN_VALUE);
            invertL.add(Integer.MAX_VALUE);
            invertU.add(Integer.MIN_VALUE);
        }
    }

    public SearchRange(int k, Tuple t, DenialConstraint dc){//generate search range for inequality predicates
        this(k);

        List<Predicate> nonEqualityPredicates = dc.getNonEqualityPredicates();
        for(Predicate p : nonEqualityPredicates){//process homogeneous predicates and heterogeneous predicates differently
            int index = nonEqualityPredicates.indexOf(p);
            if(p.getOperand1().getColumn() == p.getOperand2().getColumn()){
                homogeneousPredicate(t, p, index);
            }
            else{
                //inequality predicate 𝑠.𝐶 op 𝑡.𝐷 in 𝜑
                //first expand L and U for heterogeneous case
                //index for 𝑠.𝐶
                //index+1 for 𝑡.𝐷
                expandBounds(index);
                heterogeneousPredicate(t, p, index);
            }
        }

    }

    public void homogeneousPredicate(Tuple t, Predicate p, int index){
        if(p.getOperator() == Operator.LESS || p.getOperator() == Operator.LESS_EQUAL){
            int t1 = U.get(index);
            int t2 = t.getColValue(p.getOperand1().getColumn().getColumnName());
            U.set(index, Math.min(t1, t2));
        }
        if(p.getOperator() == Operator.GREATER || p.getOperator() == Operator.GREATER_EQUAL){
            L.set(index, Math.max(L.get(index), t.getColValue(p.getOperand1().getColumn().getColumnName())));
        }
        invertRange(index);

/*        invertU.set(index, L.get(index));
        for(Integer i : invertU)
            if(i == Integer.MIN_VALUE)
                invertU.set(U.indexOf(i), Integer.MAX_VALUE);

        invertL.addAll(U);
        for(Integer i : invertL)
            if(i == Integer.MAX_VALUE)
                invertL.set(L.indexOf(i), Integer.MIN_VALUE);*/
    }

    public void heterogeneousPredicate(Tuple t, Predicate p, int index){
        if(p.getOperator() == Operator.EQUAL){//heterogeneous equality case
            //split into inequalities
            Predicate p1 = new Predicate(Operator.LESS_EQUAL, p.getOperand1(), p.getOperand2());
            Predicate p2 = new Predicate(Operator.GREATER_EQUAL, p.getOperand1(), p.getOperand2());
            U.add(index+1, Integer.MAX_VALUE);
            L.add(index+1, Integer.MIN_VALUE);
            homogeneousPredicate(t, p1, index);
            homogeneousPredicate(t, p2, index+1);
        }
        //if op is < or ≤ then
        // U.𝐶 ← min{U.𝐶, 𝑟.𝐷}
        // L′.𝐷 ← max{L′.𝐷, 𝑟.𝐶}
        if(p.getOperator() == Operator.LESS || p.getOperator() == Operator.LESS_EQUAL){
            U.set(index, Math.min(U.get(index), t.getColValue(p.getOperand1().getColumn().getColumnName())));
            invertL.set(index+1, Math.max(invertL.get(index+1), t.getColValue(p.getOperand2().getColumn().getColumnName())));
        }
        //if op is > or ≥ then
        // L.𝐶 ← max{L.𝐶, 𝑟.𝐷}
        // U′.𝐷 ← min{U′.𝐷, 𝑟.𝐶}
        if(p.getOperator() == Operator.GREATER || p.getOperator() == Operator.GREATER_EQUAL){
            L.set(index, Math.max(L.get(index), t.getColValue(p.getOperand1().getColumn().getColumnName())));
            invertU.set(index+1, Math.min(invertU.get(index+1), t.getColValue(p.getOperand2().getColumn().getColumnName())));
        }
    }

    public void expandBounds(int index){
        L.add(index + 1, Integer.MIN_VALUE);
        U.add(index + 1, Integer.MAX_VALUE);
        invertL.add(index + 1, Integer.MIN_VALUE);
        invertU.add(index + 1, Integer.MAX_VALUE);
        invertL.set(index, Integer.MIN_VALUE);
        invertU.set(index, Integer.MAX_VALUE);
    }

    public void invertRange(int index){
        //invert range
        int tmp = L.get(index);
        if(tmp == Integer.MIN_VALUE)
            invertU.set(index, Integer.MAX_VALUE);
        else
            invertU.set(index, tmp);

        tmp = U.get(index);
        if(tmp == Integer.MAX_VALUE)
            invertL.set(index, Integer.MIN_VALUE);
        else
            invertL.set(index, tmp);
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
