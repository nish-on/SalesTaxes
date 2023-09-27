package com.github.nish_on.salestaxes;



public class SalesTaxes {


    public String[] getAmountAndRestOfItemDescription(String itemDescription) {

        String[] firstSpaceSplitted = itemDescription.split(" ", 2);
        return firstSpaceSplitted;

    }

}
