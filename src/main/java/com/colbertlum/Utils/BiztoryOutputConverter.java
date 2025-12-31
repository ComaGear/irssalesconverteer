package com.colbertlum.Utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.colbertlum.entity.BiztoryOutputMoveOut;
import com.colbertlum.entity.BiztoryOutputResult;
import com.colbertlum.entity.DocSalesConverterResult;
import com.colbertlum.entity.MoveOut;
import com.colbertlum.entity.MoveOutDocResultGroupByDate;
import com.colbertlum.entity.MoveOutDocResults;

public class BiztoryOutputConverter {
    public static BiztoryOutputResult converting(DocSalesConverterResult docResults){
        BiztoryOutputResult bzResult = new BiztoryOutputResult();


        MoveOutDocResultGroupByDate cashDocGroupByDate = docResults.getCashDocGroupByDate();
        groupingIntoDate(bzResult, cashDocGroupByDate);
    }

    private void groupingIntoDate(BiztoryOutputResult bzResult, MoveOutDocResultGroupByDate moveoutDoc){
        Set<LocalDate> localDateSet = moveoutDoc.getMapKeySet();
        
        for(LocalDate date : localDateSet) {
            MoveOutDocResults moveOutDocResults = moveoutDoc.getMoveOutDocResultByLocalDate(date);
            List<BiztoryOutputMoveOut> biztoryOutputMoveOuts = new ArrayList<BiztoryOutputMoveOut>();
            moveOutDocResults.getDocList().stream().forEach(item -> {
                biztoryOutputMoveOuts.addAll(toBiztoryOutputMoveOuts(item.getMoveOuts())));
            });;
            bzResult.putCashDoc(date, biztoryOutputMoveOuts);
        }
        
    }

    private List<BiztoryOutputMoveOut> toBiztoryOutputMoveOuts(List<MoveOut> moveOuts) {
        converting list of MoveOut.java into biztory outputing ready BiztoryOutputMoveOuts
    }
}
