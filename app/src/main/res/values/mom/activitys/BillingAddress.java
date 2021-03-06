package vp.mom.activitys;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vp.mom.R;
import vp.mom.app.AppController;
import vp.mom.gcm.gcm.NotificationUtils;

public class BillingAddress extends AppCompatActivity {
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String msg,chatRoomId,userName;
    private NotificationUtils notificationUtils;
    private String strname,strlastname,straddress,strcity,strstate,strzipcode,strcountry,strphonenumber;

    private EditText input_name, input_address, input_city,  input_zipcode,  input_phonenumber, input_lastname;
    private TextInputLayout input_layout_name, input_layout_lastname, input_layout_address, input_layout_City, input_layout_State, input_layout_zipcode, input_layout_country, input_layout_phonenumber;
    private Button btnSubmit,inputstate,inputcountry;
    String countryId,cityId;
    String[] all_country_array, all_country_id_array,all_county,all_county_id;
    TextView tooltitle;
    vp.mom.api.SessionManager session;

    boolean flag=false;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shipping_address);
        pDialog = new ProgressDialog(vp.mom.activitys.BillingAddress.this,
                R.style.AppTheme_Dark_Dialog);
        //   pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        session=new vp.mom.api.SessionManager(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null) setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tooltitle = (TextView) findViewById(R.id.tooltitle);
        tooltitle.setText("Billing address");
        init();

        getShippingAddress();

        input_name.addTextChangedListener(new MyTextWatcher(input_name));

        inputcountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder dialog_area = new AlertDialog.Builder(
                        vp.mom.activitys.BillingAddress.this);

                // Set the dialog title

                dialog_area.setTitle("Select country ! ")

                        .setSingleChoiceItems(all_country_array, 0,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int item) {
                                        inputcountry.setText(all_country_array[item]);
                                        countryId = all_country_id_array[item].toString().trim();
                                        cityId = null;
                                        inputstate.setHint("County");
                                        get_All_County(countryId);
                                        dialog.dismiss();

                                    }
                                });

                dialog_area.show();

            }
        });

        inputstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialog_area = new AlertDialog.Builder(
                        vp.mom.activitys.BillingAddress.this);

                // Set the dialog title

                dialog_area.setTitle("Select county ! ")

                        .setSingleChoiceItems(all_county, 0,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int item) {
                                        inputstate.setText(all_county[item]);
                                        cityId = all_county_id[item].toString().trim();


                                        dialog.dismiss();

                                    }
                                });

                dialog_area.show();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (submitForm()) {
                    strname = input_name.getText().toString().trim();
                    strlastname = input_lastname.getText().toString().trim();
                    straddress = input_address.getText().toString().trim();
                    strcity = input_city.getText().toString().trim();
                   /// strstate = input_state.getText().toString().trim();
                    strzipcode = input_zipcode.getText().toString().trim();
                  //  strcountry = input_country.getText().toString().trim();
                    strphonenumber = input_phonenumber.getText().toString().trim();
                    addShippingAddress();
                }
            }

            private boolean submitForm() {

                if (!validateName()) {
                    return false;
                } else if (!validateAddress()) {
                    return false;
                } else if (!validateAddress()) {
                    return false;
                } else if (!validateCity()) {
                    return false;
                } else if (!validateState()) {
                    return false;
                } else if (!validateZip()) {
                    return false;
                } else if (!validateCountry()) {
                    return false;
                } else if (!validatePhoneNumber()) {
                    return false;
                } else return true;
            }
        });
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(vp.mom.app.Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    handlePushNotification(intent);
                }
            }
        };
    }
    private void get_All_County(final String countryId) {

        String tag_string_req = "get_All_County";

        pDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                vp.mom.api.AppConfig.GET_ALL_CITY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("shipping activity", "cHARITY Response: " + response);
                hideDialog();

                ArrayList<String> stringArrayListID = new ArrayList<String>();
                ArrayList<String> stringArrayListNAME = new ArrayList<String>();
                JSONObject jObj;
                try {
                    jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("status");
                    if (status) {

                        JSONArray jsonArray = jObj.getJSONArray("items");
                        for (int i=0;i<jsonArray.length();i++)
                        {

                            JSONObject  jsonObject = jsonArray.getJSONObject(i);

                            stringArrayListID.add(jsonObject.getString("id"));
                            stringArrayListNAME.add(jsonObject.getString("name"));

                        }

                        all_county=stringArrayListNAME.toArray(new String[stringArrayListNAME.size()]);
                        all_county_id=stringArrayListID.toArray(new String[stringArrayListID.size()]);
                    } else {
                        String errorMsg = "Something went wrong";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("shipping activity", "getAllCharity Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("country_id",countryId);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private void handlePushNotification(Intent intent) {
        int type = intent.getIntExtra("type", -1);

        // if the push is of chat room message
        // simply update the UI unread messages count
        if (type == vp.mom.app.Config.PUSH_TYPE_CHATROOM) {
            msg = (String)intent.getSerializableExtra("message");

            chatRoomId  = intent.getStringExtra("UserToChat");

            userName=intent.getStringExtra("userName");

//            UserChatListPojo feeditem=new UserChatListPojo();
//
//            feeditem.setUserName(userName);
//            feeditem.setUserId(chatRoomId);
//            feeditem.setLastMessage(msg);
//            feeditem.setUserImage("http://marketofmums.com/images/users/");
//
//            if (msg != null && chatRoomId != null) {
//                updateRow(chatRoomId, feeditem);
//            }
        }
        long when = System.currentTimeMillis();
        // app is in background. show the message in notification try
        Intent resultIntent = new Intent(getApplicationContext(), vp.mom.gcm.gcm.ChatRoomActivity.class);
        resultIntent.putExtra("UserToChat", chatRoomId);
        resultIntent.putExtra("name", userName);
        showNotificationMessage(getApplicationContext(), userName, msg,
                ""+when, resultIntent);

    }
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }
    @Override
    protected void onResume() {
        super.onResume();

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(vp.mom.app.Config.PUSH_NOTIFICATION));

        // clearing the notification tray
        // NotificationUtils.clearNotifications();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private void addShippingAddress() {
        pDialog.setMessage("");
        showDialog();
        String tag_string_req = "AddShippingAddress";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                vp.mom.api.AppConfig.ADD_Billing_ADDRESS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
           //     Log.e("ADD_SHIPPING_ADDRESS", "ADD_SHIPPING_ADDRESS Response: " + response.toString());
                	hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("status");
                    // Check for error node in json
                    if (status) {
                        showAlertMessage("Billing address updated successfully",true);

                        // user successfully logged in
                    //    Log.e("ADD_SHIPPING_ADDRESS", "ADD_SHIPPING_ADDRESS Response: " + response.toString());

                    } else {

                     //   Log.e("ADD_SHIPPING_ADDRESS", "ADD_SHIPPING_ADDRESS Response: " + response.toString());
                        // Error in login. Get the error message
                     //   String errorMsg = jObj.getString("message");

                        showAlertMessage("Something went wrong",false);
//                        Toast.makeText(BillingAddress.this,
//                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(vp.mom.activitys.BillingAddress.this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
             //   Log.e("ADD_SHIPPING_ADDRESS", "ADD_SHIPPING_ADDRESS Error: " + error.getMessage());
                Toast.makeText(vp.mom.activitys.BillingAddress.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
//                params.put("email", email);
//                params.put("password", password);
                http://dev8.edreamz3.com/API/productmgt.php/SetBillingaddress/?user_id=179&first_name=Vijay&
                // last_name=Badgujar&address=Riddhi Siddi Paradise&city=Pune&state=Delhi&country=India&pincode=411041
                // &mobile_number=942234078976
                params.put("user_id",session.getStringSessionData("uid"));
                params.put("first_name",strname);
                params.put("last_name",strlastname);
                params.put("address",straddress);
                params.put("city",strcity);
                params.put("state",cityId);
                params.put("country",countryId);
                params.put("pincode",strzipcode);
                params.put("mobile_number",strphonenumber);

                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private  void showAlertMessage(String alertMsg, final boolean flag)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Alert !");
        builder.setCancelable(false);
        builder.setMessage(alertMsg);
        //  builder.setPositiveButton("OK", null);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (flag) {

                    finish();
                } else
                    dialog.dismiss();


                //  dialog.dismiss();
            }
        });

        //  builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void getShippingAddress() {
        String tag_string_req = "getShippingAddress";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                vp.mom.api.AppConfig.GET_Billing_ADDRESS+"?user_id="+session.getStringSessionData("uid"), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                ArrayList<String> stringArrayListID = new ArrayList<String>();
                ArrayList<String> stringArrayListNAME = new ArrayList<String>();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("status");
                    // Check for error node in json
                    if (status) {
                        //Toast.makeText(PrivacyPolicy.this, "Your concern about this product is recorded", Toast.LENGTH_SHORT).show();
                        Log.v("res", "" + jObj);
                        btnSubmit.setText("Update");
                        JSONArray jsonArray = jObj.getJSONArray("items");

                        input_name.setText(jsonArray.getJSONObject(0).getString("first_name"));
                        Log.v("input name", "" + input_name);
                        input_lastname.setText(jsonArray.getJSONObject(0).getString("last_name"));
                        input_address.setText(jsonArray.getJSONObject(0).getString("address"));
                        //input_state.setText(jsonArray.getJSONObject(0).getString("address2"));
                        input_city.setText(jsonArray.getJSONObject(0).getString("city"));
                        inputcountry.setText(jsonArray.getJSONObject(0).getString("country_name"));
                        inputstate.setText(jsonArray.getJSONObject(0).getString("state_name"));
                        input_zipcode.setText(jsonArray.getJSONObject(0).getString("pincode"));
                        input_phonenumber.setText(jsonArray.getJSONObject(0).getString("mobile_number"));

                        countryId= jsonArray.getJSONObject(0).getString("cid");
                        cityId=  jsonArray.getJSONObject(0).getString("sid");

                        JSONArray countryArray = jsonArray.getJSONObject(0).getJSONArray("countries");

                        for (int i=0;i<countryArray.length();i++)
                        {
                            JSONObject  country = countryArray.getJSONObject(i);
                            stringArrayListID.add(country.getString("id"));
                            stringArrayListNAME.add(country.getString("name"));
                        }

                        all_country_array=stringArrayListNAME.toArray(new String[stringArrayListNAME.size()]);
                        all_country_id_array=stringArrayListID.toArray(new String[stringArrayListID.size()]);

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(vp.mom.activitys.BillingAddress.this,
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(vp.mom.activitys.BillingAddress.this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //   Log.e("ReportProduct", "ReportProduct Error: " + error.getMessage());
                Toast.makeText(vp.mom.activitys.BillingAddress.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //	hideDialog();
            }
        }) {

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private boolean validateName() {
        if (input_name.getText().toString().trim().isEmpty()) {
            input_layout_name.setError(getString(R.string.err_msg_name));
            requestFocus(input_name);
            return false;
        } else {
            input_layout_name.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateLAstName() {


        if (input_lastname.getText().toString().trim().isEmpty()) {
            input_layout_lastname.setError(getString(R.string.err_msg_name));
            requestFocus(input_lastname);
            return false;
        } else {
            input_layout_lastname.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateAddress() {
        if (input_address.getText().toString().trim().isEmpty()) {
            input_layout_address.setError(getString(R.string.err_msg_address));
            requestFocus(input_address);
            return false;
        } else {
            input_layout_address.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateCity() {
        if (input_city.getText().toString().trim().isEmpty()) {
            input_layout_City.setError(getString(R.string.err_msg_city));
            requestFocus(input_city);
            return false;
        } else {
            input_layout_City.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateState() {
        if(!(cityId != null && cityId.length() > 0))
        {
            Toast.makeText(getApplicationContext(), "Please select county", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean validateZip() {
        if (input_zipcode.getText().toString().trim().isEmpty()) {
            input_layout_zipcode.setError(getString(R.string.err_msg_zip));
            requestFocus(input_zipcode);
            return false;
        } else {
            input_layout_zipcode.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateCountry() {
        if(!(countryId != null && countryId.length() > 0))
        {
            Toast.makeText(getApplicationContext(), "Please select country", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean validatePhoneNumber() {
        if (input_phonenumber.getText().toString().trim().isEmpty()) {
            input_layout_phonenumber.setError(getString(R.string.err_msg_phone_number));
            requestFocus(input_phonenumber);
            return false;
        } else {
            input_layout_phonenumber.setErrorEnabled(false);
        }

        return true;
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void init() {
        input_name = (EditText) findViewById(R.id.input_name);
        input_lastname = (EditText) findViewById(R.id.input_lastname);
        input_address = (EditText) findViewById(R.id.input_address);
        input_city = (EditText) findViewById(R.id.input_city);
        input_zipcode = (EditText) findViewById(R.id.input_zipcode);
        input_phonenumber = (EditText) findViewById(R.id.input_phonenumber);
        inputstate= (Button) findViewById(R.id.input_state);
        inputcountry= (Button) findViewById(R.id.input_country);
        input_layout_name = (TextInputLayout) findViewById(R.id.input_layout_name);
        input_layout_lastname = (TextInputLayout) findViewById(R.id.input_layout_lastname);
        input_layout_address = (TextInputLayout) findViewById(R.id.input_layout_address);
        input_layout_City = (TextInputLayout) findViewById(R.id.input_layout_City);
        input_layout_State = (TextInputLayout) findViewById(R.id.input_layout_State);
        input_layout_zipcode = (TextInputLayout) findViewById(R.id.input_layout_zipcode);
        input_layout_country = (TextInputLayout) findViewById(R.id.input_layout_country);
        input_layout_phonenumber = (TextInputLayout) findViewById(R.id.input_layout_phonenumber);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
    }

    private class MyTextWatcher implements TextWatcher {
        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_address:
                    validateAddress();
                    break;
                case R.id.input_city:
                    validateCity();
                    break;
                case R.id.input_state:
                    validateState();
                    break;
                case R.id.input_zipcode:
                    validateZip();
                    break;
                case R.id.input_country:
                    validateCountry();
                    break;
                case R.id.input_phonenumber:
                    validatePhoneNumber();
                    break;
            }
        }
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
