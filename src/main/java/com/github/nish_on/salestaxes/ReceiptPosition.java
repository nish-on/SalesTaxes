package com.github.nish_on.salestaxes;

import java.math.BigDecimal;

public class ReceiptPosition {

    private int amount;
    private String itemDescription;
    private BigDecimal itemValue;

    private boolean imported;

    private float salesTaxRate;

    private BigDecimal salesTax;

    public ReceiptPosition(int amount, String itemDescription, BigDecimal itemValue, boolean imported) {
        this.amount = amount;
        this.itemDescription = itemDescription;
        this.itemValue = itemValue;
        this.imported = imported;
    }

    public ReceiptPosition(int amount, String itemDescription, BigDecimal itemValue, boolean imported, float salesTaxRate) {
        this.amount = amount;
        this.itemDescription = itemDescription;
        this.itemValue = itemValue;
        this.imported = imported;
        this.salesTaxRate = salesTaxRate;
    }

    public ReceiptPosition(int amount, String itemDescription, BigDecimal itemValue, boolean imported, float salesTaxRate, BigDecimal salesTax) {
        this.amount = amount;
        this.itemDescription = itemDescription;
        this.itemValue = itemValue;
        this.imported = imported;
        this.salesTaxRate = salesTaxRate;
        this.salesTax = salesTax;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public BigDecimal getItemValue() {
        return itemValue;
    }

    public void setItemValue(BigDecimal itemValue) {
        this.itemValue = itemValue;
    }

    public boolean isImported() {
        return imported;
    }

    public void setImported(boolean imported) {
        this.imported = imported;
    }

    public float getSalesTaxRate() {
        return salesTaxRate;
    }

    public void setSalesTaxRate(float salesTaxRate) {
        this.salesTaxRate = salesTaxRate;
    }

    public BigDecimal getSalesTax() {
        return salesTax;
    }

    public void setSalesTax(BigDecimal salesTax) {
        this.salesTax = salesTax;
    }
}
