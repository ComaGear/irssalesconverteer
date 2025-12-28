package com.colbertlum.contentHandler.dataValidation;

import java.util.List;

import com.colbertlum.contentHandler.dataColumn.IrsSalesReportWithDocIdColumn;

public class IrsSalesReportDataValidator implements ContentHandlerValidationInterface<String> {

    @Override
    public boolean supports(ExcelSchema schema) {
        return schema == ExcelSchema.SALES_REPORT_WITH_ORDER_ID;
    }

    @Override
    public List<String> shouldHaveColumns() {
        return List.of(
            IrsSalesReportWithDocIdColumn.DATE,
            IrsSalesReportWithDocIdColumn.DOC_NO,
            IrsSalesReportWithDocIdColumn.ITEM_NO,
            IrsSalesReportWithDocIdColumn.NAME,
            IrsSalesReportWithDocIdColumn.QUANTITY,
            IrsSalesReportWithDocIdColumn.UOM,
            IrsSalesReportWithDocIdColumn.TOTAL_AMOUNT);
    }

    @Override
    public ExpectedDataMap<String> shouldContainedExpectedDataForTheseColumns() {
        return new ExpectedDataMap<>();
    }
    
}
