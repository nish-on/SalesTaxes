package com.github.nish_on.salestaxes;

import junit.framework.TestCase;
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
                .isEqualTo(new ReceiptPosition(1, "box of imported chocolates", BigDecimal.valueOf(11.25F), true));
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
                .isEqualTo(new ReceiptPosition(1, "imported bottle of perfume", BigDecimal.valueOf(47.50F), true));
    }

    @Test
    public void testCalculateSalesBasicSalesTaxRateForExcemptedProduct(){

        ReceiptPosition receiptPosition = new ReceiptPosition(1, "book", BigDecimal.valueOf(12.49F), false);

        assertThat(salesTaxes.getBasicSalesTaxRate(receiptPosition))
                .usingRecursiveComparison()
                .ignoringFields("salesTax")
                .isEqualTo(new ReceiptPosition(1, "book", BigDecimal.valueOf(12.49F), false, 0.0F));
    }

    @Test
    public void testCalculateSalesBasicSalesTaxRateForNonExcemptedProduct(){

        ReceiptPosition receiptPosition = new ReceiptPosition(1, "music CD", BigDecimal.valueOf(14.99F), false);

        assertThat(salesTaxes.getBasicSalesTaxRate(receiptPosition))
                .usingRecursiveComparison()
                .ignoringFields("salesTax")
                .isEqualTo(new ReceiptPosition(1, "music CD", BigDecimal.valueOf(14.99F), false, 10.0F));
    }

    @Test
    public void testAddImportTaxForExcemptedProduct() {
        ReceiptPosition receiptPosition = new ReceiptPosition(1, "book", BigDecimal.valueOf(12.49F), true, 0.0F);

        assertThat(salesTaxes.addImportTax(receiptPosition))
                .usingRecursiveComparison()
                .ignoringFields("salesTax")
                .isEqualTo(new ReceiptPosition(1, "book", BigDecimal.valueOf(12.49F), true, 5.0F));
    }

    @Test
    public void testAddImportTaxForNotExcemptedProduct() {
        ReceiptPosition receiptPosition = new ReceiptPosition(1, "music CD", BigDecimal.valueOf(14.99F), true, 10.0F);

        assertThat(salesTaxes.addImportTax(receiptPosition))
                .usingRecursiveComparison()
                .ignoringFields("salesTax")
                .isEqualTo(new ReceiptPosition(1, "music CD", BigDecimal.valueOf(14.99F), true, 15.0F));
    }

    @Test
    public void testAddImportTaxForExcemptedProductForNonImportedProducts() {
        ReceiptPosition receiptPosition = new ReceiptPosition(1, "book", BigDecimal.valueOf(12.49F), false, 0.0F);

        assertThat(salesTaxes.addImportTax(receiptPosition))
                .usingRecursiveComparison()
                .ignoringFields("salesTax")
                .isEqualTo(new ReceiptPosition(1, "book", BigDecimal.valueOf(12.49F), false, 0.0F));
    }

    @Test
    public void testAddImportTaxForNotExcemptedProductForNonImportedProducts() {
        ReceiptPosition receiptPosition = new ReceiptPosition(1, "music CD", BigDecimal.valueOf(14.99F), false, 10.0F);

        assertThat(salesTaxes.addImportTax(receiptPosition))
                .usingRecursiveComparison()
                .ignoringFields("salesTax")
                .isEqualTo(new ReceiptPosition(1, "music CD", BigDecimal.valueOf(14.99F), false, 10.0F));
    }

    @Test
    public void testCalculationOfSalesTax(){
        ReceiptPosition receiptPosition = new ReceiptPosition(1, "music CD", BigDecimal.valueOf(14.99F), false, 10.0F);

        assertThat(salesTaxes.calculateSalesTax(receiptPosition))
                .usingRecursiveComparison()
                .ignoringFields("salesTax")
                .isEqualTo(new ReceiptPosition(1, "music CD", BigDecimal.valueOf(14.99F), false, 10.0F, BigDecimal.valueOf(1.5F)));
    }

    @Test
    public void testCalculationOfSalesTax_2(){
        ReceiptPosition receiptPosition = new ReceiptPosition(1, "book", BigDecimal.valueOf(12.49F), true, 5.0F);

        assertThat(salesTaxes.calculateSalesTax(receiptPosition))
                .usingRecursiveComparison()
                .ignoringFields("salesTax")
                .isEqualTo(new ReceiptPosition(1, "book", BigDecimal.valueOf(12.49F), true, 5.0F, BigDecimal.valueOf(0.65F)));
    }

    @Test
    public void testCalculationOfSalesTax_3(){
        ReceiptPosition receiptPosition = new ReceiptPosition(1, "imported music CD", BigDecimal.valueOf(14.99F), true, 15.0F);

        assertThat(salesTaxes.calculateSalesTax(receiptPosition))
                .usingRecursiveComparison()
                .ignoringFields("salesTax")
                .isEqualTo(new ReceiptPosition(1, "imported music CD", BigDecimal.valueOf(14.99F), true, 15.0F, BigDecimal.valueOf(2.25F)));
    }

}