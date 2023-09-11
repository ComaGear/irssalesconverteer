package com.colbertlum.entity;

public class MoveOut {

    private String productId;
    private String uom;
    private Float Quantity;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public Float getQuantity() {
        return Quantity;
    }

    public void setQuantity(Float quantity) {
        Quantity = quantity;
    }

    @Override
    public String toString() {
        return productId + "\n";
    }

    
}
