package rapidash;

import FastADC.FastADC;
import de.metanome.algorithms.dcfinder.denialconstraints.DenialConstraint;
import de.metanome.algorithms.dcfinder.input.Input;
import de.metanome.algorithms.dcfinder.input.RelationalInput;
import de.metanome.algorithms.dcfinder.predicates.Predicate;
import edu.wlu.cs.levy.CG.KeyDuplicateException;
import edu.wlu.cs.levy.CG.KeySizeException;

import java.util.List;
import java.util.Set;

/**
 * @Author: LYC
 * @Create: 2024/6/7 18:16
 */
public class test_for_v3 {
    public static void main(String[] args) throws KeyDuplicateException, KeySizeException {
        String path ="dataset/airport.csv";
        Input input = new Input(new RelationalInput(path), -1);

        FastADC dcFinder = new FastADC(true, 0, 100, false);
        Set<DenialConstraint> dcs = dcFinder.buildApproxDCs(path, 100).getConstraints();


        for(DenialConstraint dc: dcs){
            if(dc.getNonEqualityPredicates().size() == 1&&dc.getEqualityPredicates().size() != 0 ){
                Verification3 verify = new Verification3();
                List<Predicate> l =  dc.getNonEqualityPredicates();
                System.out.println(dc);
                System.out.println(l);
                System.out.println(verify.verify(input, dc,l.get(0)));

            }

        }
    }
}
