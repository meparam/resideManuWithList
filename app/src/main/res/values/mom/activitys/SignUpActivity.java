package vp.mom.activitys;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import vp.mom.LoginActivity;
import vp.mom.R;
import vp.mom.api.AppConfig;
import vp.mom.imageupload.AndroidMultiPartEntity;

/**
 * Created by param on 18-11-2015.
 */
public class SignUpActivity extends AppCompatActivity {
//    compile 'com.mlsdev.rximagepicker:library:1.1.2'
//    compile 'io.reactivex:rxjava:1.0.14'
public  static  final int REQUEST_CAMERA=0x1;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 0x4;
    private static final String TAG = vp.mom.activitys.SignUpActivity.class.getSimpleName();
    Button btnSignUp;
    //  LinearLayout backll;
    EditText inputFirstName, inputLastName, inputEmail, inputUsername, inputPassword;
    //  Button buttonCharityDonate;
    //  String[] allcharityId,allcharityName;
    String fname, lname, email, username, password;
    private LayoutInflater inflater;
    private View view;
    private ProgressDialog pDialog;
    long totalSize = 0;
    ImageView inputprofileimage;
    AlertDialog dialog;
    Uri fileUri,destination;;
    String imagePath;
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    public static final int REQUEST_CODE_GALLERY = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static final int REQUEST_CODE_CROP_IMAGE = 0x3;
    private File mFileTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater = LayoutInflater.from(this);
        view = inflater.inflate(R.layout.signup_xml, null);

      //  AppController.applyFonts(view);
        setContentView(view);

        captureImageInitialization();
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        if (toolbar != null) setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        pDialog = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        pDialog.setCancelable(false);

       /* coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .signupcoordinatorLayout);*/

        //   buttonCharityDonate=(Button)findViewById(R.id.buttonCharityDonate);
        inputFirstName = (EditText) findViewById(R.id.edttxtFirstName);
        inputLastName = (EditText) findViewById(R.id.edttxtLastname);
        inputEmail = (EditText) findViewById(R.id.edttxtEmail);
        inputUsername = (EditText) findViewById(R.id.edttxtUserName);
        inputPassword = (EditText) findViewById(R.id.edttxtPassword);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        inputprofileimage = (ImageView) findViewById(R.id.inputprofileimage);

        inputprofileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkAndRequestPermissions())
                        dialog.show();
                }

                else
                    dialog.show();

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getValidtion())

                {
                    new SignUpServer().execute();
                }


            }
        });

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//        getSupportActionBar().setHomeAsUpIndicator(upArrow);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  getSupportActionBar().setHomeAsUpIndicator(upArrow);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(upArrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
    private  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);

       // int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int memorycardPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int ReadPermissionMemory = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (ReadPermissionMemory != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (memorycardPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
              //  perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            &&
                            perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            &&
                    perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                     //   Log.d(TAG, "sms & location services permission granted");
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d(TAG, "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||

                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE
                                )|| ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                                ) {
                            showDialogOK("Camera and  Gallery  access  Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }
    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }
    private boolean getValidtion() {
        fname = inputFirstName.getText().toString().trim();

        if (fname.isEmpty()) {
            // showMessage("Please enter first name");
            Toast.makeText(getApplicationContext(), "Please enter first name", Toast.LENGTH_SHORT).show();

            return false;
        }
        lname = inputLastName.getText().toString().trim();
        if (lname.isEmpty()) {
            // showMessage("Please enter last name");

            Toast.makeText(getApplicationContext(), "Please enter last name", Toast.LENGTH_SHORT).show();
            return false;
        }
        email = inputEmail.getText().toString().trim();
        if (email.isEmpty()) {
            // showMessage("Please enter email id");
            Toast.makeText(getApplicationContext(), "Please enter email id", Toast.LENGTH_SHORT).show();

            return false;
        }

        if (!isValidEmail(email)) {
            Toast.makeText(getApplicationContext(), "Please enter valid email id", Toast.LENGTH_SHORT).show();

            //showMessage("Please enter valid email id");
            return false;
        }


        username = inputUsername.getText().toString().trim();
        if (username.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter valid user name", Toast.LENGTH_SHORT).show();

            //showMessage("Please enter user name");
            return false;
        }
        password = inputPassword.getText().toString().trim();
        if (password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_SHORT).show();

            //showMessage("Please enter password");
            return false;
        }
//        charitytodonate = buttonCharityDonate.getText().toString().trim();
//        if(!(charitytodonate != null && charitytodonate.length() > 0))
//
//        {
//            showMessage("Please select charity");
//            return  false;
//        }

        if (!(imagePath != null && imagePath.length() > 0)) {
            Toast.makeText(getApplicationContext(), "Please select profile image", Toast.LENGTH_SHORT).show();

            /// showMessage("Please select profile image");
            return false;

        }

        return true;
    }

    /*  private void  showMessage(String msg){
          Snackbar snackbar = Snackbar
                  .make(coordinatorLayout, msg, Snackbar.LENGTH_LONG);
          View sbView = snackbar.getView();
          TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);

          textView.setTextColor(Color.YELLOW);

          snackbar.show();

      }
  */
    private void captureImageInitialization() {
        /**
         * a selector dialog to display two image source options, from camera
         * ‘Take from camera’ and from existing files ‘Select from gallery’
         */
        final String[] items = new String[]{"Take from camera",
                "Select from gallery"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select Image");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) { // pick from

                if (item == 0) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    fileUri = getOutputMediaFileUri();

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                    // start the image capture Intent
                    startActivityForResult(intent,REQUEST_CAMERA);

                } else {
                    Crop.pickImage(vp.mom.activitys.SignUpActivity.this);

                }
            }
        });

        dialog = builder.create();


    }
    public static Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }
    private static File getOutputMediaFile() {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MOM");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e("mediaStorageDir","mediaStorageDir");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

     //   android.util.Log.e("mediaFile","mediaFile"+mediaFile);
        return mediaFile;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {

        //   Log.e("onActivityResult","onActivityResult"+result.toString());

        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
        else if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            //  Uri u = result.getData();
            beginCrop(fileUri);

        }
    }
    private void beginCrop(Uri source) {
        destination= getOutputMediaFileUri();
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {

            inputprofileimage.setImageURI(Crop.getOutput(result));

            imagePath=destination.toString();
            //   Log.e("handleCrop","handleCrop"+Crop.getOutput(result));

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Uploading the file to server
     */
    private class SignUpServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {

            pDialog.setMessage("Registering ...");
            showDialog();
            // setting progress bar to zero
            //progressBar.setProgress(0);
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
            HttpPost httppost = new HttpPost(AppConfig.URL_SIGNUP);

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
                    InputStream is = new URL( imagePath ).openStream();
                    Bitmap bitmap = BitmapFactory.decodeStream( is );
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, bos);
                    byte[] data = bos.toByteArray();

                    entity.addPart("userfile", new ByteArrayBody(data,
                            "image/jpeg", ""+System.currentTimeMillis()));
                    bitmap.recycle();
                }
                catch (Exception ex){}

                // Extra parameters if you want to pass to server
                entity.addPart("first_name", new StringBody(fname));
                entity.addPart("last_name", new StringBody(lname));
                entity.addPart("email", new StringBody(email));
                entity.addPart("username", new StringBody(username));
                entity.addPart("password", new StringBody(password));

                // entity.addPart("charity_id",new StringBody(charitytodonate));


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
          //  Log.e(TAG, "Response from server: " + result);
            hideDialog();
            try {
                JSONObject jObj = new JSONObject(result);
                boolean status = jObj.getBoolean("status");
                if (status) {
                    showAlertMessage("Registration Successfull! Check your Email and click on the link to activate your account", true);

                } else {

                    // Error occurred in registration. Get the error
                    // message
                    String errorMsg = jObj.getString("message");
                    showAlertMessage(errorMsg, false);
//                    Toast.makeText(getApplicationContext(),
//                            errorMsg, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }

    private void showAlertMessage(String alertMsg, final boolean flag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Alert !");
        builder.setIcon(R.drawable.dialog_logo);
        builder.setCancelable(false);
        builder.setMessage(alertMsg);
        //  builder.setPositiveButton("OK", null);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (flag) {
                    Intent intent = new Intent(
                            vp.mom.activitys.SignUpActivity.this,
                            LoginActivity.class);

                    startActivity(intent);
                    finish();
                } else
                    dialog.dismiss();
            }
        });

        //  builder.setNegativeButton("Cancel", null);
        builder.show();
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
