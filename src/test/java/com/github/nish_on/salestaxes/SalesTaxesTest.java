package com.github.nish_on.salestaxes;

import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;
public class SalesTaxesTest {

    SalesTaxes salesTaxes = new SalesTaxes();

    @Test
    public void testFirstSplitSuccess(){
        assertThat(salesTaxes.getAmountAndRestOfItemDescription("1 book at 12.49")).isEqualTo(new String []{"1", "book at 12.49"});
    }

    @Test
    public void testFirstSplitFails(){
        assertThat(salesTaxes.getAmountAndRestOfItemDescription("1 book at 12.49")).isNotEqualTo(new String []{"1", "book", "at", "12.49"});
    }

    @Test
    public void testSplitAtKeywordAt(){
        assertThat(salesTaxes.getItemDescriptionAndPrice("book at 12.49")).isEqualTo(new String[]{"book", "12.49"});
    }

    @Test
    public void testSplitItemDescriptionIntoThreeParts() {
        assertThat(salesTaxes.getSplittedItemDescription("1 book at 12.49")).isEqualTo(new String[]{"1", "book", "12.49"});
    }

    @Test
    public void testSplitItemDescriptionIntoFourPartsNonImported() {
        assertThat(salesTaxes.getSplittedItemDescriptionWithImportStatus("1 box of chocolates at 11.25"))
                .usingRecursiveComparison()
                .ignoringFields("salesTaxRate", "salesTax")
                .isEqualTo(new ReceiptPosition(1, "box of chocolates", BigDecimal.valueOf(11.25F), false));
    }

    @Test
    public void testSplitItemDescriptionIntoFourPartsImported() {
        assertThat(salesTaxes.getSplittedItemDescriptionWithImportStatus("1 box of imported chocolates at 11.25"))
                .usingRecursiveComparison()
                .ignoringFields("salesTaxRate", "salesTax")
                .isEqualTo(new ReceiptPosition(1, "box of chocolates", BigDecimal.valueOf(11.25F), true));
    }

    @Test
    public void testSplitItemDescriptionIntoFourPartsNonImported_2() {
        assertThat(salesTaxes.getSplittedItemDescriptionWithImportStatus("1 bottle of perfume at 47.50"))
                .usingRecursiveComparison()
                .ignoringFields("salesTaxRate", "salesTax")
                .isEqualTo(new ReceiptPosition(1, "bottle of perfume", BigDecimal.valueOf(47.50F), false));
    }

    @Test
    public void testSplitItemDescriptionIntoFourPartsImported_2() {
        assertThat(salesTaxes.getSplittedItemDescriptionWithImportStatus("1 imported bottle of perfume at 47.50"))
                .usingRecursiveComparison()
                .ignoringFields("salesTax")
                .isEqualTo(new ReceiptPosition(1, "bottle of perfume", BigDecimal.valueOf(47.50F), true));
    }

    @Test
    public void testCalculateSalesBasicSalesTaxRateForExcemptedProduct(){

        ReceiptPosition receiptPosition = new ReceiptPosition(1, "book", BigDecimal.valueOf(12.49F), false);

        assertThat(salesTaxes.getBasicSalesTaxRate(receiptPosition))
                .usingRecursiveComparison()
                .ignoringFields("salesTax")
                .isEqualTo(new ReceiptPosition(1, "book", BigDecimal.valueOf(12.49F), false, BigDecimal.valueOf(0.0d)));
    }

    @Test
    public void testCalculateSalesBasicSalesTaxRateForNonExcemptedProduct(){

        ReceiptPosition receiptPosition = new ReceiptPosition(1, "music CD", BigDecimal.valueOf(14.99F), false);

        assertThat(salesTaxes.getBasicSalesTaxRate(receiptPosition))
                .usingRecursiveComparison()
                .ignoringFields("salesTax")
                .isEqualTo(new ReceiptPosition(1, "music CD", BigDecimal.valueOf(14.99F), false, BigDecimal.valueOf(10.0d)));
    }

    @Test
    public void testAddImportTaxForExcemptedProduct() {
        ReceiptPosition receiptPosition = new ReceiptPosition(1, "book", BigDecimal.valueOf(12.49F), true, BigDecimal.valueOf(0.0d));

        assertThat(salesTaxes.addImportTax(receiptPosition))
                .usingRecursiveComparison()
                .ignoringFields("salesTax")
                .isEqualTo(new ReceiptPosition(1, "book", BigDecimal.valueOf(12.49F), true, BigDecimal.valueOf(5.0d)));
    }

    @Test
    public void testAddImportTaxForNotExcemptedProduct() {
        ReceiptPosition receiptPosition = new ReceiptPosition(1, "music CD", BigDecimal.valueOf(14.99F), true, BigDecimal.valueOf(10.0d));

        assertThat(salesTaxes.addImportTax(receiptPosition))
                .usingRecursiveComparison()
                .ignoringFields("salesTax")
                .isEqualTo(new ReceiptPosition(1, "music CD", BigDecimal.valueOf(14.99F), true, BigDecimal.valueOf(15.0d)));
    }

    @Test
    public void testAddImportTaxForExcemptedProductForNonImportedProducts() {
        ReceiptPosition receiptPosition = new ReceiptPosition(1, "book", BigDecimal.valueOf(12.49F), false, BigDecimal.valueOf(0.0d));

        assertThat(salesTaxes.addImportTax(receiptPosition))
                .usingRecursiveComparison()
                .ignoringFields("salesTax")
                .isEqualTo(new ReceiptPosition(1, "book", BigDecimal.valueOf(12.49F), false, BigDecimal.valueOf(0.0d)));
    }

    @Test
    public void testAddImportTaxForNotExcemptedProductForNonImportedProducts() {
        ReceiptPosition receiptPosition = new ReceiptPosition(1, "music CD", BigDecimal.valueOf(14.99F), false, BigDecimal.valueOf(10.0d));

        assertThat(salesTaxes.addImportTax(receiptPosition))
                .usingRecursiveComparison()
                .ignoringFields("salesTax")
                .isEqualTo(new ReceiptPosition(1, "music CD", BigDecimal.valueOf(14.99F), false, BigDecimal.valueOf(10.0d)));
    }

    @Test
    public void testCalculationOfSalesTax(){
        ReceiptPosition receiptPosition = new ReceiptPosition(1, "music CD", BigDecimal.valueOf(14.99F), false, BigDecimal.valueOf(10.0));

        assertThat(salesTaxes.calculateSalesTax(receiptPosition))
                .usingRecursiveComparison()
                .ignoringFields("salesTax")
                .isEqualTo(new ReceiptPosition(1, "music CD", BigDecimal.valueOf(14.99F), false, BigDecimal.valueOf(10.0d), BigDecimal.valueOf(1.5F)));
    }

    @Test
    public void testCalculationOfSalesTax_2(){
        ReceiptPosition receiptPosition = new ReceiptPosition(1, "book", BigDecimal.valueOf(12.49F), true, BigDecimal.valueOf(5.0d));

        assertThat(salesTaxes.calculateSalesTax(receiptPosition))
                .usingRecursiveComparison()
                .ignoringFields("salesTax")
                .isEqualTo(new ReceiptPosition(1, "book", BigDecimal.valueOf(12.49F), true, BigDecimal.valueOf(5.0d), BigDecimal.valueOf(0.65F)));
    }

    @Test
    public void testCalculationOfSalesTax_3(){
        ReceiptPosition receiptPosition = new ReceiptPosition(1, "imported music CD", BigDecimal.valueOf(14.99F), true, BigDecimal.valueOf(15.0d));

        assertThat(salesTaxes.calculateSalesTax(receiptPosition))
                .usingRecursiveComparison()
                .ignoringFields("salesTax")
                .isEqualTo(new ReceiptPosition(1, "imported music CD", BigDecimal.valueOf(14.99F), true, BigDecimal.valueOf(15.0d), BigDecimal.valueOf(2.25F)));
    }

    @Test
    public void testCalculationOfSalesTax_4(){
        ReceiptPosition receiptPosition = new ReceiptPosition(1, "imported box of chocolates", BigDecimal.valueOf(10.0F), true, BigDecimal.valueOf(5.0d));

        assertThat(salesTaxes.calculateSalesTax(receiptPosition))
                .usingRecursiveComparison()
                .ignoringFields("salesTax")
                .isEqualTo(new ReceiptPosition(1, "imported box of chocolates", BigDecimal.valueOf(10.0F), true, BigDecimal.valueOf(5.0d), BigDecimal.valueOf(0.5F)));
    }

    @Test
    public void testCalculationOfSalesTax_5(){
        ReceiptPosition receiptPosition = new ReceiptPosition(1, "imported music CD", BigDecimal.valueOf(10.0F), true, BigDecimal.valueOf(5.0d));

        assertThat(salesTaxes.calculateSalesTax(receiptPosition))
                .usingRecursiveComparison()
                .ignoringFields("salesTax")
                .isEqualTo(new ReceiptPosition(1, "imported music CD", BigDecimal.valueOf(10.0F), true, BigDecimal.valueOf(5.0d), BigDecimal.valueOf(0.5F)));
    }

    @Test
    public void testCalculationOfSalesTax_6(){
        ReceiptPosition receiptPosition = new ReceiptPosition(1, "box of imported chocolates", BigDecimal.valueOf(11.25F), true, BigDecimal.valueOf(5.0d));

        assertThat(salesTaxes.calculateSalesTax(receiptPosition))
                .usingRecursiveComparison()
                .ignoringFields("salesTax")
                .isEqualTo(new ReceiptPosition(1, "box of imported chocolates", BigDecimal.valueOf(11.25F), true, BigDecimal.valueOf(5.0d), BigDecimal.valueOf(0.5F)));
    }

    @Test
    public void testOutput1(){
        String input = "1 book at 12.49\n1 music CD at 14.99\n1 chocolate bar at 0.85";
        String expectedOutput = "1 book: 12.49\n1 music CD: 16.49\n1 chocolate bar: 0.85\nSales Taxes: 1.50\nTotal: 29.83";
        assertThat(salesTaxes.getReceiptContent(input))
                .isEqualTo(expectedOutput);
    }

    @Test
    public void testOutput2(){
        String input = "1 imported box of chocolates at 10.00\n1 imported bottle of perfume at 47.50";
        String expectedOutput = "1 imported box of chocolates: 10.50\n1 imported bottle of perfume: 54.65\nSales Taxes: 7.65\nTotal: 65.15";
        assertThat(salesTaxes.getReceiptContent(input))
                .isEqualTo(expectedOutput);
    }

    @Test
    public void testOutput3(){
        String input = "1 imported bottle of perfume at 27.99\n1 bottle of perfume at 18.99\n1 packet of headache pills at 9.75\n1 box of imported chocolates at 11.25";
        String expectedOutput = "1 imported bottle of perfume: 32.19\n1 bottle of perfume: 20.89\n1 packet of headache pills: 9.75\n1 imported box of chocolates: 11.85\nSales Taxes: 6.70\nTotal: 74.68";
        assertThat(salesTaxes.getReceiptContent(input))
                .isEqualTo(expectedOutput);
    }

}