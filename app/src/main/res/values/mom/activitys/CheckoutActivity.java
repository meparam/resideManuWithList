//package vp.mom.activitys;
//
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Bundle;
//import android.support.v4.content.LocalBroadcastManager;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RadioButton;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.paypal.android.MEP.PayPal;
//import com.paypal.android.MEP.PayPalActivity;
//import com.paypal.android.MEP.PayPalAdvancedPayment;
//import com.paypal.android.MEP.PayPalReceiverDetails;
//
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import vp.mom.R;
//import vp.mom.api.AppConfig;
//import vp.mom.api.SessionManager;
//import vp.mom.app.AppController;
//import vp.mom.app.Config;
//import vp.mom.gcm.gcm.ChatRoomActivity;
//import vp.mom.gcm.gcm.NotificationUtils;
//import vp.mom.utils.StaticData;
//
///**
// * Created by pallavi.b on 22-Jan-16.
// */
//public class CheckoutActivity extends AppCompatActivity {
//    private BroadcastReceiver mRegistrationBroadcastReceiver;
//    String msg,chatRoomId,userName;
//    private NotificationUtils notificationUtils;
//    SessionManager session;
//    String productId,userId,productSize,productDisc,productPrice,productName,shippingCost;
//    String shipAdd;
//    private LinearLayout mLinearLayout,shippingLL,linearlayoutshipping,viacourier_layout;
//  //  private CheckBox mCheckBox;
//    RadioButton checkmeetperson,checkboxshipping;
//    private ImageView imgShippingAddress;
//    TextView txtShippingAddress;
//    TextView tooltitle,txtInternationalShipping,txtInternationalShippingAmount,
//            txtItemPriceAmt,txtTotalAmount,textmeetperson,txtSize;
////boolean meetInperson;
//    String deliveryType,payPalEmail;
//    private Button btnPayment;
//    private static final String TAG = CheckoutActivity.class.getSimpleName();
////    private static final String TAG = "paymentExample";
////    private static final int REQUEST_CODE_PAYMENT = 1;
////    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;
////    private static final String CONFIG_CLIENT_ID ="AcdxC5o3kTLtge0C8XQQ3QEuNDXyojaH1GQw1JZfk7KYWQMmRKoOol-DditXZlFNMG5fGY2NMCUztJ4O";
////    public static final String PAYMENT_INTENT = PayPalPayment.PAYMENT_INTENT_SALE;
//
//    public final int PAYPAL_RESPONSE = 100;
//
//    public  static String PAYPAL_APP_ID_LIVE="APP-04R573629J9509537";
//
//    public  static String PAYPAL_APP_ID_SAND_BOX="APP-80W284485P519543T";
////    private static PayPalConfiguration paypalConfig = new PayPalConfiguration()
////            .environment(CONFIG_ENVIRONMENT)
////            .clientId(CONFIG_CLIENT_ID);
//
//  //  private List<PayPalItem> productsInCart = new ArrayList<PayPalItem>();
//boolean meetInPersonFlag=true;
//    StringBuilder userShippingAddress;
//
//    private final String ADMIN_EMAIL="marketofmums_api1.outlook.com";
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.checkout);
//        userShippingAddress = new StringBuilder();
//        session=new SessionManager(this);
//        Intent data = getIntent();
//        if (data != null) {
//            productId = data.getStringExtra("productId");
//         //   userId = data.getStringExtra("userId");
//            productSize = data.getStringExtra("productSize");
//            productDisc = data.getStringExtra("productDisc");
//            productPrice = data.getStringExtra("productPrice");
//            productName= data.getStringExtra("product_name");
//            shippingCost= data.getStringExtra("shippingcost");
//            deliveryType= data.getStringExtra("deliveryType");
//            payPalEmail= data.getStringExtra("paypalEmail");
//        }
//        setCustomActionBar();
//
//
////        // Starting PayPal service
////        Intent intent = new Intent(this, PayPalService.class);
////        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);
////        startService(intent);
//
//        mLinearLayout = (LinearLayout) findViewById(R.id.linearlayoutshipping);
//        shippingLL= (LinearLayout) findViewById(R.id.shippingLL);
//        linearlayoutshipping= (LinearLayout) findViewById(R.id.linearlayoutshipping);
//        viacourier_layout= (LinearLayout) findViewById(R.id.viacourier_layout);
//        imgShippingAddress = (ImageView) findViewById(R.id.imgShippingAddress);
//        //  mCheckBox = (CheckBox) findViewById(R.id.checkBox);
//        txtShippingAddress = (TextView) findViewById(R.id.txtShippingAddress);
//        checkmeetperson = (RadioButton) findViewById(R.id.radio_meet_person1);
//        checkboxshipping = (RadioButton) findViewById(R.id.radio_meet_person2);
//        btnPayment = (Button) findViewById(R.id.btnPayment);
//        txtInternationalShipping = (TextView) findViewById(R.id.txtInternationalShipping);
//        txtTotalAmount= (TextView) findViewById(R.id.txtTotalAmount);
//        txtInternationalShippingAmount = (TextView) findViewById(R.id.txtInternationalShippingAmount);
//        txtItemPriceAmt = (TextView) findViewById(R.id.txtItemPriceAmt);
//        textmeetperson= (TextView) findViewById(R.id.textmeetperson);
//
//        txtSize=(TextView) findViewById(R.id.txtSize);
//
//        txtSize.setText(productSize);
//        txtItemPriceAmt.setText("£" + productPrice);
//
//        if(deliveryType.equalsIgnoreCase("shipping"))
//        {
//            BigDecimal amount= getAamoutOfProduct(1);
//
//            textmeetperson.setVisibility(View.VISIBLE);
//            checkmeetperson.setVisibility(View.GONE);
//            mLinearLayout.setVisibility(View.VISIBLE);
//            checkboxshipping.setChecked(true);
//            txtInternationalShipping.setText("International Shipping");
//            txtInternationalShippingAmount.setText("£" + shippingCost);
//            txtTotalAmount.setText("£"+amount);
//            meetInPersonFlag=true;
//
//        }
//            else if(deliveryType.equalsIgnoreCase("Meet in Person"))
//        {
//            checkboxshipping.setChecked(false);
//            checkmeetperson.setChecked(true);
//
//            BigDecimal amount= getAamoutOfProduct(2);
//            mLinearLayout.setVisibility(View.GONE);
//            viacourier_layout.setVisibility(View.GONE);
//            txtInternationalShipping.setText("Meet in person");
//            txtTotalAmount.setText("£"+amount);
//            shippingLL.setVisibility(View.GONE);
//            meetInPersonFlag=false;
//        }
//        else
//        {
//            meetInPersonFlag=true;
//           // double amount= getAamoutOfProduct(3);
//            BigDecimal amount= getAamoutOfProduct(1);
//         //   textmeetperson.setVisibility(View.VISIBLE);
//          //  checkmeetperson.setVisibility(View.GONE);
//            mLinearLayout.setVisibility(View.VISIBLE);
//            checkboxshipping.setChecked(true);
//            checkmeetperson.setChecked(false);
//            txtInternationalShipping.setText("International Shipping");
//            txtInternationalShippingAmount.setText("£" + shippingCost);
//            txtTotalAmount.setText("£"+amount);
//        }
//        checkmeetperson.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                checkboxshipping.setChecked(false);
//                boolean checked = ((RadioButton) v).isChecked();
//                if (checked) {
//                    mLinearLayout.setVisibility(View.GONE);
//                    txtInternationalShipping.setText("Meet in person");
//                    txtTotalAmount.setText("£" + getAamoutOfProduct(3));
//                    shippingLL.setVisibility(View.GONE);
//
//                } else {
//                    mLinearLayout.setVisibility(View.VISIBLE);
//                }
//
//            }
//        });
//
//        checkboxshipping.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                checkmeetperson.setChecked(false);
//                boolean checked = ((RadioButton) v).isChecked();
//                if (checked) {
//
//                    shippingLL.setVisibility(View.VISIBLE);
//                    mLinearLayout.setVisibility(View.VISIBLE);
//                    txtInternationalShipping.setText("International Shipping");
//                    txtInternationalShippingAmount.setText("£" + shippingCost);
//                    txtTotalAmount.setText("£" + getAamoutOfProduct(1));
//                } else
//                    mLinearLayout.setVisibility(View.GONE);
//            }
//        });
//
//
//
//        linearlayoutshipping.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(CheckoutActivity.this, ShippingAddressActivity.class);
//                startActivity(intent);
//            }
//        });
//
//
////        txtShippingAddress.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////
////            }
////        });
//
//        btnPayment.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                String finalAmount=txtTotalAmount.getText().toString().trim().replace("£","");
//       if(getValidation()) {
//
//
//           double price = Double.parseDouble(finalAmount);
//
//           double percentage = (price / 100.0f) * 8;;
//
//            Double sellerAmt= price-percentage;
//
//           try {
//               PayPalButtonClick(ADMIN_EMAIL, new BigDecimal(percentage), payPalEmail, new BigDecimal(sellerAmt));
//           } catch (Exception e) {
//
//           }
//       }
//
//
////                String finalAmount=txtTotalAmount.getText().toString().trim().replace("£","");
////
////                productsInCart.clear();
////
////                PayPalItem item = new PayPalItem(productName, 1,
////                        new BigDecimal(finalAmount), "GBP", productId);
////
////                productsInCart.add(item);
////
////            if(getValidation())
////            {
////                launchPayPalPayment();
////            }
//
//            }
//        });
//        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
//                if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
//                    // new push notification is received
//                    handlePushNotification(intent);
//                }
//            }
//        };
//
//        shipAdd=session.getStringSessionData(AppConfig.SHIPPING_ADD);
//
//        if(!shipAdd.isEmpty())
//        {
//            userShippingAddress = new StringBuilder();
//            try
//            {
//                JSONObject jObj = new JSONObject(shipAdd);
//                JSONArray jsonArray = jObj.getJSONArray("items");
//                JSONObject add_data=     jsonArray.getJSONObject(0);
//
//                userShippingAddress.append(add_data.getString("shipping_fname"));
//                userShippingAddress.append(",");
//
//                userShippingAddress.append(add_data.getString("shipping_lname"));
//                userShippingAddress.append(",");
//
//                userShippingAddress.append(add_data.getString("shipping_address"));
//                userShippingAddress.append(",");
//
//                userShippingAddress.append(add_data.getString("shipping_city"));
//                userShippingAddress.append(",");
//
//                userShippingAddress.append(add_data.getString("shipping_state"));
//                userShippingAddress.append(",");
//
//                userShippingAddress.append(add_data.getString("shipping_zip"));
//
//                txtShippingAddress.setText(userShippingAddress);
//            }
//            catch (Exception ex)
//            {
//Log.e("exception",""+ex.toString());
//            }
//        }
//        initLibrary();
//}
//
//
//    public void initLibrary() {
//        PayPal pp = PayPal.getInstance();
//        if (pp == null) {
//
//            pp = PayPal.initWithAppID(this, "APP-80W284485P519543T",
//                    PayPal.ENV_SANDBOX);
//
//        }
//    }
//
//    public void PayPalButtonClick(String adminEmail, BigDecimal adminAmount,
//                                  String sellerEmail, BigDecimal sellerAmount) {
//
//        PayPalReceiverDetails receiver0, receiver1;
//        receiver0 = new PayPalReceiverDetails();
//        receiver0.setRecipient(adminEmail);
//        receiver0.setSubtotal(new BigDecimal("2.0"));
//
//        // config reciever2
//        receiver1 = new PayPalReceiverDetails();
//        receiver1.setRecipient(sellerEmail);
//        receiver1.setSubtotal(new BigDecimal("3.0"));
//
//        // adding payment type
//        PayPalAdvancedPayment advPayment = new PayPalAdvancedPayment();
////        advPayment.setCurrencyType("USD");
//        advPayment.setCurrencyType("GBP");
//
//
//        advPayment.getReceivers().add(receiver0);
//        advPayment.getReceivers().add(receiver1);
//        Intent paypalIntent = PayPal.getInstance().checkout(advPayment, this);
//        this.startActivityForResult(paypalIntent, PAYPAL_RESPONSE);
//
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        Log.e(TAG, "response"+data.getExtras().toString());
//
//        if (requestCode == PAYPAL_RESPONSE) {
//
//            switch (resultCode) {
//                case Activity.RESULT_OK:
//
//                    String payKey =
//                            data.getStringExtra(PayPalActivity.EXTRA_PAY_KEY);
//                    Log.e(TAG, "success " + payKey);
//
//                    BigDecimal    productCost=   new BigDecimal(productPrice);
//
//                    BigDecimal finalProductAmount =productCost.add(new BigDecimal(shippingCost));
//
//                    sendPaymentToServer(productPrice, shippingCost, "" + finalProductAmount);
//
//                  //  Toast.makeText(getApplicationContext(), "Payment done successfully ", Toast.LENGTH_LONG).show();
//
//
//                    break;
//                case Activity.RESULT_CANCELED:
//                    Toast.makeText(getApplicationContext(), "Payment Canceled , Try again ", Toast.LENGTH_LONG).show();
//
//
//                    break;
//                case PayPalActivity.RESULT_FAILURE:
//                    Toast.makeText(getApplicationContext(), "Payment failed , Try again ", Toast.LENGTH_LONG).show();
//                    break;
//            }
//
//
//        }
//
//    }
//
//    private boolean getValidation() {
//
//
//                if(meetInPersonFlag)
//                {
//                    if(!(txtShippingAddress != null && txtShippingAddress.length() > 0))
//
//                    {
//                        Toast.makeText(getApplicationContext(), "Please enter shipping address", Toast.LENGTH_SHORT).show();
//                        return false;
//                    }
//                }
//
////        if (productsInCart.size() <= 0)
////        {
////            Toast.makeText(getApplicationContext(), "Cart is empty! Please add few products to cart.",
////                    Toast.LENGTH_SHORT).show();
////            return false;
////        }
//
//        return true;
//    }
//
//
//
//    private void handlePushNotification(Intent intent) {
//        int type = intent.getIntExtra("type", -1);
//
//        if (type == Config.PUSH_TYPE_CHATROOM) {
//            msg = (String)intent.getSerializableExtra("message");
//
//            chatRoomId  = intent.getStringExtra("UserToChat");
//
//            userName=intent.getStringExtra("userName");
//
//        }
//        long when = System.currentTimeMillis();
//        // app is in background. show the message in notification try
//        Intent resultIntent = new Intent(getApplicationContext(), ChatRoomActivity.class);
//        resultIntent.putExtra("UserToChat", chatRoomId);
//        resultIntent.putExtra("name", userName);
//        showNotificationMessage(getApplicationContext(), userName, msg,
//                ""+when, resultIntent);
//
//    }
//    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
//        notificationUtils = new NotificationUtils(context);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
//    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        shipAdd=session.getStringSessionData(AppConfig.SHIPPING_ADD);
//        userShippingAddress = new StringBuilder();
//      //  Log.e("exception","outside ");
//        if(StaticData.shippingAddressFlag)
//        {
//            if(!shipAdd.isEmpty())
//            {
//                Log.e("exception","onResume"+shipAdd);
//                try
//                {
//
//
//
//                    JSONObject jObj = new JSONObject(shipAdd);
//                    JSONArray jsonArray = jObj.getJSONArray("items");
//                    JSONObject add_data=     jsonArray.getJSONObject(0);
//
//                    userShippingAddress.append(add_data.getString("shipping_fname"));
//                    userShippingAddress.append(",");
//
//                    userShippingAddress.append(add_data.getString("shipping_lname"));
//                    userShippingAddress.append(",");
//
//                    userShippingAddress.append(add_data.getString("shipping_address"));
//                    userShippingAddress.append(",");
//
//                    userShippingAddress.append(add_data.getString("shipping_city"));
//                    userShippingAddress.append(",");
//
//                    userShippingAddress.append(add_data.getString("shipping_state"));
//                    userShippingAddress.append(",");
//
//                    userShippingAddress.append(add_data.getString("shipping_country"));
//                    userShippingAddress.append(",");
//
//                    userShippingAddress.append(add_data.getString("shipping_zip"));
//                    txtShippingAddress.setText(userShippingAddress);
//                }
//                catch (Exception ex)
//                {
//                    Log.e("exception","onResume"+ex.toString());
//                }
//
//            }
//        }
//        StaticData.shippingAddressFlag=false;
//        // register new push message receiver
//        // by doing this, the activity will be notified each time a new message arrives
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                new IntentFilter(Config.PUSH_NOTIFICATION));
//
//        // clearing the notification tray
//        // NotificationUtils.clearNotifications();
//    }
//
//    @Override
//    protected void onPause() {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
//        super.onPause();
//    }
//    private BigDecimal getAamoutOfProduct(int type) {
//        BigDecimal finalAMt = null;
//        if(type==1)
//        {
//            try {
//                 finalAMt=  new BigDecimal(productPrice).add( new BigDecimal(shippingCost));
//
//            }catch(Exception excep)
//            {
//
//            }
//        //    return finalAMt;
//        }
//        else
//        {
//
//            try {
//                finalAMt = new BigDecimal(productPrice);
//
//            }catch(Exception excep)
//            {
//
//            }
//
//        }
//        return finalAMt;
//    }
//
////    private void launchPayPalPayment() {
////
////        PayPalPayment thingsToBuy = prepareFinalCart();
////
////        Intent intent = new Intent(CheckoutActivity.this, PaymentActivity.class);
////
////        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);
////
////        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingsToBuy);
////
////        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
////    }
//
////    private PayPalPayment prepareFinalCart() {
////
////        PayPalItem[] items = new PayPalItem[productsInCart.size()];
////        items = productsInCart.toArray(items);
////
////        // Total amount
////        BigDecimal subtotal = PayPalItem.getItemTotal(items);
////
////        // If you have shipping cost, add it here
////        BigDecimal shipping = new BigDecimal("0.0");
////
////        // If you have tax, add it here
////        BigDecimal tax = new BigDecimal("0.0");
////
////        PayPalPaymentDetails paymentDetails = new PayPalPaymentDetails(
////                shipping, subtotal, tax);
////
////        BigDecimal amount = subtotal.add(shipping).add(tax);
////
////        PayPalPayment payment = new PayPalPayment(
////                amount,
////                "GBP",
////                "Description about transaction. This will be displayed to the user.",
////                PAYMENT_INTENT);
////
////        payment.items(items).paymentDetails(paymentDetails);
////
////        // Custom field like invoice_number etc.,
////        payment.custom("This is text that will be associated with the payment that the app can use.");
////
////        return payment;
////    }
//
//    private void setCustomActionBar() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
//        if (toolbar != null) setSupportActionBar(toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        tooltitle = (TextView) findViewById(R.id.tooltitle);
//        tooltitle.setText("Checkout");
//
//    }
////    @Override
////    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////        if (requestCode == REQUEST_CODE_PAYMENT) {
////            if (resultCode == Activity.RESULT_OK) {
////                PaymentConfirmation confirm = data
////                        .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
////                if (confirm != null) {
////                    try {
////                      //  Log.e(TAG, confirm.toJSONObject().toString(4));
////
////                       // Log.e(TAG, confirm.getPayment().toJSONObject()
////                          //      .toString(4));
////
////                        String paymentId = confirm.toJSONObject()
////                                .getJSONObject("response").getString("id");
////
////                        String payment_client = confirm.getPayment()
////                                .toJSONObject().toString();
////
//////                        Log.e(TAG, "paymentId: " + paymentId
//////                                + ", payment_json: " + payment_client);
////
////                        try {
////                            JSONObject jObj = new JSONObject(payment_client);
////
////                            String amount=jObj.getString("amount");
////
////                            JSONObject jobj1=jObj.getJSONObject("details");
////
////                            String shipping=jobj1.getString("shipping");
////
////                            String subtotal=jobj1.getString("subtotal");
////
////                         //   Log.e("amount",""+amount+"  "+shipping+"  "+subtotal);
////                            sendPaymentToServer(amount, shipping, subtotal);
////                        }
////                        catch (Exception e)
////                        {
////                        }
////                    } catch (JSONException e) {
////                        Log.e(TAG, "an extremely unlikely failure occurred: ",
////                                e);
////                    }
////                }
////            } else if (resultCode == Activity.RESULT_CANCELED) {
////                Log.e(TAG, "The user canceled.");
////            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
////                Log.e(TAG,
////                        "An invalid Payment or PayPalConfiguration was submitted.");
////            }
////        }
////    }
//
//
//    private void sendPaymentToServer(final String amt, final String shippingAmt, final String totalAmt) {
//        String tag_string_req = "storePayment";
//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                AppConfig.BUY_PRODUCT, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                Log.d("storePayment", "storePayment Response: " + response.toString());
//                //	hideDialog();
//
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    boolean status = jObj.getBoolean("status");
//                    // Check for error node in json
//                    if (status) {
//
//                        String msg="You have made successful purchase of  "+productName;
//
//                        final AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutActivity.this);
//                        builder.setCancelable(false);
//                        builder.setIcon(R.drawable.app_logo);
//                        builder.setMessage(msg).setTitle("Congratulations !")
//                                .setCancelable(false)
//                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.dismiss();
//                                        finish();
//
//
//                                    }
//                                });
//                        AlertDialog alert = builder.create();
//                        alert.show();
//                        // user successfully logged in
//                   //     Log.e("storePayment", "storePayment Response: " + response.toString());
//
//                    } else {
//                        // Error in login. Get the error message
//                        String errorMsg = jObj.getString("message");
//                        Toast.makeText(CheckoutActivity.this,
//                                errorMsg, Toast.LENGTH_LONG).show();
//                    }
//                } catch (JSONException e) {
//                    // JSON error
//                    e.printStackTrace();
//                    Toast.makeText(CheckoutActivity.this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//             //   Log.e("storePayment", "storePayment Error: " + error.getMessage());
//                Toast.makeText(CheckoutActivity.this,
//                        error.getMessage(), Toast.LENGTH_LONG).show();
//                //	hideDialog();
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                // Posting parameters to login url
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("user_id",session.getStringSessionData("uid"));
//                params.put("product_id",productId);
//                params.put("total_amount",amt);
//                params.put("discount","00");
//                params.put("final_amount",totalAmt);
//                params.put("shipping_charges",shippingAmt);
//                params.put("quantity","1");
//                return params;
//            }
//
//        };
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
//    }
//
//    }
