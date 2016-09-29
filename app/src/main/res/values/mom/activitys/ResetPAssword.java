package vp.mom.activitys;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import vp.mom.api.AppConfig;
import vp.mom.app.AppController;

public class ResetPAssword extends AppCompatActivity {
    TextView title;
    Button btnResetPassword;
    EditText inputOldPassword,inputNewPassword,inputConfirmPassword;
    String oldPassword,newPassword,confirmPassword;
    vp.mom.api.SessionManager session;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_view);
        session=new vp.mom.api.SessionManager(this);
        pDialog = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        pDialog.setCancelable(false);
        initActionBar();
        inputOldPassword= (EditText) findViewById(R.id.inputOldPassword);
        inputNewPassword= (EditText) findViewById(R.id.inputNewPassword);
        inputConfirmPassword= (EditText) findViewById(R.id.inputConfirmPassword);
        btnResetPassword= (Button) findViewById(R.id.btnResetPassword);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getValidation())
                {
                    ChangePassword();
                }
            }
        });
    }

    private boolean getValidation() {

        oldPassword=inputOldPassword.getText().toString().trim();
        newPassword=inputNewPassword.getText().toString().trim();
        confirmPassword=inputConfirmPassword.getText().toString().trim();

     if(oldPassword.length()<=0)
     {
         Toast.makeText(getApplicationContext(), "Old password is empty", Toast.LENGTH_SHORT).show();
         return false;

     }
        if(newPassword.length()<=0)
        {
            Toast.makeText(getApplicationContext(), "New password is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(confirmPassword.length()<=0)
        {
            Toast.makeText(getApplicationContext(), "Confirm password is empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!newPassword.equals(confirmPassword))
        {
            Toast.makeText(getApplicationContext(), "Password doesn't match", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }

    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null) setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        title= (TextView) toolbar.findViewById(R.id.tooltitle);
        title.setText("Reset Password");
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void ChangePassword() {
        String tag_string_req = "ChangePassword";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.RESET_PASSWORD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("ChangePassword", "ChangePassword Response: " + response.toString());
                	hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("status");
                    // Check for error node in json
                    if (status) {
                        showAlertMessage("Your password reset done  successfully");
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(vp.mom.activitys.ResetPAssword.this,
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(vp.mom.activitys.ResetPAssword.this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            //    Log.e("ChangePassword", "ChangePassword Error: " + error.getMessage());
                Toast.makeText(vp.mom.activitys.ResetPAssword.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                	hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
               params.put("uid",session.getStringSessionData("uid"));
                params.put("oldpwd",oldPassword);
                params.put("newpwd",newPassword);
                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private  void showAlertMessage(String alertMsg)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Alert !");
        builder.setCancelable(false);
        builder.setIcon(R.drawable.app_logo);
        builder.setMessage(alertMsg);
        //  builder.setPositiveButton("OK", null);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                finish();
            }
        });

        //  builder.setNegativeButton("Cancel", null);
        builder.show();
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
