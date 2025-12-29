package com.colbertlum.entity;

public class DocSalesConverterResult {

    private MoveOutDocResultGroupByDate cashDocGroupByDate;
    private MoveOutDocResultGroupByDate excludeDocListGroupByDate;

    public MoveOutDocResultGroupByDate getCashDocGroupByDate() {
        return cashDocGroupByDate;
    }


    public MoveOutDocResultGroupByDate getExcludeDocListGroupByDate() {
        return excludeDocListGroupByDate;
    }

    public DocSalesConverterResult(MoveOutDocResultGroupByDate groupByDate,
            MoveOutDocResultGroupByDate excludeDocListGroupByDate) {
        this.cashDocGroupByDate = groupByDate;
        this.excludeDocListGroupByDate = excludeDocListGroupByDate;
    }

}
