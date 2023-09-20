package com.colbertlum.entity;

public class MoveOut {

    private String productId;
    private String uom;
    private String productName;
    public Double totalAmount;
    private Double quantity;

    public Double getTotalAmount() {
        return totalAmount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

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

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return productId + "\n";
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

}
