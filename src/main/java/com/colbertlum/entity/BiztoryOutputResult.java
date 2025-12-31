package com.colbertlum.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public void putSpecifyDoc(LocalDate date, String docNo, List<BiztoryOutputMoveOut> moveOuts) {
        if(specifyDocMap.get(date) == null) {
            specifyDocMap.put(date, new HashMap<String, List<BiztoryOutputMoveOut>>());
        }

        specifyDocMap.get(date).put(docNo, moveOuts);
    }

    // Get list of biztoryOutputMoveOut from sepecify Doc and localated Date
    public List<BiztoryOutputMoveOut> getSpecifyMoveOuts(LocalDate date, String DocNo) {
        return specifyDocMap.get(date).get(DocNo);
    }

    // Get all move outs from specify but split by LocalDate
    public List<BiztoryOutputMoveOut> getAllSpecifyDocListByLocalDate(LocalDate date) {
        List<BiztoryOutputMoveOut> list = new ArrayList<BiztoryOutputMoveOut>();
        Map<String, List<BiztoryOutputMoveOut>> localDateMap = specifyDocMap.get(date);
        Set<String> localDateKeySet = localDateMap.keySet();
        for(String key : localDateKeySet) {
            list.addAll(localDateMap.get(key));
        }

        return list;
    }

    // get KeySet of String from entry of specifyDocMap by LocalDate.
    // it used to before getSpecifyMoveOuts to get a list of DocNo Strings.
    public Set<String> getSpecifyDocNoKeySet(LocalDate date){
        return specifyDocMap.get(date).keySet();
    }

    public BiztoryOutputResult(){
        cashDocMap = new HashMap<LocalDate, List<BiztoryOutputMoveOut>>();
        specifyDocMap = new HashMap<LocalDate, Map<String, List<BiztoryOutputMoveOut>>>();
    }
}