package vp.mom;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
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
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import vp.mom.activitys.ForgetPasswordActivty;
import vp.mom.activitys.SuggestedUserActivity;
import vp.mom.app.AppController;
import vp.mom.imageupload.AndroidMultiPartEntity;


public class LoginActivity extends Activity {
    long totalSize = 0;

    String first_name,last_name,femail,fusername,fpassword,fbid,userfile;
    private static final String TAG = vp.mom.LoginActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    Button btnLogin,btnFackeBookLogin;
    TextView txtSignUp, txtForgetPassword;
    EditText editEmail,editpassword;
    private vp.mom.api.SessionManager session;
    String email,password;
    LoginButton facebookLogin;
    CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.login_xml);

        pDialog = new ProgressDialog(vp.mom.LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        pDialog.setCancelable(false);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        txtSignUp = (TextView) findViewById(R.id.txtSignUp);
        txtForgetPassword = (TextView) findViewById(R.id.txtforgotpassword);
        SpannableString content = new SpannableString("Sign Up");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        txtSignUp.setText(content);

        SpannableString content1 = new SpannableString("Forgot Password?");

        content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
        txtForgetPassword.setText(content1);

        editEmail= (EditText) findViewById(R.id.editEmail);
        editpassword= (EditText) findViewById(R.id.edtPassword);

        facebookLogin= (LoginButton) findViewById(R.id.login_button);
        btnFackeBookLogin= (Button) findViewById(R.id.btnFackeBookLogin);
        facebookLogin.setReadPermissions("email");
        btnFackeBookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facebookLogin.performClick();
            }
        });

        facebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                // Application code
                                try {
                                    Log.i("Response","Response "+response.toString());

                                    femail = response.getJSONObject().getString("email");
                                    first_name = response.getJSONObject().getString("first_name");
                                    last_name = response.getJSONObject().getString("last_name");
                                    fbid = response.getJSONObject().getString("id");
                                    fusername=first_name+" "+last_name;
                                    fpassword=fbid;
                                    if (Profile.getCurrentProfile()!=null)
                                    {
                                        userfile=""+Profile.getCurrentProfile().getProfilePictureUri(200, 200);
                                    }



                                   new SignUpServer().execute();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,email,first_name,last_name");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                // App code
                Log.e("onCancel","onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e("exception","exception"+exception);
            }
        });


        // Session manager
        session = new vp.mom.api.SessionManager(getApplicationContext());
        txtForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(vp.mom.LoginActivity.this, ForgetPasswordActivty.class);
                startActivity(i);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

            }
        });
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(vp.mom.LoginActivity.this, vp.mom.activitys.SignUpActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getValivation())

                {
                    checkLogin(email, password);
                }
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    private boolean getValivation() {

         email = editEmail.getText().toString().trim();
            if(email.isEmpty())
            {
                Toast.makeText(getApplicationContext(), "Please enter email id", Toast.LENGTH_SHORT).show();

                return false;
            }

         password = editpassword.getText().toString().trim();
        if(password.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_SHORT).show();

            return false;
        }

        return true;
    }
    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                vp.mom.api.AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
              //  Log.e(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("status");
                    // Check for error node in json
                    if (status) {
                //        session.setStringSessionData("LoginResponse",response);
                        session.setBooleanSessionData("isLoggedin",true);

                        session.setStringSessionData("uid",jObj.getJSONObject("items").getString("id"));

                        session.setStringSessionData(vp.mom.api.AppConfig.USER_DATA, response.toString());
                        Intent intent = new Intent(vp.mom.LoginActivity.this,
                                SuggestedUserActivity.class);
                        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
               // Log.e(TAG, "Login Error: " + error.getMessage());
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
                params.put("password", password);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    /**
     * Uploading the file to server
     */
    private class SignUpServer extends AsyncTask<Void, Integer, String> {



        @Override
        protected void onPreExecute() {

            pDialog.setMessage("Registering ...");
            showDialog();

            super.onPreExecute();
        }


        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(vp.mom.api.AppConfig.FACEBOOK_LOGIN);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });
                try
                {
                    InputStream is = new URL(userfile).openStream();
                    Bitmap bitmap = BitmapFactory.decodeStream( is );
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    byte[] data = bos.toByteArray();

                    entity.addPart("userfile", new ByteArrayBody(data,
                            "image/jpeg", ""+System.currentTimeMillis()));
                    bitmap.recycle();
                }
                catch (Exception ex){}

                // Extra parameters if you want to pass to server
                entity.addPart("first_name", new StringBody(first_name));
                entity.addPart("last_name", new StringBody(last_name));
                entity.addPart("email", new StringBody(femail));
                entity.addPart("username", new StringBody(fusername));
                entity.addPart("password", new StringBody(fpassword));
                entity.addPart("fbid", new StringBody(fbid));


                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            hideDialog();
            try {
                JSONObject jObj = new JSONObject(result);
                boolean status = jObj.getBoolean("status");
                // Check for error node in json
                if (status) {
                    //        session.setStringSessionData("LoginResponse",response);
                    session.setBooleanSessionData("isLoggedin",true);

                    session.setStringSessionData("uid",jObj.getJSONObject("items").getString("id"));

                    session.setStringSessionData(vp.mom.api.AppConfig.USER_DATA, result.toString());
                    Intent intent = new Intent(vp.mom.LoginActivity.this,
                            SuggestedUserActivity.class);
                    overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                    startActivity(intent);
                    finish();
                } else {
                    // Error in login. Get the error message
                    String errorMsg = jObj.getString("message");
                    Toast.makeText(getApplicationContext(),
                            errorMsg, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                // JSON error
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(result);
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



