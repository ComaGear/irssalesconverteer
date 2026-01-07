package com.colbertlum.Utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.colbertlum.entity.AutoCountOutputMoveOut;
import com.colbertlum.entity.AutoCountOutputResult;
import com.colbertlum.entity.DocSalesConverterResult;
import com.colbertlum.entity.MoveOut;
import com.colbertlum.entity.MoveOutDocResultGroupByDate;
import com.colbertlum.entity.MoveOutDocResults;

public class AutoCountOutputConverter {

    public static AutoCountOutputResult converting(DocSalesConverterResult docResults) {
        AutoCountOutputResult acResult = new AutoCountOutputResult();

        MoveOutDocResultGroupByDate cashDocGroupByDate = docResults.getCashDocGroupByDate();
        MoveOutDocResultGroupByDate specifyDocGroupByDate = docResults.getExcludeDocListGroupByDate();
        groupingCashIntoDate(acResult, cashDocGroupByDate);
        groupingSpecifyIntoDate(acResult, specifyDocGroupByDate);

        return acResult;
    }

    private static void groupingSpecifyIntoDate(AutoCountOutputResult acResult,
            MoveOutDocResultGroupByDate moveoutDoc) {
        Set<LocalDate> localDateSet = moveoutDoc.getMapKeySet();
        
        for(LocalDate date : localDateSet) {
            MoveOutDocResults moveOutDocResults = moveoutDoc.getMoveOutDocResultByLocalDate(date);
            List<AutoCountOutputMoveOut> autoCountOutputMoveOuts = new ArrayList<AutoCountOutputMoveOut>();
            moveOutDocResults.getDocList().stream().forEach(item -> {
                autoCountOutputMoveOuts.addAll(toAutoCountOutputMoveOuts(item.getMoveOuts()));
                acResult.putSpecifyDoc(date, item.getId(), autoCountOutputMoveOuts);
            });
        }
    }

    private static void groupingCashIntoDate(AutoCountOutputResult acResult, MoveOutDocResultGroupByDate moveoutDoc){
        Set<LocalDate> localDateSet = moveoutDoc.getMapKeySet();
        
        for(LocalDate date : localDateSet) {
            MoveOutDocResults moveOutDocResults = moveoutDoc.getMoveOutDocResultByLocalDate(date);
            List<AutoCountOutputMoveOut> autoCountOutputMoveOuts = new ArrayList<AutoCountOutputMoveOut>();
            moveOutDocResults.getDocList().stream().forEach(item -> {
                autoCountOutputMoveOuts.addAll(toAutoCountOutputMoveOuts(item.getMoveOuts()));
            });;
            acResult.putCashDoc(date, autoCountOutputMoveOuts);
        }
    }

    private static List<AutoCountOutputMoveOut> toAutoCountOutputMoveOuts(List<MoveOut> moveOuts) {
        List<AutoCountOutputMoveOut> list = new ArrayList<AutoCountOutputMoveOut>();
        for(MoveOut moveOut : moveOuts) {
            AutoCountOutputMoveOut acMoveOut = new AutoCountOutputMoveOut();
            acMoveOut.setId(moveOut.getProductId() + " (" + moveOut.getUom() + ")");
            acMoveOut.setName(moveOut.getProductName());
            acMoveOut.setPrice(moveOut.getTotalAmount() / moveOut.getQuantity());
            acMoveOut.setQuantity(moveOut.getQuantity());
            acMoveOut.setUom("");

            list.add(acMoveOut);
        }
        return list;
    }

}
