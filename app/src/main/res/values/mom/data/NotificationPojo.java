package vp.mom.data;

/**
 * Created by shivkanya.i on 23-01-2016.
 */
public class NotificationPojo {
    public NotificationPojo() {
    }

    String notificationMsg;
    String userName;
    String time;
String productID,producrImage;

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProducrImage() {
        return producrImage;
    }

    public void setProducrImage(String producrImage) {
        this.producrImage = producrImage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    String userImage;
    String userId;

    public String getNotificationMsg() {
        return notificationMsg;
    }

    public void setNotificationMsg(String notificationMsg) {
        this.notificationMsg = notificationMsg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
