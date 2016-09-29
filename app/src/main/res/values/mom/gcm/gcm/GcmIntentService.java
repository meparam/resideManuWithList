package vp.mom.gcm.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import vp.mom.R;
import vp.mom.api.AppConfig;
import vp.mom.app.AppController;

public class GcmIntentService extends IntentService {
    private static final String TAG = vp.mom.gcm.gcm.GcmIntentService.class.getSimpleName();
    public GcmIntentService() {
        super(TAG);
    }
    public static final String KEY = "key";
    public static final String TOPIC = "topic";
    public static final String SUBSCRIBE = "subscribe";
    public static final String UNSUBSCRIBE = "unsubscribe";
    vp.mom.api.SessionManager session;
String action="item";
    @Override
    protected void onHandleIntent(Intent intent) {

        if(intent!=null)
        action=intent.getStringExtra("key");

        session=new vp.mom.api.SessionManager(getApplicationContext());
        registerGCM();
    }
    /**
     * Registering with GCM and obtaining the gcm registration id
     */
    private void registerGCM() {
     //   SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
       try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

         //   Log.e(TAG, "GCM Registration Token: " + token);

            // sending the registration id to our server
            sendRegistrationToServer(token);
           // sharedPreferences.edit().putBoolean(Config.SENT_TOKEN_TO_SERVER, true).apply();
        } catch (Exception e) {
           // Log.e(TAG, "Failed to complete token refresh", e);

           // sharedPreferences.edit().putBoolean(Config.SENT_TOKEN_TO_SERVER, false).apply();
        }
//        // Notify UI that registration has completed, so the progress indicator can be hidden.
//        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token) {
        String tag_string_req = "sendRegistrationToServer";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.DEVICE_DETAILS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
             //   Log.d("send gcm ToServer", " Response: " + response.toString());
                //	hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("status");
                    // Check for error node in json
                    if (status) {
                        // user successfully logged in
                       // Log.e("send gcm ToServer", " Response: " + response.toString());

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("message");

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("send gcm ToServer", " Error: " + error.getMessage());

                //	hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",session.getStringSessionData("uid"));

                if(action.equalsIgnoreCase("logout"))
                    params.put("gcm_id","0");
                else
                    params.put("gcm_id",token);

                params.put("device_flag","1");
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


}
