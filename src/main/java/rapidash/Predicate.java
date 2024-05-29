package rapidash;

import de.metanome.algorithms.dcfinder.predicates.Operator;
import de.metanome.algorithms.dcfinder.predicates.operands.ColumnOperand;

import java.util.List;

public class Predicate {
    private Operator op;
    private String operand1;
    private String operand2;

    public Predicate(Operator op, String operand1, String operand2) {
        if (op == null)
            throw new IllegalArgumentException("Operator must not be null.");

        if (operand1 == null)
            throw new IllegalArgumentException("First operand must not be null.");

        if (operand2 == null)
            throw new IllegalArgumentException("Second operand must not be null.");

        this.op = op;
        this.operand1 = operand1;
        this.operand2 = operand2;
    }

    public Operator getOperator() {
        return this.op;
    }

    public String getOperand1() {
        return this.operand1;
    }

    public  String getOperand2() {
        return this.operand2;
    }
}
