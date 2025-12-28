package com.colbertlum.contentHandler.dataValidation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExpectedDataMap<T>{

    private Map<String, List<T>> map;

    public void addExpectDataSetForColumn(String column, List<T> list){
        if(map.containsKey(column)) throw new IllegalStateException("Column is already existed :" + column);

        map.put(column, list);
    }

    public List<T> getExpectDataSetForColumn(String column){
        Set<String> keySet = map.keySet();
        if(keySet.contains(column) ){
            return (List<T>) map.get(column);
        }
        
        return null;
    }

    public boolean hasExpectDataSetForColumn(String column){
        return  map.keySet().contains(column);
    }

    public ExpectedDataMap(){
        this.map = new HashMap<String, List<T>>();
    }
}
