/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package vp.mom.gcm.gcm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import vp.mom.MainActivity;
import vp.mom.activitys.ProductDetails;


public class MyGcmPushReceiver extends GcmListenerService {

    private static final String TAG = vp.mom.gcm.gcm.MyGcmPushReceiver.class.getSimpleName();

    private NotificationUtils notificationUtils;
    int enentID;
    /**
     * Called when message is received.
     *
     * @param from   SenderID of the sender.
     * @param bundle Data bundle containing message data as key/value pairs.
     *               For Set of keys use data.keySet().
     */

    boolean chatFlag=false;
    @Override
    public void onMessageReceived(String from, Bundle bundle) {

        Log.e(TAG, "isBackground: " + bundle.toString());
        String title = bundle.getString("title");
        String flag = bundle.getString("push_flag");

        if (flag == null)
            return;

        switch (Integer.parseInt(flag)) {
//            case Config.PUSH_TYPE_CHATROOM:
//                // push notification belongs to a chat room
//                processChatRoomPush(title, isBackground, data);
//                break;
            case vp.mom.app.Config.PUSH_TYPE_USER:
                // push notification is specific to user
            //    processUserMessage(title, isBackground, data);
                chatFlag=true;
                String msg=bundle.getString("message");
                String userID=bundle.getString("chat_id");
                String msgTimeStamp=bundle.getString("push_time");
                processUserChatMessage(title,msg,userID,msgTimeStamp);

                break;

            case vp.mom.app.Config.PRODUCT_NOTIFICATIO:

                chatFlag=false;
            try
            {
                enentID =Integer.parseInt(bundle.getString("eventID"));
            }
            catch (Exception excptn)
            {
                enentID=100;
            }


                if(vp.mom.app.Config.PRODUCT_LIKE==enentID) {
                    productLikeNotification(title, bundle.getString("message"), bundle.getString("image").replace("th_", ""),
                            bundle.getString("product_id"), bundle.getString("owner_product_id"));
                }
                else if(vp.mom.app.Config.PRODUCT_COMMENT==enentID)
               {

                   productCoometNotificatio(title, bundle.getString("message"), bundle.getString("image").replace("th_", ""), bundle.getString("product_id"));
               }
                else if(vp.mom.app.Config.PRODUCT_SOLD==enentID)
                {
                   productSoldNotification(title, bundle.getString("message"), bundle.getString("image").replace("th_", ""));
                }

                else if(vp.mom.app.Config.PRODUCT_FOLLOW==enentID)
                {
                    followNotification(title, bundle.getString("message"), bundle.getString("image").replace("th_", ""));
                }
            else
                {
                    processProductNotification(title, bundle.getString("message"), bundle.getString("image").replace("th_", ""));
                }



                break;
        }




    }

    private void followNotification(String title, String message, String image) {

        long when = System.currentTimeMillis();
        Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
        resultIntent.putExtra("NotificationCalled",true);
        // check for push notification image attachment
        if (TextUtils.isEmpty(image)) {
            showNotificationMessage(getApplicationContext(), title,message, ""+when, resultIntent,chatFlag);
        } else {
            // push notification contains image
            // show it with the image
            showNotificationMessageWithBigImage(getApplicationContext(), title, message, ""+when, resultIntent, image);
        }
    }

    private void productSoldNotification(String title, String message, String image) {

        long when = System.currentTimeMillis();
        Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
        resultIntent.putExtra("soldItemCalled",true);
        // check for push notification image attachment
        if (TextUtils.isEmpty(image)) {
            showNotificationMessage(getApplicationContext(), title,message, ""+when, resultIntent,chatFlag);
        } else {
            // push notification contains image
            // show it with the image
            showNotificationMessageWithBigImage(getApplicationContext(), title, message, ""+when, resultIntent, image);
        }
    }

    private void productCoometNotificatio(String title, String message, String image, String product_id) {
        long when = System.currentTimeMillis();
        Intent commentIntent = new Intent(getApplicationContext(), vp.mom.activitys.CommentsActivity.class);

        commentIntent.putExtra("ProducID", product_id);

        if (TextUtils.isEmpty(image)) {
            showNotificationMessage(getApplicationContext(), title,message, ""+when, commentIntent,chatFlag);
        } else {
            // push notification contains image
            // show it with the image
            showNotificationMessageWithBigImage(getApplicationContext(), title, message, ""+when, commentIntent, image);
        }


    }

    private void productLikeNotification(String title, String message, String image, String productID, String userID) {

        long when = System.currentTimeMillis();
        Intent seelingintent = new Intent(getApplicationContext(), ProductDetails.class);
        seelingintent.putExtra("productId",productID);
        seelingintent.putExtra("calledUserId",userID);
        if (TextUtils.isEmpty(image)) {
            showNotificationMessage(getApplicationContext(), title,message, ""+when, seelingintent,chatFlag);
        } else {
            // push notification contains image
            // show it with the image
            showNotificationMessageWithBigImage(getApplicationContext(), title, message, ""+when, seelingintent, image);
        }


    }


    private void processUserChatMessage(String title, String msg, String userID,String msgTimeStamp) {

        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {

            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(vp.mom.app.Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("type", vp.mom.app.Config.PUSH_TYPE_CHATROOM);
            pushNotification.putExtra("message",msg);
            pushNotification.putExtra("userName",title);
            pushNotification.putExtra("UserToChat",userID);
            pushNotification.putExtra("msgTimeStamp",msgTimeStamp);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
//            NotificationUtils notificationUtils = new NotificationUtils();
//            notificationUtils.playNotificationSound();
        }
        else {
            vp.mom.app.Config.appendNotificationMessages=true;
            long when = System.currentTimeMillis();
            // app is in background. show the message in notification try
            Intent resultIntent = new Intent(getApplicationContext(), vp.mom.gcm.gcm.ChatRoomActivity.class);
            resultIntent.putExtra("UserToChat", userID);
            resultIntent.putExtra("name", title);
            showNotificationMessage(getApplicationContext(), title, msg,
                    "" + when, resultIntent,chatFlag);
        }
    }

    private void processProductNotification(String title, String message, String image) {

        long when = System.currentTimeMillis();
        Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
        // check for push notification image attachment
        if (TextUtils.isEmpty(image)) {
            showNotificationMessage(getApplicationContext(), title,message, ""+when, resultIntent,chatFlag);
        } else {
            // push notification contains image
            // show it with the image
            showNotificationMessageWithBigImage(getApplicationContext(), title, message, "" + when, resultIntent, image);
        }

    }


    private void processUserMessage(String title, boolean isBackground, String data,String imgUrl) {
      //  if (!isBackground) {

            String   imageUrl= imgUrl;

        long when = System.currentTimeMillis();
        Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);

        showNotificationMessageWithBigImage(getApplicationContext(), title, data, ""+when, resultIntent, imageUrl);
    }
    /**
     * Showing notification with text only
     * */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp,
                                         Intent intent,boolean chatFlag) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

      //  if(chatFlag)
     //   notificationUtils.showNotificationMessage(title, message, timeStamp, intent,chatFlag);
 //   else
            notificationUtils.showNotificationMessage(title, message, timeStamp, intent);

    }


    /**
     * Showing notification with text and image
     * */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}
