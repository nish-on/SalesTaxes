package com.github.nish_on.salestaxes;


import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

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
        MathContext mathContext = new MathContext(2);
        ReceiptPosition receiptPosition = new ReceiptPosition(Integer.parseInt(splittedDescriptionWithImportStatus[0]), splittedDescriptionWithImportStatus[1], BigDecimal.valueOf(Double.parseDouble(splittedDescriptionWithImportStatus[2])), Boolean.parseBoolean(splittedDescriptionWithImportStatus[3]));
        return receiptPosition;
    }

    public ReceiptPosition getBasicSalesTaxRate(ReceiptPosition receiptPosition) {
        String[] excemptedProductsKeywords = new String[]{"book", "bar", "pill", "chocolate"};
        String[] itemWithVat = null;

        receiptPosition.setSalesTaxRate(BigDecimal.valueOf(10.0d));

        for(String keyword : excemptedProductsKeywords) {
            if(receiptPosition.getItemDescription().contains(keyword)){
                receiptPosition.setSalesTaxRate(BigDecimal.valueOf(0.0d));
                break;
            }
        }
        return receiptPosition;
    }

    public ReceiptPosition addImportTax(ReceiptPosition receiptPosition) {

        if(receiptPosition.isImported()) {
            receiptPosition.setSalesTaxRate(receiptPosition.getSalesTaxRate().add(BigDecimal.valueOf(5.0d)));
        }

        return receiptPosition;
    }

    public ReceiptPosition calculateSalesTax(ReceiptPosition receiptPosition) {

        BigDecimal salesTaxRate = receiptPosition.getSalesTaxRate();

        MathContext mathContext = new MathContext(2, RoundingMode.HALF_UP);
       //  MathContext mathContext2 = new MathContext(10, RoundingMode.UP);

        BigDecimal salesTax = BigDecimal.valueOf((double)(Math.round (Math.ceil(receiptPosition.getItemValue().doubleValue() * receiptPosition.getSalesTaxRate().doubleValue() * 20.0d  / 100.0d) ) ) / 20.0d) ;
        salesTax = salesTax.setScale(2, RoundingMode.UP);

       //  BigDecimal salesTax = salesTaxRate.multiply(receiptPosition.getItemValue()).divide(BigDecimal.valueOf(100.0F), mathContext2).multiply(BigDecimal.valueOf(20.0d)).round(mathContext).divide(BigDecimal.valueOf(20.0d), mathContext);

        receiptPosition.setSalesTax(salesTax);

        return receiptPosition;
    }

    public String getReceiptContent(String cart) {
        StringBuilder sb = new StringBuilder();
        BigDecimal salesTaxes = BigDecimal.valueOf(0.0F);
        BigDecimal total = BigDecimal.valueOf(0.0F);
        String[] cartArray = cart.split("\\n");
        MathContext mathContext = new MathContext(2);
        DecimalFormatSymbols dfs
                = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');

        DecimalFormat df = new DecimalFormat("#0.00", dfs);
        //System.out.println(df.format(50)); // 050.0


        for(String itemDescription : cartArray) {
            ReceiptPosition receiptPosition = getSplittedItemDescriptionWithImportStatus(itemDescription);
            receiptPosition = calculateSalesTax(addImportTax(getBasicSalesTaxRate( receiptPosition)));
            sb.append(receiptPosition.getAmount())
                    .append(" ")
                    .append(receiptPosition.getItemDescription())
                    .append(": ")
                    .append(df.format(receiptPosition.getItemValue().add(receiptPosition.getSalesTax()).doubleValue()))
                    .append("\n");
            salesTaxes = salesTaxes.add(receiptPosition.getSalesTax());
            total = total.add(receiptPosition.getItemValue().add(receiptPosition.getSalesTax()));
        }

        sb.append("Sales Taxes: ").append(df.format(salesTaxes)).append("\n")
                .append("Total: ").append(df.format(total));

        return sb.toString();
    }
}
