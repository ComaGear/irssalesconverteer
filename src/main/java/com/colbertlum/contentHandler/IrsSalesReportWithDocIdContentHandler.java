package com.colbertlum.contentHandler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.model.SharedStrings;
import org.apache.poi.xssf.model.StylesTable;

import com.colbertlum.constants.DateTimePattern;
import com.colbertlum.contentHandler.dataColumn.IrsSalesReportWithDocIdColumn;
import com.colbertlum.contentHandler.dataValidation.IrsSalesReportDataValidator;
import com.colbertlum.entity.Doc;
import com.colbertlum.entity.MoveOut;

public class IrsSalesReportWithDocIdContentHandler extends ContentHandler {

    private List<Doc> docList;
    private List<MoveOut> moveOuts;
    private MoveOut moveOut;
    private Doc doc;

    @Override
    protected void onCell(String header, int row, String value) {
        switch (header) {
            case IrsSalesReportWithDocIdColumn.DATE:
                doc.setDate(DateTimePattern.getLocalDate(value));
                break;
            case IrsSalesReportWithDocIdColumn.DOC_NO:
                doc.setId(value);
                break;
            case IrsSalesReportWithDocIdColumn.ITEM_NO:
                moveOut.setProductId(value);
                break;
            case IrsSalesReportWithDocIdColumn.NAME:
                moveOut.setProductName(value);
                break;
            case IrsSalesReportWithDocIdColumn.QUANTITY:
                moveOut.setQuantity(Double.parseDouble(value));
                break;
            case IrsSalesReportWithDocIdColumn.UOM:
                moveOut.setUom(value);
                break;
            case IrsSalesReportWithDocIdColumn.TOTAL_AMOUNT:
                moveOut.setTotalAmount(Double.parseDouble(value));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onRow(int row) {
        if(doc.getId() != null && moveOut.getProductId() != null) {
            addDocToList(doc, moveOut);
        }
    }
    
    public IrsSalesReportWithDocIdContentHandler(SharedStrings sharedStrings, StylesTable stylesTable,
            List<Doc> docList, List<MoveOut> moveOuts) {
        super(sharedStrings, stylesTable, new IrsSalesReportDataValidator());
        
        this.docList = docList;
        this.moveOuts = moveOuts;

        this.moveOut = new MoveOut();
        this.doc = new Doc();
    }

    private void addDocToList(Doc doc, MoveOut moveOut){
        Doc lastDoc = null;
        if(docList.size() > 0) {
            lastDoc = docList.get(docList.size() - 1);
        }
        if(lastDoc == null || !lastDoc.getId().equals(doc.getId())) {

            if(doc.getMoveOuts() == null) {
                doc.setMoveOuts(new ArrayList<MoveOut>());
            }

            docList.add(doc);
            doc.getMoveOuts().add(moveOut);
        } else {
            lastDoc.getMoveOuts().add(moveOut);
        }

        moveOuts.add(moveOut);
    }
}
