package vp.mom.data;

/**
 * Created by shivkanya.i on 23-01-2016.
 */
public class NotificationSettingPojo {

    String Notification;
boolean settingStatus;
    String notificatiId;

    public String getNotificatiId() {
        return notificatiId;
    }

    public void setNotificatiId(String notificatiId) {
        this.notificatiId = notificatiId;
    }

    public boolean isSettingStatus() {
        return settingStatus;
    }

    public void setSettingStatus(boolean settingStatus) {
        this.settingStatus = settingStatus;
    }

    public NotificationSettingPojo() {
        //Notification = notification;
    }

    public String getNotification() {
        return Notification;
    }

    public void setNotification(String notification) {
        Notification = notification;
    }
}
