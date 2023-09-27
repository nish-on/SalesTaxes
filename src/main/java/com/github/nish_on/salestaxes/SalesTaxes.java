package com.github.nish_on.salestaxes;



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
}
