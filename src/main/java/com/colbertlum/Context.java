package com.colbertlum;

import java.util.List;

import com.colbertlum.entity.UOM;
import com.colbertlum.entity.UnsableItem;

public class Context {
    private List<UOM> uom;

    private List<UnsableItem> unsableItems;

    public List<UnsableItem> getUnsableItems() {
        return unsableItems;
    }

    public void setUnsableItems(List<UnsableItem> unsableItems) {
        this.unsableItems = unsableItems;
    }

    public List<UOM> getUom() {
        return uom;
    }

    public void setUom(List<UOM> uom) {
        this.uom = uom;
    }


    
}
