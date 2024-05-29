package rapidash;

import de.metanome.algorithms.dcfinder.predicates.Operator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DenialConstraint {
    private List<Predicate> predicates;

    public DenialConstraint(){
        predicates = new ArrayList<>();
    }

    public List<Predicate> getPredicates() {
        return this.predicates;
    }

    public int getPredicateCount() {
        return this.predicates.size();
    }

    public List<Predicate> getEqualityPredicates() {
        List<Predicate> equalityPredicates = new ArrayList<>();
        for (Predicate p : predicates) {
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
        for (Predicate p : predicates) {
            if (p.getOperator() != Operator.EQUAL)
                nonEqualityPredicates.add(p);
        }
        return nonEqualityPredicates;
    }


    public int getNonEqualityCount() {
        return getNonEqualityPredicates().size();
    }

    public List<String> getEqualityColumns() {
        HashSet<String> equalityColumns = new HashSet<>();
        for(Predicate p : getEqualityPredicates()){
            equalityColumns.add(p.getOperand1());
            equalityColumns.add(p.getOperand2());
        }
        return new ArrayList<>(equalityColumns);
    }
}
