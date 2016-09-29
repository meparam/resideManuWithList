package vp.mom.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import vp.mom.R;
import vp.mom.app.AppController;
import vp.mom.data.NotificationSettingPojo;

/**
 * Created by pallavi.b on 23-Jan-16.
 */
public class NotificationSettingAadapter extends BaseAdapter {
    private Context context;
    private ArrayList<NotificationSettingPojo> mArraylistPojo = new ArrayList<NotificationSettingPojo>();
    private LayoutInflater mLayoutInflater;
vp.mom.api.SessionManager session;
    public NotificationSettingAadapter(ArrayList<NotificationSettingPojo> mArraylistPojo, Context context) {
        this.mArraylistPojo = mArraylistPojo;
        this.context = context;
        session=new vp.mom.api.SessionManager(context);
    }

    @Override
    public int getCount() {
        return mArraylistPojo.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mLayoutInflater == null) {
            mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null)
            convertView = mLayoutInflater.inflate(R.layout.simplerow, null);
        TextView txtNotificationName = (TextView) convertView.findViewById(R.id.rowTextView);
       final CheckBox settingbox= (CheckBox) convertView.findViewById(R.id.settingbox);
        final NotificationSettingPojo data = mArraylistPojo.get(position);
        txtNotificationName.setText(data.getNotification());
              settingbox.setChecked(data.isSettingStatus());

        settingbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBoxSetting(data.getNotificatiId());
                  //  Log.v("checkboxclick", "" + settingbox);
                } else {
                    checkBoxSetting(data.getNotificatiId());
                //    Log.v("checkboxclick", "" + settingbox);
                }
            }
        });

        return convertView;
    }
    private void checkBoxSetting(String ntId) {

        String tag_string_req = "get_check_box";



        StringRequest strReq = new StringRequest(Request.Method.POST,
                vp.mom.api.AppConfig.SET_UNSET_PUSH_SETTING + "?" + "Uid=" + session.getStringSessionData("uid")+"&"+"NTId=" + ntId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
             //   Log.e("get_check_box", "get_check_box " + response.toString());
                //   hideDialog();
                if (response != null) {

                    // commentData.clear();
                    JSONObject jsonresponse = null;
                    try {
                        jsonresponse = new JSONObject(response.toString());

                        //    parseJsonFeed(jsonresponse);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("get_check_box", "get_check_box " + error.getMessage());

                //  hideDialog();
            }
        }) {

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
