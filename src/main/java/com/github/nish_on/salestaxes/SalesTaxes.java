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

        ReceiptPosition receiptPosition = new ReceiptPosition(Integer.parseInt(splittedDescriptionWithImportStatus[0]), splittedDescriptionWithImportStatus[1], BigDecimal.valueOf(Float.parseFloat(splittedDescriptionWithImportStatus[2])), Boolean.parseBoolean(splittedDescriptionWithImportStatus[3]));
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

        MathContext mathContext = new MathContext(2);

        BigDecimal salesTax = salesTaxRate.multiply(receiptPosition.getItemValue()).multiply(BigDecimal.valueOf(100.0F)).round(mathContext);

        receiptPosition.setSalesTax(salesTax);
        
        return receiptPosition;
    }
}
