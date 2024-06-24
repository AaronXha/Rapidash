package rapidash;

import de.metanome.algorithms.dcfinder.denialconstraints.DenialConstraint;
import de.metanome.algorithms.dcfinder.input.Input;
import de.metanome.algorithms.dcfinder.predicates.Operator;
import de.metanome.algorithms.dcfinder.predicates.Predicate;
import de.metanome.algorithms.dcfinder.predicates.operands.ColumnOperand;

import java.util.HashMap;
import java.util.List;

/**
 * @Author: LYC
 * @Create: 2024/6/7 16:31
 */
public class Verification3 {

    public boolean verify(List<Tuple> tuples, DenialConstraint dc, Predicate p) {
        // Initialize hash tables

        HashMap<Projection, Double> minA = new HashMap<>();
        HashMap<Projection, Double> maxA = new HashMap<>();
        HashMap<Projection, Double> minB = new HashMap<>();
        HashMap<Projection, Double> maxB = new HashMap<>();

        for (Tuple t : tuples) {
            Projection v = t.getProjection(dc.getEqualityColumns());

            if (!minA.containsKey(v)) {
                minA.put(v, Double.POSITIVE_INFINITY);
                maxA.put(v, Double.NEGATIVE_INFINITY);
                minB.put(v, Double.POSITIVE_INFINITY);
                maxB.put(v, Double.NEGATIVE_INFINITY);
            }
            ColumnOperand A = p.getOperand1();
            ColumnOperand B = p.getOperand2();
            int idxA = A.getColumn().getIndex();
            int idxB = B.getColumn().getIndex();




            double tA = t.getValues().get(idxA);
            double tB = t.getValues().get(idxB);
            Operator op = p.getOperator();
            double minA_v =  minA.get(v);
            double minB_v =  minB.get(v);
            double maxA_v =  maxA.get(v);
            double maxB_v =  maxB.get(v);




            // Check for violations
            if ((op.containLess() && op.eval(minA_v,tB))
                ||
                (op.containGreat() && op.eval(maxA_v,tB)))
            {
                return false;
            }
            if ((op.containLess() && op.eval(tA,maxB_v))
                    ||
                    (op.containGreat()&& op.eval(tA,minB_v)))
            {
                return false;
            }

            // Update min and max values
            minA.put(v, Math.min(minA_v, tA));
            minB.put(v, Math.min(minB_v, tB));
            maxA.put(v, Math.max(maxA_v, tA));
            maxB.put(v, Math.max(maxB_v, tB));
        }

        return true;
    }



}
