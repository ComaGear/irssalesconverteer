package com.colbertlum.entity;

import java.util.List;

public class DocSalesConverterResult {

    private MoveOutDocResultGroupByDate cashDocGroupByDate;
    private MoveOutDocResultGroupByDate excludeDocListGroupByDate;

    private List<MoveOut> unfoundMoveOuts;

    public List<MoveOut> getUnfoundMoveOuts() {
        return unfoundMoveOuts;
    }


    public void setUnfoundMoveOuts(List<MoveOut> unfoundMoveOuts) {
        this.unfoundMoveOuts = unfoundMoveOuts;
    }


    public MoveOutDocResultGroupByDate getCashDocGroupByDate() {
        return cashDocGroupByDate;
    }


    public MoveOutDocResultGroupByDate getExcludeDocListGroupByDate() {
        return excludeDocListGroupByDate;
    }

    public DocSalesConverterResult(MoveOutDocResultGroupByDate groupByDate,
            MoveOutDocResultGroupByDate excludeDocListGroupByDate, List<MoveOut> unfoundMoveOuts) {
        this.cashDocGroupByDate = groupByDate;
        this.excludeDocListGroupByDate = excludeDocListGroupByDate;
        this.unfoundMoveOuts = unfoundMoveOuts;
    }

}
