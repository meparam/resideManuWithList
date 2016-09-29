//package vp.mom.activitys;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.support.v7.app.ActionBarActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.paypal.android.MEP.PayPal;
//import com.paypal.android.MEP.PayPalActivity;
//import com.paypal.android.MEP.PayPalAdvancedPayment;
//import com.paypal.android.MEP.PayPalReceiverDetails;
//
//import java.math.BigDecimal;
//import java.util.Iterator;
//import java.util.Set;
//
//import vp.mom.R;
//
//
//public class NewCheckoutActivity extends ActionBarActivity {
//
//
//
//    public  static  final  String sand_box_id="APP-80W284485P519543T";
//    public  static  final  String paypal_liv_id="APP-0C165959RL117014F";
//
//
//
//
//    private static final String TAG = NewCheckoutActivity.class.getSimpleName();
//    public final int PAYPAL_RESPONSE = 100;
//    EditText editText_friend1_id;
//    EditText editText_friend1_amount;
//    EditText editText_friend2_id;
//    EditText editText_friend2_amount;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        editText_friend1_id = (EditText) findViewById(R.id.editText_friend1_id);
//        editText_friend1_amount = (EditText) findViewById(R.id.editText_friend1_amount);
//        editText_friend2_id = (EditText) findViewById(R.id.editText_friend2_id);
//        editText_friend2_amount = (EditText) findViewById(R.id.editText_friend2_amount);
//
//        Button paypal_button = (Button) findViewById(R.id.paypal_button);
//
//        initLibrary();
//        paypal_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // pay integration here
//
//                PayPalButtonClick(editText_friend1_id.getText().toString(),
//                        editText_friend1_amount.getText().toString(), editText_friend2_id.getText().toString(),
//                        editText_friend2_amount.getText().toString());
////
////                PayPalButtonClick(editText_friend1_id.getText().toString(), editText_friend1_amount.getText().toString(), editText_friend2_id.getText().toString(),
////                        editText_friend2_amount.getText().toString());
//
//            }
//        });
//
//    }
//
//    public void initLibrary() {
//        PayPal pp = PayPal.getInstance();
//        if (pp == null) {
//
//            pp = PayPal.initWithAppID(this, Util.sand_box_id,
//                    PayPal.ENV_SANDBOX);
//
//        }
//    }
//
//
//    public void PayPalButtonClick(String primary_id, String primary_amount,
//                                  String secondary_id, String secondary_amount) {
//        // Create a basic PayPal payment
//
//        // PayPalPayment newPayment = new PayPalPayment();
//        // newPayment.setSubtotal(new BigDecimal("1.0"));
//        // newPayment.setCurrencyType("USD");
//        // newPayment.setRecipient("npavankumar34@gmail.com");
//        // newPayment.setMerchantName("My Company");
//        // Log.d(TAG, "calling intent");
//        // if( PayPal.getInstance()!=null){
//        // Log.d(TAG, "in if");
//        // Intent paypalIntent = PayPal.getInstance().checkout(newPayment,
//        // this);
//        // startActivityForResult(paypalIntent, 1);
//        //
//
//        Log.d(TAG, "primary " + primary_id);
//        Log.d(TAG, "primary_amount " + primary_amount);
//
//        Log.d(TAG, "secondary_amount " + secondary_amount);
//        Log.d(TAG, "secondary_id " + secondary_id);
//
//        // config reciever1
//        PayPalReceiverDetails receiver0, receiver1;
//        receiver0 = new PayPalReceiverDetails();
//        receiver0.setRecipient(primary_id);
//        receiver0.setSubtotal(new BigDecimal(primary_amount));
//
//        // config reciever2
//        receiver1 = new PayPalReceiverDetails();
//        receiver1.setRecipient(secondary_id);
//        receiver1.setSubtotal(new BigDecimal(secondary_amount));
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
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        Log.d(TAG, "response");
//
//        if (requestCode == PAYPAL_RESPONSE) {
//
//            switch (resultCode) {
//                case Activity.RESULT_OK:
//                    //The payment succeeded
//                    String payKey =
//                            data.getStringExtra(PayPalActivity.EXTRA_PAY_KEY);
//                    Log.d(TAG, "success " + payKey);
//
//                    Toast.makeText(getApplicationContext(), "Payment done successfully ", Toast.LENGTH_LONG).show();
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
//
//}
