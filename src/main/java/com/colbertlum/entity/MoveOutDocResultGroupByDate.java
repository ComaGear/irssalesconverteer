package com.colbertlum.entity;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.colbertlum.constants.DateTimePattern;

public class MoveOutDocResultGroupByDate {
    private Map<LocalDate, MoveOutDocResults> map;

    // public Map<LocalDate, MoveOutDocResults> getMap() {
    //     return map;
    // }
    public Set<LocalDate> getMapKeySet(){
        return map.keySet();
    }

    public MoveOutDocResults getMoveOutDocResultByLocalDate(LocalDate date){
        return map.get(date);
    }

    public void putMoveOutDocResultByLocalDate(LocalDate date, MoveOutDocResults results) {
        if(map.containsKey(date)) {
            throw new IllegalStateException(
                "MoveOutDocResultGroupByDate is already contained : " + DateTimePattern.parseString(date));
        }

        map.put(date, results);
    }

    public MoveOutDocResultGroupByDate(){
        this.map = new HashMap<LocalDate, MoveOutDocResults>();
    }
}
