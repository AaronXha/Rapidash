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
import java.util.List;
import java.util.Set;

public class Benchmark {

    static long time_insert = 0;
    static long time_search = 0;
    static long time_projection = 0;
    static long time_rangeTree = 0;
    static long time_getColumns = 0;


    static long verify = 0;
    public static void main(String[] args) throws KeyDuplicateException, KeySizeException {
        String path ="dataset/airport.csv";
        Input input = new Input(new RelationalInput(path), -1);

        FastADC dcFinder = new FastADC(true, 0, 100, false);
        Set<DenialConstraint> dcs = dcFinder.buildApproxDCs(path, 100).getConstraints();

        List<Tuple> tuples = input.buildTuples();

        long t0 = System.currentTimeMillis();
        for (DenialConstraint dc : dcs) {
            Verification.verify(tuples, dc);
        }
        System.out.println(System.currentTimeMillis() - t0 + "ms");
        System.out.println("time_search " + time_search + "ms");
        System.out.println("time_insert " + time_insert + "ms");
        System.out.println("time_projection " + time_projection + "ms");
        System.out.println("time_rangeTree " + time_rangeTree + "ms");
        System.out.println("time_HTablePut " + HTable.time_HTablePut + "ms");
        System.out.println("time_HTableGet " + HTable.time_HTableGet + "ms");
        System.out.println("time_HTableContain " + HTable.time_HTableContain + "ms");
        System.out.println("time_getColumns " + time_getColumns + "ms");
        System.out.println(verify);
    }
}
