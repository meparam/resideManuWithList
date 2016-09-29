package vp.mom.activitys;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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


public class ForgetPasswordActivty extends AppCompatActivity {
    private static final String TAG = vp.mom.activitys.ForgetPasswordActivty.class.getSimpleName();
    EditText inputPassword;
    Button btnPassword;
  //  private TextInputLayout inputLayoutEmail;
 // LinearLayout backll;
    ProgressDialog pDialog;
    String email;
    TextView titletool;
 //   private CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);

        pDialog = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
//        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
//                .forgetpsscoordinatorLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        if (toolbar != null) setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        titletool= (TextView) toolbar.findViewById(R.id.titletool);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titletool.setText("Forgot password");

       // back=(ImageView)toolbar.findViewById(R.id.forget_back);
//        backll=(LinearLayout)toolbar.findViewById(R.id.backllpass);
//        backll.setClickable(true);
//
//
//        backll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });


     //   inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);

        inputPassword= (EditText) findViewById(R.id.input_email);
        btnPassword= (Button) findViewById(R.id.btn_forget_password);

       // inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));

        btnPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateEmail()) {


                    forgetPassword();
                }

            }
        });
    }

    private void forgetPassword() {
        String tag_string_req = "forgetPassword";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                vp.mom.api.AppConfig.URL_FORGETPASSWORD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
          //      Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject data=new JSONObject(response);
                    if(data.getBoolean("status"))
                        showReponse("Password is sent to your mail");

                    else
                    Toast.makeText(getApplicationContext(), data.getString("message"), Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            //    Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
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

                params.put("email", email);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    void showReponse(String msg)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setMessage(msg).setTitle("Alert !").setIcon(R.drawable.app_logo)
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

//    private void  showMessage(String msg){
//        Snackbar snackbar = Snackbar
//                .make(coordinatorLayout, msg, Snackbar.LENGTH_LONG);
//        View sbView = snackbar.getView();
//        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
//
//        textView.setTextColor(Color.YELLOW);
//
//        snackbar.show();
//
//    }


    private boolean validateEmail() {
        email = inputPassword.getText().toString().trim();

        if (email.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please enter  email id", Toast.LENGTH_SHORT).show();
            return  false;
        }

        if (!isValidEmail(email)) {
            Toast.makeText(getApplicationContext(), "Please enter valid email id", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


//    private class MyTextWatcher implements TextWatcher {
//
//        private View view;
//
//        private MyTextWatcher(View view) {
//            this.view = view;
//        }
//
//        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//        }
//
//        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//        }
//
//        public void afterTextChanged(Editable editable) {
//            switch (view.getId()) {
//                case R.id.input_email:
//                    validateEmail();
//                    break;
//
//            }
//        }
//    }
}
