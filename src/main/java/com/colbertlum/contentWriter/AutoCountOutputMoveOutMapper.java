package com.colbertlum.contentWriter;

import java.util.List;

import com.colbertlum.contentHandler.dataColumn.AutoCountOutputMoveOutColumn;
import com.colbertlum.entity.AutoCountOutputMoveOut;

public class AutoCountOutputMoveOutMapper implements ContentHeaderMapperInterface<AutoCountOutputMoveOut> {

    @Override
    public String onCell(String header, AutoCountOutputMoveOut item) {
        switch (header) {
            case AutoCountOutputMoveOutColumn.PRODUCT_ID:
                return item.getId();
            case AutoCountOutputMoveOutColumn.NAME:
                return item.getName();
            case AutoCountOutputMoveOutColumn.QUANTITY:
                return Double.toString(item.getQuantity());
            case AutoCountOutputMoveOutColumn.UOM:
                return item.getUom();
            case AutoCountOutputMoveOutColumn.PRICE:
                return Double.toString(item.getPrice());
            default:
                return "";
        }
    }

    @Override
    public List<String> onHeader() {
        return List.of(
            AutoCountOutputMoveOutColumn.PRODUCT_ID,
            AutoCountOutputMoveOutColumn.NAME,
            AutoCountOutputMoveOutColumn.QUANTITY,
            AutoCountOutputMoveOutColumn.UOM,
            AutoCountOutputMoveOutColumn.PRICE
        );
    }
}
