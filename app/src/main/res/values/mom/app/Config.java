package vp.mom.app;

/**
 * Created by Lincoln on 05/01/16.
 */
public class Config {

    // flag to identify whether to show single line
    // or multi line test push notification tray
    public static boolean appendNotificationMessages = true;

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // type of push messages
    public static final int PUSH_TYPE_CHATROOM = 3;

    public static final int PUSH_TYPE_USER = 1;
    public static  final int PRODUCT_NOTIFICATIO=2;

    // id to handle the notification in the notification try
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final int PRODUCT_LIKE=1;
    public static final int PRODUCT_COMMENT=2;
    public static final int PRODUCT_SOLD=5;
    public static final int PRODUCT_FOLLOW=6;

}
