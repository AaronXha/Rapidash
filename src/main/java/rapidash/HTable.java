package rapidash;

import java.util.HashMap;
import java.util.Map;

public class HTable {

    static long time_HTablePut = 0;
    static long time_HTableGet = 0;
    static long time_HTableContain = 0;
    private Map<Projection, SearchTree> mapSearchTree;

    private Map<Projection, Integer> mapInteger;

    public HTable(){
        mapSearchTree = new HashMap<>();
        mapInteger = new HashMap<>();
    }

    public boolean contains(Projection p){
        long t0 = System.currentTimeMillis();
        boolean flag =  mapSearchTree.containsKey(p) || mapInteger.containsKey(p);
        time_HTableContain += System.currentTimeMillis() - t0;
        return flag;
    }

    public void put(Projection p, SearchTree sTree){
        long t0 = System.currentTimeMillis();
        mapSearchTree.put(p, sTree);
        time_HTablePut += System.currentTimeMillis() - t0;
    }

    public void put(Projection p, Integer i){
        long t0 = System.currentTimeMillis();
        mapInteger.put(p, i);
        time_HTablePut += System.currentTimeMillis() - t0;
    }

    public SearchTree getSearchTree(Projection p){
        long t0 = System.currentTimeMillis();
        SearchTree st = mapSearchTree.get(p);
        time_HTableGet += System.currentTimeMillis() - t0;
        return st;
    }

    public Integer getInt(Projection p){
        long t0 = System.currentTimeMillis();
        Integer i = mapInteger.get(p);
        time_HTableGet += System.currentTimeMillis() - t0;
        return i;
    }

}
