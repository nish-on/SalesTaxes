package com.github.nish_on.salestaxes;


import java.math.BigDecimal;
import java.math.MathContext;

public class SalesTaxes {


    public String[] getAmountAndRestOfItemDescription(String itemDescription) {

        String[] firstSpaceSplitted = itemDescription.split(" ", 2);
        return firstSpaceSplitted;

    }

    public String[] getItemDescriptionAndPrice(String itemDescription) {
        String[] splittedItemDescription = itemDescription.split(" at ");
        if(splittedItemDescription.length == 2) {
            return splittedItemDescription;
        } else {

            String description = "";
            for (int i = 0; i < splittedItemDescription.length -2 ;i++) {
                description += splittedItemDescription[i] + " at ";
            }
            return new String[]{description, splittedItemDescription[splittedItemDescription.length - 1]};
        }
    }

    public String[] getSplittedItemDescription(String itemDescription){
        String[] firsSplittedArray = getAmountAndRestOfItemDescription(itemDescription);
        String[] secondSplitted = getItemDescriptionAndPrice(firsSplittedArray[1]);
        return new String[]{firsSplittedArray[0], secondSplitted[0], secondSplitted[1]};
    }

    public ReceiptPosition getSplittedItemDescriptionWithImportStatus(String itemDescription){
        String[] splittedDescription = getSplittedItemDescription( itemDescription);
        String[] splittedDescriptionWithImportStatus ;
        if(splittedDescription[1].contains("imported")) {
            splittedDescriptionWithImportStatus = new String[] {splittedDescription[0], splittedDescription[1], splittedDescription[2], "true"};
        } else {
            splittedDescriptionWithImportStatus = new String[] {splittedDescription[0], splittedDescription[1], splittedDescription[2], "false"};
        }
        MathContext mathContext = new MathContext(0);
        ReceiptPosition receiptPosition = new ReceiptPosition(Integer.parseInt(splittedDescriptionWithImportStatus[0]), splittedDescriptionWithImportStatus[1], BigDecimal.valueOf(Float.parseFloat(splittedDescriptionWithImportStatus[2])).round(mathContext), Boolean.parseBoolean(splittedDescriptionWithImportStatus[3]));
        return receiptPosition;
    }

    public ReceiptPosition getBasicSalesTaxRate(ReceiptPosition receiptPosition) {
        String[] excemptedProductsKeywords = new String[]{"book", "bar", "pill"};
        String[] itemWithVat = null;

        receiptPosition.setSalesTaxRate(10.0F);

        for(String keyword : excemptedProductsKeywords) {
            if(receiptPosition.getItemDescription().contains(keyword)){
                receiptPosition.setSalesTaxRate(0.0F);
                break;
            }
        }
        return receiptPosition;
    }

    public ReceiptPosition addImportTax(ReceiptPosition receiptPosition) {

        if(receiptPosition.isImported()) {
            receiptPosition.setSalesTaxRate(receiptPosition.getSalesTaxRate() + 5.0F);
        }

        return receiptPosition;
    }

    public ReceiptPosition calculateSalesTax(ReceiptPosition receiptPosition) {

        BigDecimal salesTaxRate = BigDecimal.valueOf(receiptPosition.getSalesTaxRate());

        MathContext mathContext = new MathContext(0);

        BigDecimal salesTax = salesTaxRate.multiply(receiptPosition.getItemValue()).multiply(BigDecimal.valueOf(100.0F)).round(mathContext);

        receiptPosition.setSalesTax(salesTax);

        return receiptPosition;
    }

    public String getReceiptContent(String cart) {
        StringBuilder sb = new StringBuilder();
        BigDecimal salesTaxes = BigDecimal.valueOf(0.0F);
        BigDecimal total = BigDecimal.valueOf(0.0F);
        String[] cartArray = cart.split("\\n");

        for(String itemDescription : cartArray) {
            ReceiptPosition receiptPosition = getSplittedItemDescriptionWithImportStatus(itemDescription);
            receiptPosition = calculateSalesTax(addImportTax(getBasicSalesTaxRate( receiptPosition)));
            sb.append(receiptPosition.getAmount())
                    .append(" ")
                    .append(receiptPosition.getItemDescription())
                    .append(": ")
                    .append(receiptPosition.getItemValue().add(receiptPosition.getSalesTax()))
                    .append("\n");
            salesTaxes = salesTaxes.add(receiptPosition.getSalesTax());
            total = total.add(receiptPosition.getItemValue().add(receiptPosition.getSalesTax()));
        }

        sb.append("Sales Taxes: ").append(salesTaxes).append("\n")
                .append("Total: ").append(total);

        return sb.toString();
    }
}
