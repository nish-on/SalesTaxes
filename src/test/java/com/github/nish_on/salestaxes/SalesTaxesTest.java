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
                .ignoringFields("salesTaxRate", "salesTax")
                .isEqualTo(new ReceiptPosition(1, "imported bottle of perfume", BigDecimal.valueOf(47.50F), true));
    }




}