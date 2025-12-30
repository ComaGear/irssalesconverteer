package com.colbertlum.contentWriter;

import com.colbertlum.contentHandler.dataColumn.BiztoryOutputMoveOutColumn;
import com.colbertlum.entity.BiztoryOutputMoveOut;
import java.util.List;

public class BiztoryOutputMoveOutMapper implements ContentHeaderMapperInterface<BiztoryOutputMoveOut> {
    @Override
    public String onCell(String header, BiztoryOutputMoveOut item) {
        switch (header) {
            case BiztoryOutputMoveOutColumn.PRODUCT_ID:
                return item.getId();
            case BiztoryOutputMoveOutColumn.NAME:
                return item.getName();
            case BiztoryOutputMoveOutColumn.QUANTITY:
                return Double.toString(item.getQuantity());
            case BiztoryOutputMoveOutColumn.UOM:
                return item.getUom();
            case BiztoryOutputMoveOutColumn.PRICE:
                return Double.toString(item.getPrice());
            default:
                return "";
        }
    }

    @Override
    public List<String> onHeader() {
        return List.of(
            BiztoryOutputMoveOutColumn.PRODUCT_ID,
            BiztoryOutputMoveOutColumn.NAME,
            BiztoryOutputMoveOutColumn.QUANTITY,
            BiztoryOutputMoveOutColumn.UOM,
            BiztoryOutputMoveOutColumn.PRICE
        );
    }
}