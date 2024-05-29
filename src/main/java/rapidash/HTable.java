package rapidash;

import java.util.HashMap;
import java.util.Map;

public class HTable {
    private Map<Projection, SearchTree> mapSearchTree;

    private Map<Projection, Integer> mapInteger;

    public HTable(){
        mapSearchTree = new HashMap<>();
        mapInteger = new HashMap<>();
    }

    public boolean contains(Projection p){
        return mapSearchTree.containsKey(p) || mapInteger.containsKey(p);
    }

    public void put(Projection p, SearchTree sTree){
        mapSearchTree.put(p, sTree);
    }

    public void put(Projection p, Integer i){
        mapInteger.put(p, i);
    }

    public SearchTree getSearchTree(Projection p){
        return mapSearchTree.get(p);
    }

    public Integer getInt(Projection p){
        return mapInteger.get(p);
    }

}
