package com.github.nish_on.salestaxes;

import junit.framework.TestCase;
import org.junit.Test;

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




}