package de.metanome.algorithms.dcfinder.denialconstraints;

import ch.javasoft.bitset.LongBitSet;
import de.metanome.algorithms.dcfinder.predicates.Operator;
import de.metanome.algorithms.dcfinder.predicates.Predicate;
import de.metanome.algorithms.dcfinder.predicates.sets.PredicateSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class DenialConstraint {

    private final PredicateSet predicateSet;

    public DenialConstraint(PredicateSet predicateSet) {
        this.predicateSet = predicateSet;
       // initialize();
    }

    public DenialConstraint(LongBitSet predicates) {
        this.predicateSet = new PredicateSet(predicates);
        //initialize();
    }

    public void initialize() {
        for(Predicate p:predicateSet) {
            if (p.getOperator() == Operator.EQUAL)
                counts[0]++;
            else if(p.getOperator() == Operator.UNEQUAL)
                counts[2]++;
            else{
                counts[1]++;
                singleInequality = p;
            }
        }
        if(counts[1] != 1)
            singleInequality = null;
    }
    public boolean containsPredicate(Predicate p) {
        return predicateSet.containsPredicate(p) || predicateSet.containsPredicate(p.getSymmetric());
    }

    public DenialConstraint getInvT1T2DC() {
        return new DenialConstraint(predicateSet.getInvT1T2());
    }

    public PredicateSet getPredicateSet() {
        return predicateSet;
    }

    public int getPredicateCount() {
        return predicateSet.size();
    }

    public int[] counts = new int[3];//0 for equality, 1 for inequality, 2 for unEquality

    public Predicate singleInequality;

    public List<Predicate> getPredicates() {
        List<Predicate> predicates = new ArrayList<>();
        for(Predicate p : predicateSet) {
            predicates.add(p);
        }
        return  predicates;
    }

    public List<Predicate> getEqualityPredicates() {
        List<Predicate> equalityPredicates = new ArrayList<>();
        for (Predicate p : predicateSet) {
            if (p.getOperator() == Operator.EQUAL)
                equalityPredicates.add(p);
        }
        return equalityPredicates;
    }

    public int getEqualityCount() {
        return getEqualityPredicates().size();
    }

    public List<Predicate> getNonEqualityPredicates() {
        List<Predicate> nonEqualityPredicates = new ArrayList<>();
        for (Predicate p : predicateSet) {
            if (p.getOperator() != Operator.EQUAL)
                nonEqualityPredicates.add(p);
        }
        return nonEqualityPredicates;
    }


    public int getNonEqualityCount() {
        return getNonEqualityPredicates().size();
    }

    public List<Predicate> getInEqualityPredicate(){
        List<Predicate> inEqualityPredicates = new ArrayList<>();
        for (Predicate p : predicateSet) {
            if (p.getOperator() != Operator.EQUAL && p.getOperator() != Operator.UNEQUAL)
                inEqualityPredicates.add(p);
        }
        return inEqualityPredicates;
    }

    public List<Predicate> getUnEquality(){
        List<Predicate> unEqualityPredicates = new ArrayList<>();
        for (Predicate p : predicateSet) {
            if (p.getOperator() == Operator.UNEQUAL)
                unEqualityPredicates.add(p);
        }
        return unEqualityPredicates;
    }

    public int getUnEqualityCount(){
        return getUnEquality().size();
    }

    public int getInEqualityCount(){
        return getInEqualityPredicate().size();
    }

    public List<String> getEqualityColumns() {
        Set<String> equalityColumns = new HashSet<>();
        for(Predicate p : getEqualityPredicates()){
            equalityColumns.add(p.getOperand1().getColumn().getColumnName());
            equalityColumns.add(p.getOperand2().getColumn().getColumnName());
        }
        return new ArrayList<>(equalityColumns);
    }

    public List<String> getNonEqualityColumns() {
        HashSet<String> nonEqualityColumns = new HashSet<>();
        for(Predicate p : getNonEqualityPredicates()){
            nonEqualityColumns.add(p.getOperand1().getColumn().getColumnName());
            nonEqualityColumns.add(p.getOperand2().getColumn().getColumnName());
        }
        return new ArrayList<>(nonEqualityColumns);
    }


    //判断两个dcs是否相等。
    private boolean containedIn(PredicateSet otherPS) {
        for (Predicate p : predicateSet) {
            if (!otherPS.containsPredicate(p) && !otherPS.containsPredicate(p.getSymmetric()))
                return false;
        }
        return true;
    }


    @Override
    public int hashCode() {
        int result1 = 0;
        for (Predicate p : predicateSet)
            result1 += Math.max(p.hashCode(), p.getSymmetric().hashCode());

        int result2 = 0;
        if (getInvT1T2DC() != null)
            for (Predicate p : getInvT1T2DC().predicateSet)
                result2 += Math.max(p.hashCode(), p.getSymmetric().hashCode());

        return Math.max(result1, result2);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        DenialConstraint other = (DenialConstraint) obj;
        if (predicateSet == null) {
            return other.predicateSet == null;
        } else if (predicateSet.size() != other.predicateSet.size()) {
            return false;
        } else {
            PredicateSet otherPS = other.predicateSet;
            return containedIn(otherPS) || getInvT1T2DC().containedIn(otherPS) || containedIn(other.getInvT1T2DC().predicateSet);
        }
    }


    public static final String NOT = "\u00AC";
    public static final String AND = " ∧ ";

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(NOT + "{ ");
        int count = 0;
        for (Predicate predicate : this.predicateSet) {
            if (count == 0)
                sb.append(predicate.toString());
            else
                sb.append(AND + predicate.toString());

            count++;
        }
        sb.append(" }");
        return sb.toString();
    }

    public DenialConstraint modifyPredicate(Predicate p, Operator op){
        predicateSet.modifyPredicate(p, op);
        return this;
    }
}
