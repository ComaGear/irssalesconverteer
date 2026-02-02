package com.colbertlum.Utils.tabReportContextConvertUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.colbertlum.IrsSalesConverterApplication;
import com.colbertlum.Utils.UOMUtils;
import com.colbertlum.entity.AutoCountOutputResult;
import com.colbertlum.entity.DocSalesConverterResult;
import com.colbertlum.entity.MoveOut;
import com.colbertlum.entity.MoveOutDocResultGroupByDate;
import com.colbertlum.entity.MoveOutDocResults;
import com.colbertlum.entity.UOM;
import com.colbertlum.entity.tabReportContext.AutoCountOutputMoveOut;

public class AutoCountOutputConverter {

    private static List<UOM> uoms;

    {
        uoms = IrsSalesConverterApplication.getContext().getUom();
    }


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
            
            acMoveOut.setId(UOMUtils.hasMutliUom(uoms, moveOut.getProductId())
                ? moveOut.getProductId() + " (" + moveOut.getUom() + ")"
                : moveOut.getProductId());
            acMoveOut.setName(moveOut.getProductName());
            acMoveOut.setPrice(moveOut.getTotalAmount() / moveOut.getQuantity());
            acMoveOut.setQuantity(moveOut.getQuantity());
            acMoveOut.setUom("");

            list.add(acMoveOut);
        }
        return list;
    }

}
