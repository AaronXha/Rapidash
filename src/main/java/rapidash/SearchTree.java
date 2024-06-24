package rapidash;

import edu.wlu.cs.levy.CG.KDTree;
import edu.wlu.cs.levy.CG.KeyDuplicateException;
import edu.wlu.cs.levy.CG.KeySizeException;

import java.util.List;

public class SearchTree {

    private KDTree<Object> kt;
    private int dims;

    public SearchTree(int dims) {
        this.dims = dims;
        this.kt = new KDTree<Object>(dims);
    }


    public boolean booleanRangeSearch(SearchRange range) throws KeySizeException {
        long t0 = System.currentTimeMillis();
        int k = range.getK();
        double[] L = transform(k, range.getL());
        double[] U = transform(k, range.getU());
        double[] invertL = transform(k, range.getInvertL());
        double[] invertU = transform(k, range.getInvertU());
        boolean flag = !kt.range(L, U).isEmpty() && !kt.range(invertL, invertU).isEmpty();
        Benchmark.time_search += System.currentTimeMillis() - t0;
        return flag;
    }

    public void insert(Projection p) throws KeyDuplicateException, KeySizeException {
        long t0 = System.currentTimeMillis();
        kt.insert(transform(p.getSize(), p.getValues()), new Object());
        Benchmark.time_insert += System.currentTimeMillis() - t0;
    }

    public double[] transform(int k, List<Integer> list) {
        double[] array = new double[k];
        for(int i = 0; i < k; i++)
            array[i] = list.get(i).doubleValue();
        return array;
    }
}
