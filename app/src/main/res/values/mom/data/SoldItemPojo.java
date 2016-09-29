package vp.mom.data;

/**
 * Created by shivkanya.i on 20-01-2016.
 */
public class SoldItemPojo {
    String soldItemName,soldItemDesc,soldItemPrice,soldItemImage;

    public String getSoldItemImage() {
        return soldItemImage;
    }

    public void setSoldItemImage(String soldItemImage) {
        this.soldItemImage = soldItemImage;
    }

    public SoldItemPojo() {
    }

    public String getSoldItemName() {

        return soldItemName;
    }

    public void setSoldItemName(String soldItemName) {
        this.soldItemName = soldItemName;
    }

    public String getSoldItemDesc() {
        return soldItemDesc;
    }

    public void setSoldItemDesc(String soldItemDesc) {
        this.soldItemDesc = soldItemDesc;
    }

    public String getSoldItemPrice() {
        return soldItemPrice;
    }

    public void setSoldItemPrice(String soldItemPrice) {
        this.soldItemPrice = soldItemPrice;
    }
}
