package vp.mom.activitys;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import vp.mom.R;
import vp.mom.api.AppConfig;
import vp.mom.api.CircleTransformation;
import vp.mom.api.SessionManager;
import vp.mom.app.AppController;
import vp.mom.app.Config;
import vp.mom.gcm.gcm.NotificationUtils;

public class UserProfileUpdate extends AppCompatActivity {
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 0x4;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String msg,chatRoomId,userName;
    private NotificationUtils notificationUtils;
    private static final String TAG = vp.mom.activitys.UserProfileUpdate.class.getSimpleName();
    TextView title;
    String fname,lname,email,username,mobNo="",address="",city="",state="",zipcode="",country="",userId;
    EditText  inputfname,inputlname,inputemail,inputusername,inputmobNo,inputaddress,inputcity,
            inputzipcode;
    Button updateButton,inputstate,inputcountry;
    String countryId,cityId;
    ImageView profilePic;
    String imagePath;
    public static final String TEMP_PHOTO_FILE_NAME = "update_photo.jpg";
    private File mFileTemp;
  //  public static final int REQUEST_CODE_GALLERY      = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static final int REQUEST_CODE_CROP_IMAGE   = 0x3;
    AlertDialog dialog;
    private SessionManager session;
    private ProgressDialog pDialog;
    long totalSize = 0;
    String[] all_country_array, all_country_id_array,all_county,all_county_id;
    Uri fileUri;
    public  static  final int REQUEST_CAMERA=0x1;
    Uri destination;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_update);
        pDialog = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        pDialog.setCancelable(false);
        session = new SessionManager(vp.mom.activitys.UserProfileUpdate.this);
        initActionBar();
        captureImageInitialization();
        init();

      getData();

        inputcountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder dialog_area = new AlertDialog.Builder(
                        vp.mom.activitys.UserProfileUpdate.this);

                // Set the dialog title

                dialog_area.setTitle("Select country ! ")

                        .setSingleChoiceItems(all_country_array, 0,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int item) {
                                        inputcountry.setText(all_country_array[item]);
                                        countryId = all_country_id_array[item].toString().trim();
                                        cityId=null;
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
                        vp.mom.activitys.UserProfileUpdate.this);

                // Set the dialog title

                dialog_area.setTitle("Select county ! ")

                        .setSingleChoiceItems(all_county, 0,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int item) {
                                        inputstate.setText(all_county[item]);
                                        cityId=all_county_id[item].toString().trim();



                                        dialog.dismiss();

                                    }
                                });

                dialog_area.show();
            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
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

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getValidation()) {
                    CallUpdateProfile();
                }
            }
        });

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    handlePushNotification(intent);
                }
            }
        };
        getAllContry();
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
//    private void pickImageFromSource(Sources source) {
//        RxImagePicker.with(this).requestImage(source)
//                .flatMap(uri -> {
//                    return    Observable.just(uri);
//
//                })
//                .subscribe(result -> {
//                  //  Toast.makeText(UserProfileUpdate.this, String.format("Result: %s", result), Toast.LENGTH_LONG).show();
////                    if (result instanceof Bitmap) {
////                        ivPickedImage.setImageBitmap((Bitmap) result);
////                    } else {
////
//             //       Log.e("estimate",""+result.toString());
//
//                    imagePath=result.getPath();
//
//                    Glide.with(UserProfileUpdate.this)
//                            .load(result) // works for File or Uri
//                            .crossFade()
//                            .into(profilePic);
//                }, throwable -> {
//                    //  Toast.makeText(FacebookList.this, String.format("Error: %s", throwable), Toast.LENGTH_LONG).show();
//                });
//    }

    public static Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }
    private static File getOutputMediaFile() {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MOMParam");

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

        Log.e("mediaFile","mediaFile"+mediaFile);
        return mediaFile;
    }

    private void handlePushNotification(Intent intent) {
        int type = intent.getIntExtra("type", -1);

        if (type == Config.PUSH_TYPE_CHATROOM) {
            msg = (String)intent.getSerializableExtra("message");

            chatRoomId  = intent.getStringExtra("UserToChat");

            userName=intent.getStringExtra("userName");

        }
        long when = System.currentTimeMillis();
        // app is in background. show the message in notification try
        Intent resultIntent = new Intent(getApplicationContext(), vp.mom.gcm.gcm.ChatRoomActivity.class);
        resultIntent.putExtra("UserToChat", chatRoomId);
        resultIntent.putExtra("name", userName);
        showNotificationMessage(getApplicationContext(), userName, msg,
                "" + when, resultIntent);


    }

    private void getAllContry() {

        String tag_string_req = "getAllCharity";

        pDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.GET_ALL_COUNTRY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
             //   Log.e(TAG, "cHARITY Response: " + response);
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

                        all_country_array=stringArrayListNAME.toArray(new String[stringArrayListNAME.size()]);
                        all_country_id_array=stringArrayListID.toArray(new String[stringArrayListID.size()]);
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = "Something went wrong";
//                        Toast.makeText(getApplicationContext(),
//                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
             //   Log.e(TAG, "getAllCharity Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void get_All_County(final String countryId) {

        String tag_string_req = "get_All_County";

        pDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.GET_ALL_CITY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
               // Log.e(TAG, "cHARITY Response: " + response);
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

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = "Something went wrong";
//                        Toast.makeText(getApplicationContext(),
//                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
             //   Log.e(TAG, "getAllCharity Error: " + error.getMessage());
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

    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }
    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
    private boolean getValidation() {
        fname = inputfname.getText().toString().trim();

        if(fname.isEmpty())
        {
            showMessage("Please enter first name");
            return  false;
        }
        lname = inputlname.getText().toString().trim();
        if(lname.isEmpty())
        {
            showMessage("Please enter last name");
            return  false;
        }
        email = inputemail.getText().toString().trim();
        if(email.isEmpty())
        {
            showMessage("Please enter email id");
            return  false;
        }

        if(!isValidEmail(email))
        {
            showMessage("Please enter valid email id");
            return false;
        }

        username = inputusername.getText().toString().trim();
        if(username.isEmpty())
        {
            showMessage("Please enter user name");
            return  false;
        }
        mobNo = inputmobNo.getText().toString().trim();
        address = inputaddress.getText().toString().trim();
        city = inputcity.getText().toString().trim();
        zipcode = inputzipcode.getText().toString().trim();
        country = inputcountry.getText().toString().trim();
        state = inputstate.getText().toString().trim();

        if(!(countryId != null && countryId.length() > 0))
        {
            Toast.makeText(getApplicationContext(), "Please select country", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!(cityId != null && cityId.length() > 0))
        {
            Toast.makeText(getApplicationContext(), "Please select county", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
   public void showMessage(String message)
    {
        Context context=getApplicationContext();
        LayoutInflater inflater=getLayoutInflater();

        View customToastroot =inflater.inflate(R.layout.mycustom_toast, null);
        TextView tv = (TextView) customToastroot.findViewById(R.id.customoast);
        tv.setText(message);
        Toast customtoast=new Toast(context);

        customtoast.setView(customToastroot);
        customtoast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,0, 0);
        customtoast.setDuration(Toast.LENGTH_LONG);
        customtoast.show();
       // Toast.makeText(UserProfileUpdate.this,message,Toast.LENGTH_SHORT).show();

    }

    private void init() {
        inputfname= (EditText) findViewById(R.id.edttxtFirstNameupdate);
        inputlname= (EditText) findViewById(R.id.edttxtLastnameupdate);
        inputemail= (EditText) findViewById(R.id.edttxtEmailupdate);
        inputemail.setKeyListener(null);
        inputusername= (EditText) findViewById(R.id.edttxtUserNameupdate);
        inputusername.setKeyListener(null);
        //inputpassword= (EditText) findViewById(R.id.edttxtPasswordupdate);
        inputmobNo= (EditText) findViewById(R.id.edttxtmobNoupdate);
        inputaddress= (EditText) findViewById(R.id.editTextAddressupdate);
        inputcity= (EditText) findViewById(R.id.edttxtCityupdate);
        inputstate= (Button) findViewById(R.id.edttxtstateupdate);
        inputzipcode= (EditText) findViewById(R.id.edttxtpincodeupdate);
        inputcountry= (Button) findViewById(R.id.edttxtCountryupdate);

        profilePic= (ImageView) findViewById(R.id.inputprofileimageupdte);

        updateButton= (Button) findViewById(R.id.btnSignUpupdate);
    }


    private String checkNull(final JSONObject json, final String key) {
        return json.isNull(key) ? "" : json.optString(key);
    }

    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null) setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        title= (TextView) toolbar.findViewById(R.id.tooltitle);
        title.setText("Profile update");
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    });

    }

    private void captureImageInitialization() {
        /**
         * a selector dialog to display two image source options, from camera
         * ‘Take from camera’ and from existing files ‘Select from gallery’
         */
        final String[] items = new String[] { "Take from camera",
                "Select from gallery" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Image");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) { // pick from
                // camera
//                if (item == 0) {
//                    takePicture();
//                } else {
//                    openGallery();
//                }


                if (item == 0) {
                    // takePicture();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUri = getOutputMediaFileUri();
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(intent,REQUEST_CAMERA);
                } else {

                    Crop.pickImage(vp.mom.activitys.UserProfileUpdate.this);
                }
            }
        });

        dialog = builder.create();

//        String state = Environment.getExternalStorageState();
//        if (Environment.MEDIA_MOUNTED.equals(state)) {
//            mFileTemp = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME);
//        }
//        else {
//            mFileTemp = new File(getFilesDir(), TEMP_PHOTO_FILE_NAME);
//        }

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

            profilePic.setImageURI(Crop.getOutput(result));

            imagePath=destination.toString();
         //   Log.e("handleCrop","handleCrop"+Crop.getOutput(result));

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getData() {
        String tag_string_req = "suggested_user";

        pDialog.setMessage("Loading ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.GET_INDIVIDUAL_MEMBER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              //  Log.e("GET_INDIVIDUAL_MEMBER", "GET_INDIVIDUAL_MEMBER Response: " + response.toString());
                hideDialog();

                if (response != null) {
                    JSONObject jsonresponse= null;
                    try {
                        jsonresponse = new JSONObject(response.toString());

                        parseJsonFeed(jsonresponse);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
               // Log.e("param", "Login Error: " + error.getMessage());

                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();

                params.put("uid",session.getStringSessionData("uid"));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void parseJsonFeed(JSONObject jsonresponse) {

      try {
          JSONObject jObj=    jsonresponse.getJSONObject("items");
          boolean status = jsonresponse.getBoolean("status");
          if (status) {

              session.setStringSessionData(AppConfig.USER_DATA, jsonresponse.toString());

             // Log.e("jsonresponse", " jsonresponse: " + jsonresponse);
              userId=jObj.getString("id");
              inputfname.setText(jObj.getString("first_name"));
              inputlname.setText(jObj.getString("last_name"));
              inputemail.setText(jObj.getString("email"));
              inputusername.setText(jObj.getString("username"));
              //  inputpassword.setText(jObj.getString("password"));

              inputmobNo.setText(checkNull(jObj,"mobile_number"));
              inputaddress.setText(checkNull(jObj,"address"));
              inputcity.setText(checkNull(jObj,"city"));
              inputstate.setText(checkNull(jObj,"state_name"));
              inputzipcode.setText(checkNull(jObj, "pincode"));

              inputcountry.setText(checkNull(jObj, "country_name"));

              if(jObj.isNull("country_name"))
                  inputcountry.setHint("Country");

              if(jObj.isNull("state_name"))
                  inputstate.setHint("County");


              countryId=checkNull(jObj,"cid");
              cityId=checkNull(jObj,"sid");
              Picasso.with(this)
                      .load(checkNull(jObj, "photo"))
                      .placeholder(R.drawable.img_circle_placeholder).transform(new CircleTransformation())
                      .into(profilePic);

              //session.setStringSessionData(AppConfig.USER_IMAGE,checkNull(jObj, "photo"));

          }

      }
      catch (Exception ex)
        {}




    }

    private  void showAlertMessage(String alertMsg, final boolean flag)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Alert !");
        builder.setCancelable(false);
        builder.setIcon(R.drawable.dialog_logo);
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
    public  void CallUpdateProfile() {
        showDialog();
        String url = AppConfig.PROFILE_UPDATE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id",session.getStringSessionData("uid"));
        params.put("first_name",fname);
        params.put("last_name",lname);
        params.put("username",username);
        params.put("mobile_number",mobNo);
        params.put("address",address);
        params.put("city",city);
        params.put("state",cityId);
        params.put("pincode",zipcode);
        params.put("country",countryId);

       // File sourceFile = null;
//        try {
//            sourceFile = new File(imagePath);
//
//        }
//        catch (Exception e)
//        {
//
//        }
        Log.v("params::", "" + params);
      //  Log.v("file::", "" + sourceFile);

        vp.mom.api.MultipartRequest mr = new vp.mom.api.MultipartRequest("userfile", url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                      //  Log.v("TAG", "Success " + response);
                        hideDialog();
                        // showing the server response in an alert dialog
                        //showAlert(result);
                        String msg;
                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean status = jObj.getBoolean("status");
                            //  msg=jObj.getString("status");
                            if (status) {
                                //  Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();
                             //   session.setStringSessionData("LoginResponse",response);
                                session.setStringSessionData(AppConfig.USER_DATA,response.toString());
                                showAlertMessage("Profile updated successfully",true);
                            } else {

                                // Error occurred in registration. Get the error
                                // message
                                String errorMsg = jObj.getString("message");
                                showAlertMessage(errorMsg,false);
//                    Toast.makeText(getApplicationContext(),
//                            errorMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof TimeoutError
                        || volleyError instanceof NoConnectionError) {

                    Toast.makeText(vp.mom.activitys.UserProfileUpdate.this,"Network error",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(vp.mom.activitys.UserProfileUpdate.this,"Something wents wrong!",Toast.LENGTH_SHORT).show();

                }
            }

        }, imagePath, params);
        AppController.getInstance().addToRequestQueue(mr, "CallUpdateProfile");
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
