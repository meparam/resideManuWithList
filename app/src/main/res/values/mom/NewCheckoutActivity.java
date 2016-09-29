package vp.mom;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.PayPalAdvancedPayment;
import com.paypal.android.MEP.PayPalReceiverDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import vp.mom.activitys.ShippingAddressActivity;
import vp.mom.app.AppController;

public class NewCheckoutActivity extends AppCompatActivity {
    String productId, userId, productSize, productDisc, productPrice, productName, shippingCost, deliveryType, payPalEmail;
    StringBuilder userShippingAddress;
    String msg, chatRoomId, userName;
    private vp.mom.gcm.gcm.NotificationUtils notificationUtils;
    vp.mom.api.SessionManager session;
    String shipAdd;
    private LinearLayout mLinearLayout, shippingLL, linearlayoutshipping, viacourier_layout;
    TextView txtShippingAddress;
    TextView tooltitle, txtInternationalShipping, txtInternationalShippingAmount,
            txtItemPriceAmt, txtTotalAmount, textmeetperson, txtSize;
    private Button btnPayment;
    RadioButton checkmeetperson, checkboxshipping;
    private ImageView imgShippingAddress;
    boolean meetInPersonFlag = true;
    private final String ADMIN_EMAIL = "marketofmums@outlook.com";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    BigDecimal finalAMt;
    private ProgressDialog pDialog;
    String customer_item_shipping_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);
        pDialog = new ProgressDialog(vp.mom.NewCheckoutActivity.this,
                R.style.AppTheme_Dark_Dialog);
        pDialog.setCancelable(false);
        userShippingAddress = new StringBuilder();
        session = new vp.mom.api.SessionManager(this);

        Intent data = getIntent();
        if (data != null) {
 //  String abc=         session.getStringSessionData("uid");
            productId = data.getStringExtra("productId");
            //   userId = data.getStringExtra("userId");
            productSize = data.getStringExtra("productSize");
            productDisc = data.getStringExtra("productDisc");
            productPrice = data.getStringExtra("productPrice");
            productName = data.getStringExtra("product_name");
            shippingCost = data.getStringExtra("shippingcost");
            deliveryType = data.getStringExtra("deliveryType");
            payPalEmail = data.getStringExtra("paypalEmail");
        }

        setCustomActionBar();

        mLinearLayout = (LinearLayout) findViewById(R.id.linearlayoutshipping);
        shippingLL = (LinearLayout) findViewById(R.id.shippingLL);
        linearlayoutshipping = (LinearLayout) findViewById(R.id.linearlayoutshipping);
        viacourier_layout = (LinearLayout) findViewById(R.id.viacourier_layout);
        imgShippingAddress = (ImageView) findViewById(R.id.imgShippingAddress);
        //  mCheckBox = (CheckBox) findViewById(R.id.checkBox);
        txtShippingAddress = (TextView) findViewById(R.id.txtShippingAddress);
        checkmeetperson = (RadioButton) findViewById(R.id.radio_meet_person1);
        checkboxshipping = (RadioButton) findViewById(R.id.radio_meet_person2);
        btnPayment = (Button) findViewById(R.id.btnPayment);
        txtInternationalShipping = (TextView) findViewById(R.id.txtInternationalShipping);
        txtTotalAmount = (TextView) findViewById(R.id.txtTotalAmount);
        txtInternationalShippingAmount = (TextView) findViewById(R.id.txtInternationalShippingAmount);
        txtItemPriceAmt = (TextView) findViewById(R.id.txtItemPriceAmt);
        textmeetperson = (TextView) findViewById(R.id.textmeetperson);

        txtSize = (TextView) findViewById(R.id.txtSize);

        txtSize.setText(productSize);
        txtItemPriceAmt.setText("£" + productPrice);

        if (deliveryType.equalsIgnoreCase("shipping")) {
            BigDecimal amount = getAamoutOfProduct(1);

            textmeetperson.setVisibility(View.VISIBLE);
            checkmeetperson.setVisibility(View.GONE);
            mLinearLayout.setVisibility(View.VISIBLE);
            checkboxshipping.setChecked(true);
            txtInternationalShipping.setText("International Shipping");
            txtInternationalShippingAmount.setText("£" + shippingCost);
            txtTotalAmount.setText("£" + amount);
            meetInPersonFlag = true;

        } else if (deliveryType.equalsIgnoreCase("Meet in Person")) {
            checkboxshipping.setChecked(false);
            checkmeetperson.setChecked(true);

            BigDecimal amount = getAamoutOfProduct(2);
            mLinearLayout.setVisibility(View.GONE);
            viacourier_layout.setVisibility(View.GONE);
            txtInternationalShipping.setText("Meet in person");
            txtTotalAmount.setText("£" + amount);
            shippingLL.setVisibility(View.GONE);
            meetInPersonFlag = false;
        } else {
            meetInPersonFlag = true;
            // double amount= getAamoutOfProduct(3);
            BigDecimal amount = getAamoutOfProduct(1);
            //   textmeetperson.setVisibility(View.VISIBLE);
            //  checkmeetperson.setVisibility(View.GONE);
            mLinearLayout.setVisibility(View.VISIBLE);
            checkboxshipping.setChecked(true);
            checkmeetperson.setChecked(false);
            txtInternationalShipping.setText("International Shipping");
            txtInternationalShippingAmount.setText("£" + shippingCost);
            txtTotalAmount.setText("£" + amount);
        }
        checkmeetperson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meetInPersonFlag = false;
                checkboxshipping.setChecked(false);
                boolean checked = ((RadioButton) v).isChecked();
                if (checked) {
                    mLinearLayout.setVisibility(View.GONE);
                    txtInternationalShipping.setText("Meet in person");
                    txtTotalAmount.setText("£" + getAamoutOfProduct(3));
                    shippingLL.setVisibility(View.GONE);
                    customer_item_shipping_type="Meet in Person";
                } else {
                    mLinearLayout.setVisibility(View.VISIBLE);
                }

            }
        });

        checkboxshipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                checkmeetperson.setChecked(false);
                boolean checked = ((RadioButton) v).isChecked();
                if (checked) {
                    meetInPersonFlag = true;
                    shippingLL.setVisibility(View.VISIBLE);
                    mLinearLayout.setVisibility(View.VISIBLE);
                    txtInternationalShipping.setText("International Shipping");
                    txtInternationalShippingAmount.setText("£" + shippingCost);
                    txtTotalAmount.setText("£" + getAamoutOfProduct(1));
                    customer_item_shipping_type="Via Courier";
                } else
                    mLinearLayout.setVisibility(View.GONE);
            }
        });


        linearlayoutshipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(vp.mom.NewCheckoutActivity.this, ShippingAddressActivity.class);
                startActivity(intent);
            }
        });


//        txtShippingAddress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        btnPayment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String finalAmount = txtTotalAmount.getText().toString().trim().replace("£", "");
                if (getValidation()) {


                    double price = Double.parseDouble(finalAmount);

                    double percentage = (price / 100.0f) * 8;


                    Double sellerAmt = price - percentage;

                    try {
                        PayPalButtonClick(ADMIN_EMAIL, new BigDecimal(percentage), payPalEmail, new BigDecimal(sellerAmt));
                    } catch (Exception e) {

                    }
                }


//                String finalAmount=txtTotalAmount.getText().toString().trim().replace("£","");
//
//                productsInCart.clear();
//
//                PayPalItem item = new PayPalItem(productName, 1,
//                        new BigDecimal(finalAmount), "GBP", productId);
//
//                productsInCart.add(item);
//
//            if(getValidation())
//            {
//                launchPayPalPayment();
//            }

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

        shipAdd = session.getStringSessionData(vp.mom.api.AppConfig.SHIPPING_ADD);

        if (!shipAdd.isEmpty()) {
            userShippingAddress = new StringBuilder();
            try {
                JSONObject jObj = new JSONObject(shipAdd);
                JSONArray jsonArray = jObj.getJSONArray("items");
                JSONObject add_data = jsonArray.getJSONObject(0);

                userShippingAddress.append(add_data.getString("shipping_fname"));
                userShippingAddress.append(",");

                userShippingAddress.append(add_data.getString("shipping_lname"));
                userShippingAddress.append(",");

                userShippingAddress.append(add_data.getString("shipping_address"));
                userShippingAddress.append(",");

                userShippingAddress.append(add_data.getString("shipping_city"));
                userShippingAddress.append(",");

                userShippingAddress.append(add_data.getString("shipping_state_name"));
                userShippingAddress.append(",");

                userShippingAddress.append(add_data.getString("shipping_country_name"));
                userShippingAddress.append(",");

                userShippingAddress.append(add_data.getString("shipping_zip"));

                txtShippingAddress.setText(userShippingAddress);
            } catch (Exception ex) {
                Log.e("exception", "" + ex.toString());
            }
        }

        initLibrary();
        //   PayPalButtonClick("abc.@gail.com",new BigDecimal(2),"abcd.@gail.com",new BigDecimal(2));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        shipAdd = session.getStringSessionData(vp.mom.api.AppConfig.SHIPPING_ADD);
        userShippingAddress = new StringBuilder();
      //  Log.e("exception", "onResume" + shipAdd);
        //  Log.e("exception","outside ");
        if (vp.mom.utils.StaticData.shippingAddressFlag) {
            if (!shipAdd.isEmpty()) {
              //  Log.e("exception", "onResume" + shipAdd);
                try {
                    JSONObject jObj = new JSONObject(shipAdd);
                    JSONArray jsonArray = jObj.getJSONArray("items");
                    JSONObject add_data = jsonArray.getJSONObject(0);

                    userShippingAddress.append(add_data.getString("shipping_fname"));
                    userShippingAddress.append(",");

                    userShippingAddress.append(add_data.getString("shipping_lname"));
                    userShippingAddress.append(",");

                    userShippingAddress.append(add_data.getString("shipping_address"));
                    userShippingAddress.append(",");

                    userShippingAddress.append(add_data.getString("shipping_city"));
                    userShippingAddress.append(",");

                    userShippingAddress.append(add_data.getString("shipping_state_name"));
                    userShippingAddress.append(",");

                    userShippingAddress.append(add_data.getString("shipping_country_name"));
                    userShippingAddress.append(",");

                    userShippingAddress.append(add_data.getString("shipping_zip"));
                    txtShippingAddress.setText(userShippingAddress);
                } catch (Exception ex) {
                  //  Log.e("exception", "onResume" + ex.toString());
                }

            }
        }
        vp.mom.utils.StaticData.shippingAddressFlag = false;
        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(vp.mom.app.Config.PUSH_NOTIFICATION));

        // clearing the notification tray
        // NotificationUtils.clearNotifications();
    }

    private boolean getValidation() {

        if(checkboxshipping.isChecked())
        customer_item_shipping_type="Via courier";

        else
            customer_item_shipping_type="Meet in Person";



        if (meetInPersonFlag) {
            if (!(txtShippingAddress != null && txtShippingAddress.length() > 0))

            {
                Toast.makeText(getApplicationContext(), "Please enter shipping address", Toast.LENGTH_SHORT).show();
                return false;
            }
        }


        return true;
    }

    private BigDecimal getAamoutOfProduct(int type) {

        if (type == 1) {
            try {
                finalAMt = new BigDecimal(productPrice).add(new BigDecimal(shippingCost));

            } catch (Exception excep) {

            }
            //    return finalAMt;
        } else {

            try {
                finalAMt = new BigDecimal(productPrice);

            } catch (Exception excep) {

            }

        }
        return finalAMt;
    }

    public void initLibrary() {
        PayPal pp = PayPal.getInstance();
        if (pp == null) {

            pp = PayPal.initWithAppID(this, "APP-04R573629J9509537",
                    PayPal.ENV_LIVE);

//            pp = PayPal.initWithAppID(this, "APP-80W284485P519543T",
//                    PayPal.ENV_SANDBOX);

        }
    }

    private void handlePushNotification(Intent intent) {
        int type = intent.getIntExtra("type", -1);

        if (type == vp.mom.app.Config.PUSH_TYPE_CHATROOM) {
            msg = (String) intent.getSerializableExtra("message");

            chatRoomId = intent.getStringExtra("UserToChat");
            userName = intent.getStringExtra("userName");

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
        notificationUtils = new vp.mom.gcm.gcm.NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    public void PayPalButtonClick(String adminEmail, BigDecimal adminAmount,
                                  String sellerEmail, BigDecimal sellerAmount) {

        PayPalReceiverDetails receiver0, receiver1;
        receiver0 = new PayPalReceiverDetails();
        receiver0.setRecipient(adminEmail);
        receiver0.setSubtotal(adminAmount);

        // config reciever2
        receiver1 = new PayPalReceiverDetails();
        receiver1.setRecipient(sellerEmail);
        receiver1.setSubtotal(sellerAmount);

        // adding payment type
        PayPalAdvancedPayment advPayment = new PayPalAdvancedPayment();
//        advPayment.setCurrencyType("USD");
        advPayment.setCurrencyType("GBP");


        advPayment.getReceivers().add(receiver0);
        advPayment.getReceivers().add(receiver1);
        Intent paypalIntent = PayPal.getInstance().checkout(advPayment, this);
        this.startActivityForResult(paypalIntent, 123);


    }


    private void setCustomActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null) setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.setBooleanSessionData("doPayment",false);
                finish();
            }
        });
        tooltitle = (TextView) findViewById(R.id.tooltitle);
        tooltitle.setText("Checkout");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

//        Log.e("abc", "response" + data.getExtras().toString());

        if (requestCode == 123) {

            switch (resultCode) {
                case Activity.RESULT_OK:

                    String payKey =
                            data.getStringExtra(PayPalActivity.EXTRA_PAY_KEY);
                    pDialog.setMessage("Loading ...");
                    showDialog();
                    if(session.getBooleanSessionData("doPayment"))
                    sendPaymentToServer();


                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(getApplicationContext(), "Payment Canceled , Try again ", Toast.LENGTH_LONG).show();


                    break;
                case PayPalActivity.RESULT_FAILURE:
                    Toast.makeText(getApplicationContext(), "Payment failed , Try again ", Toast.LENGTH_LONG).show();
                    break;
            }


        }

    }

    private void sendPaymentToServer() {


        String tag_string_req = "storePayment";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                vp.mom.api.AppConfig.BUY_PRODUCT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("storePayment", "storePayment Response: " + response.toString());


                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("status");
                    // Check for error node in json
                    if (status) {
                        session.setBooleanSessionData("doPayment",false);
                        String msg = "You have made successful purchase of  " + productName;
                        hideDialog();
                        Intent setting=new Intent(vp.mom.NewCheckoutActivity.this, vp.mom.activitys.PaymentdoneMessageActivity.class);
                        setting.putExtra("pName",msg);
                        startActivity(setting);
                        finish();


//                        Log.e("storePayment", "storePayment Response: " + response.toString());
//                        String msg = "You have made successful purchase of  " + productName;
//
//                        final AlertDialog.Builder builder = new AlertDialog.Builder(NewCheckoutActivity.this);
//                        builder.setCancelable(false);
//                        builder.setIcon(R.drawable.app_logo);
//                        builder.setMessage(msg).setTitle("Congratulations !")
//                                .setCancelable(false)
//                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.dismiss();
//                                        finish();
//                                    }
//                                });
//                        AlertDialog alert = builder.create();
//                        alert.show();
                        // user successfully logged in
                        //     Log.e("storePayment", "storePayment Response: " + response.toString());

                    } else {
                        hideDialog();
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(vp.mom.NewCheckoutActivity.this,
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    hideDialog();
                    e.printStackTrace();
                    Toast.makeText(vp.mom.NewCheckoutActivity.this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                   Log.e("storePayment", "storePayment Error: " + error.getMessage());
                Toast.makeText(vp.mom.NewCheckoutActivity.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",session.getStringSessionData("uid"));
                params.put("product_id", productId);
                params.put("total_amount", productPrice);
                params.put("discount","00");
                params.put("final_amount", ""+finalAMt);
                params.put("shipping_charges",shippingCost);
                params.put("quantity","1");
                params.put("shipping_type",customer_item_shipping_type);

                Log.e("params", "params " + params.toString());

                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    public void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
