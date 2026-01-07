package com.colbertlum.Utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.colbertlum.entity.BiztoryOutputMoveOut;
import com.colbertlum.entity.BiztoryOutputResult;
import com.colbertlum.entity.DocSalesConverterResult;
import com.colbertlum.entity.MoveOut;
import com.colbertlum.entity.MoveOutDocResultGroupByDate;
import com.colbertlum.entity.MoveOutDocResults;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BiztoryOutputConverter {
    public static BiztoryOutputResult converting(DocSalesConverterResult docResults){
        BiztoryOutputResult bzResult = new BiztoryOutputResult();


        MoveOutDocResultGroupByDate cashDocGroupByDate = docResults.getCashDocGroupByDate();
        MoveOutDocResultGroupByDate specifyDocGroupByDate = docResults.getExcludeDocListGroupByDate();
        groupingCashIntoDate(bzResult, cashDocGroupByDate);
        groupingSpecifyIntoDate(bzResult, specifyDocGroupByDate);

        return bzResult;
    }

    private static void groupingSpecifyIntoDate(BiztoryOutputResult bzResult,
            MoveOutDocResultGroupByDate moveoutDoc) {
        Set<LocalDate> localDateSet = moveoutDoc.getMapKeySet();
        
        for(LocalDate date : localDateSet) {
            MoveOutDocResults moveOutDocResults = moveoutDoc.getMoveOutDocResultByLocalDate(date);
            List<BiztoryOutputMoveOut> biztoryOutputMoveOuts = new ArrayList<BiztoryOutputMoveOut>();
            moveOutDocResults.getDocList().stream().forEach(item -> {
                biztoryOutputMoveOuts.addAll(toBiztoryOutputMoveOuts(item.getMoveOuts()));
                bzResult.putSpecifyDoc(date, item.getId(), biztoryOutputMoveOuts);
            });
        }
    }

    private static void groupingCashIntoDate(BiztoryOutputResult bzResult, MoveOutDocResultGroupByDate moveoutDoc){
        Set<LocalDate> localDateSet = moveoutDoc.getMapKeySet();
        
        for(LocalDate date : localDateSet) {
            MoveOutDocResults moveOutDocResults = moveoutDoc.getMoveOutDocResultByLocalDate(date);
            List<BiztoryOutputMoveOut> biztoryOutputMoveOuts = new ArrayList<BiztoryOutputMoveOut>();
            moveOutDocResults.getDocList().stream().forEach(item -> {
                biztoryOutputMoveOuts.addAll(toBiztoryOutputMoveOuts(item.getMoveOuts()));
            });;
            bzResult.putCashDoc(date, biztoryOutputMoveOuts);
        }
    }

    private static List<BiztoryOutputMoveOut> toBiztoryOutputMoveOuts(List<MoveOut> moveOuts) {
        List<BiztoryOutputMoveOut> list = new ArrayList<BiztoryOutputMoveOut>();
        for(MoveOut moveOut : moveOuts) {
            BiztoryOutputMoveOut bzMoveOut = new BiztoryOutputMoveOut();
            bzMoveOut.setId(moveOut.getProductId());
            bzMoveOut.setName(moveOut.getProductName());
            bzMoveOut.setPrice(moveOut.getTotalAmount() / moveOut.getQuantity());
            bzMoveOut.setQuantity(moveOut.getQuantity());
            bzMoveOut.setUom("");

            list.add(bzMoveOut);
        }
        return list;
    }
}
