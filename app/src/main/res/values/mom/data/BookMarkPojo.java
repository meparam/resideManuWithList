package vp.mom.data;

/**
 * Created by shivkanya.i on 11-01-2016.
 */
public class BookMarkPojo {
    public BookMarkPojo() {
    }

    String productId;
    String productUrl;
    String productTime;
    String productDisc;
    String bookMarkId;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    String userID;

    public String getBookMarkId() {
        return bookMarkId;
    }

    public void setBookMarkId(String bookMarkId) {
        this.bookMarkId = bookMarkId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getProductTime() {
        return productTime;
    }

    public void setProductTime(String productTime) {
        this.productTime = productTime;
    }

    public String getProductDisc() {
        return productDisc;
    }

    public void setProductDisc(String productDisc) {
        this.productDisc = productDisc;
    }
}
