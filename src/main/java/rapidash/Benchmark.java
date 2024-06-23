package rapidash;

import FastADC.FastADC;
import de.metanome.algorithms.dcfinder.denialconstraints.DenialConstraint;
import de.metanome.algorithms.dcfinder.input.Input;
import de.metanome.algorithms.dcfinder.input.RelationalInput;
import de.metanome.algorithms.dcfinder.predicates.Predicate;
import edu.wlu.cs.levy.CG.KeyDuplicateException;
import edu.wlu.cs.levy.CG.KeySizeException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

public class Benchmark {
    public static void main(String[] args) throws KeyDuplicateException, KeySizeException {
        String dataset = "column10(1)";
        String path = "../dataset/" + dataset + ".csv";
        Input input = new Input(new RelationalInput(path), -1);

        FastADC dcFinder = new FastADC(true, 0, 100, false);
        Set<DenialConstraint> dcs = dcFinder.buildApproxDCs(path, 100).getConstraints();

        for (DenialConstraint dc : dcs) {
            Verification.verify(input, dc);
        }
    }
}
