package vp.mom.activitys;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import vp.mom.R;
import vp.mom.app.AppController;

public class ManagePaypal extends AppCompatActivity {
    TextView tool_title;
    EditText paypalEmail;
    Button paypalButton;
    private ProgressDialog pDialog;
    vp.mom.api.SessionManager session;
    String email;
    TextView myClickableUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_paypal);
        session=new vp.mom.api.SessionManager(this);
        setCustomActionBar();
        pDialog = new ProgressDialog(vp.mom.activitys.ManagePaypal.this,
                R.style.AppTheme_Dark_Dialog);
        pDialog.setCancelable(false);

        myClickableUrl= (TextView) findViewById(R.id.paypalLink);
        SpannableString content1 = new SpannableString("Sign up now for free");
        content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
        myClickableUrl.setText(content1);

        paypalEmail= (EditText) findViewById(R.id.paypalEmail);
        paypalButton= (Button) findViewById(R.id.btnPaypal);

        paypalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(getValivation())
                {
                    AddPaypalEmail();
                }

            }
        });
        myClickableUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paypal.com/in/webapps/mpp/home"));
                startActivity(browserIntent);
            }
        });
        getPayPalEmail();
    }
    private boolean getValivation() {
        email = paypalEmail.getText().toString().trim();

        if(email.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please enter email id", Toast.LENGTH_SHORT).show();

            return false;
        }

        if (!isValidEmail(email)) {
            Toast.makeText(getApplicationContext(), "Please enter valid email id", Toast.LENGTH_SHORT).show();

            //showMessage("Please enter valid email id");
            return false;
        }

        return true;
    }
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private void setCustomActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null) setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tool_title = (TextView) toolbar.findViewById(R.id.tootlbar_title);
        tool_title.setText("PayPal account");
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void getPayPalEmail() {
        String tag_string_req = "getPayPalEmail";

        pDialog.setMessage("");
        // showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                vp.mom.api.AppConfig.GET_PAYPAL_EMAIL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //   Log.e("Login Response:", "Login Response: " + response.toString());
                //   hideDialog();
                if (response != null) {

                    // commentData.clear();
                    JSONObject jsonresponse= null;
                    try {
                        jsonresponse = new JSONObject(response.toString());

                    if(jsonresponse.getBoolean("status"))
                    {
                       JSONObject data=jsonresponse.getJSONObject("items");
                        paypalEmail.setText(data.getString("paypal_registered_email"));
                        paypalButton.setText("Update");
                    }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",session.getStringSessionData("uid"));
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
    private void AddPaypalEmail() {
        String tag_string_req = "AddPaypalEmail";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                vp.mom.api.AppConfig.SET_PAYAPAL_EMAIL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
             //   Log.d("AddPaypalEmail", "AddPaypalEmail Response: " + response.toString());
                //	hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("status");
                    // Check for error node in json
                    if (status) {


                        showAlert();

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(vp.mom.activitys.ManagePaypal.this,
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                  //  Toast.makeText(ManagePaypal.this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //   Log.e("ADD_COMMENTS", "ADD_COMMENTS Error: " + error.getMessage());
                Toast.makeText(vp.mom.activitys.ManagePaypal.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //	hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",session.getStringSessionData("uid"));
                params.put("paypal_email_id",email);

                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(vp.mom.activitys.ManagePaypal.this);
        builder.setMessage("Your PayPal email id is successfully added").setTitle("Alert !").setIcon(R.drawable.app_logo)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();


                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
