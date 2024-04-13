package com.colbertlum.entity;

public class UnsableItem {
    
    private String unsableId;
    private String toUseId;
    private double measurement;
    private double bundlePrice;
    private double unitPrice;
    private boolean reduceOriginUnitQuantity;

    public boolean isReduceOriginUnitQuantity() {
        return reduceOriginUnitQuantity;
    }
    public void setReduceOriginUnitQuantity(boolean reduceOriginUnitQuantity) {
        this.reduceOriginUnitQuantity = reduceOriginUnitQuantity;
    }
    public double getBundlePrice() {
        return bundlePrice;
    }
    public void setBundlePrice(double bundlePrice) {
        this.bundlePrice = bundlePrice;
    }
    
    public double getUnitPrice() {
        return unitPrice;
    }
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
    public String getUnsableId() {
        return unsableId;
    }
    public void setUnsableId(String unsableId) {
        this.unsableId = unsableId;
    }
    public String getToUseId() {
        return toUseId;
    }
    public void setToUseId(String toUseId) {
        this.toUseId = toUseId;
    }
    public double getMeasurement() {
        return measurement;
    }
    public void setMeasurement(double measurement) {
        this.measurement = measurement;
    }


}
