package com.colbertlum.entity;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AutoCountOutputResult {
    private Map<LocalDate, List<AutoCountOutputMoveOut>> cashDocMap;
        private Map<LocalDate, Map<String, List<AutoCountOutputMoveOut>>> specifyDocMap;

        // Add cashDocMap entry
        public void putCashDoc(LocalDate date, List<AutoCountOutputMoveOut> moves) {
            cashDocMap.put(date, moves);
        }

        // Get cashDocMap entry by date
        public List<AutoCountOutputMoveOut> getCashDoc(LocalDate date) {
            return cashDocMap.get(date);
        }

        public void putSpecifyDoc(LocalDate date, String docNo, List<AutoCountOutputMoveOut> moveOuts) {
            if(specifyDocMap.get(date) == null) {
                specifyDocMap.put(date, new HashMap<String, List<AutoCountOutputMoveOut>>());
            }

            specifyDocMap.get(date).put(docNo, moveOuts);
        }

        // Get list of biztoryOutputMoveOut from sepecify Doc and localated Date
        public List<AutoCountOutputMoveOut> getSpecifyMoveOuts(LocalDate date, String DocNo) {
            return specifyDocMap.get(date).get(DocNo);
        }

        // Get all move outs from specify but split by LocalDate
        // public List<BiztoryOutputMoveOut> getAllSpecifyDocListByLocalDate(LocalDate date) {
        //     List<BiztoryOutputMoveOut> list = new ArrayList<BiztoryOutputMoveOut>();
        //     Map<String, List<BiztoryOutputMoveOut>> localDateMap = specifyDocMap.get(date);
        //     Set<String> localDateKeySet = localDateMap.keySet();
        //     for(String key : localDateKeySet) {
        //         list.addAll(localDateMap.get(key));
        //     }

        //     return list;
        // }

        // get KeySet of String from entry of specifyDocMap by LocalDate.
        // it used to before getSpecifyMoveOuts to get a list of DocNo Strings.
        public Set<String> getSpecifyDocNoKeySet(LocalDate date){
            return specifyDocMap.get(date).keySet();
        }

        public Set<LocalDate> cashDocLocalDates(){
            return cashDocMap.keySet();
        }

        public Set<LocalDate> specifyDocLocalDates(){
            return specifyDocMap.keySet();
        }

        public AutoCountOutputResult(){
            cashDocMap = new HashMap<LocalDate, List<AutoCountOutputMoveOut>>();
            specifyDocMap = new HashMap<LocalDate, Map<String, List<AutoCountOutputMoveOut>>>();
        }
}
