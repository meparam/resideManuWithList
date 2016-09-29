package vp.mom.data;

/**
 * Created by shivkanya.i on 20-01-2016.
 */
public class SalePurchaseItemPojo {
    String ItemName,ItemDesc,ItemPrice,ItemImage,product_id,order_id,delivery_status,purchaseDate,order_detailed_Id;

    public String getOrder_detailed_Id() {
        return order_detailed_Id;
    }

    public void setOrder_detailed_Id(String order_detailed_Id) {
        this.order_detailed_Id = order_detailed_Id;
    }

    public String getDelivery_status() {
        return delivery_status;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public void setDelivery_status(String delivery_status) {
        this.delivery_status = delivery_status;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public float getProductRating() {
        return productRating;
    }

    public void setProductRating(float productRating) {
        this.productRating = productRating;
    }

    float productRating;

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemDesc() {
        return ItemDesc;
    }

    public void setItemDesc(String itemDesc) {
        ItemDesc = itemDesc;
    }

    public String getItemPrice() {
        return ItemPrice;
    }

    public void setItemPrice(String itemPrice) {
        ItemPrice = itemPrice;
    }

    public String getItemImage() {
        return ItemImage;
    }

    public void setItemImage(String itemImage) {
        ItemImage = itemImage;
    }

    public SalePurchaseItemPojo() {
    }


}
