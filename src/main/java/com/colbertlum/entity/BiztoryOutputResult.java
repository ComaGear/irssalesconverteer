package com.colbertlum.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class BiztoryOutputResult {
    private Map<LocalDate, List<BiztoryOutputMoveOut>> cashDocMap;
    private Map<LocalDate, Map<String, List<BiztoryOutputMoveOut>>> specifyDocMap;

    // Add cashDocMap entry
    public void putCashDoc(LocalDate date, List<BiztoryOutputMoveOut> moves) {
        cashDocMap.put(date, moves);
    }

    // Get cashDocMap entry by date
    public List<BiztoryOutputMoveOut> getCashDoc(LocalDate date) {
        return cashDocMap.get(date);
    }

    // Get specifyDocMap inner map by date
    public Map<String, List<BiztoryOutputMoveOut>> getSpecifyDocMap(LocalDate date) {
        return specifyDocMap.get(date);
    }

    specifyDocMap is get the Map<String, List<BiztoryOutputMoveOut>> by LocalDate Key.

    it will return a keySet of  Map<String, List<BiztoryOutputMoveOut>>,

    then get List<BiztoryOutputMoveOut> from BiztoryOutputResult 
}