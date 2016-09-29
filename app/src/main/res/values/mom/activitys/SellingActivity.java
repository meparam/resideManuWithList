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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
import org.json.JSONArray;
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

import vp.mom.R;
import vp.mom.app.AppController;
import vp.mom.fragments.SettingFragment;
import vp.mom.gcm.gcm.NotificationUtils;
import vp.mom.imageupload.AndroidMultiPartEntity;

public class SellingActivity extends AppCompatActivity {

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 0x4;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String msg,chatRoomId,userName;
    private NotificationUtils notificationUtils;
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo1.jpg";

    public static final int REQUEST_CODE_GALLERY      = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static final int REQUEST_CODE_CROP_IMAGE   = 0x3;
    private File mFileTemp;
   // private CoordinatorLayout coordinatorLayout;
    String  imagePath;
    GetLocationAsync  getlocation;
    private static final String TAG = vp.mom.activitys.SellingActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    String[] allcategory_array, allcategory_id_array, allsubcategory_array, all_sub_category_id_array;
    String[]all_sizes_array,all_sizes_id_arry;
    String[] color_array={ "Black","Blue","Brown","Green","Multi colour","Orange","Beige","pink" ,
            " Purple","Red","Silver","White" ,"Gold","Non Specific"};
    String [] dilevery_type={"Via courier","Meet in Person","Both"};
    Button btnCategory, btnSubCategory,btnSize,btnColor,btnDeliveryType;
    String categoryId,subcategoryId,itemType,itemSize="",itemColor;
    EditText inputProductName,inputBrand,inputQuatanty,inputDiscription,inputLocation,inputPrice,
        inputDeliveryCost;
    LinearLayout shippingCost;
    String ProductName,Brand="",Quatanty,Discription,strLocation,Price,
            DeliveryType,DeliveryCost;
   private static final int GPS_REQ_CODE =2512;
   // String selectedPath;
    long totalSize = 0;
    vp.mom.api.SessionManager mSession;
    boolean deliveryPriceFlag=false;
    HashMap imageFileArray ;
    LocationManager locationManager ;
    Location location;
     int imagePosition=0;
    Double lat,lng;
    private Geocoder geocoder;
    private List<Address> addresses;
     AlertDialog dialog;
   ImageView  img_gallery1,img_gallery2,img_gallery3,img_gallery4;
    Uri fileUri,destination;
    public  static  final int REQUEST_CAMERA=0x1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_view_test);

        captureImageInitialization();
        imageFileArray = new HashMap();

        pDialog = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        pDialog.setCancelable(false);
        mSession=new vp.mom.api.SessionManager(vp.mom.activitys.SellingActivity.this);

        init();
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null) setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ImageView seeling_acccept = (ImageView) toolbar.findViewById(R.id.seeling_acccept);

        seeling_acccept.setClickable(true);
        ImageView seeling_cancel = (ImageView) toolbar.findViewById(R.id.seeling_cancel);

        seeling_cancel.setClickable(true);

        seeling_acccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Toast.makeText(SellingActivity.this, "seeling_acccept", Toast.LENGTH_SHORT).show();

                if (getValidation()) {

                    new UploadFileToServer().execute();
//
//                registerProduct(ProductName,Company,Brand,Discription,categoryId,mSession.getStringSessionData("uid"),Price ,DiscoutPrice,DisCountPercentage,Quatanty,"1987"
//                        ,DeliveryCost,DeliveryType,lat,lng,Location);
                }
//                else if(getValidation() && lat == null && lng == null)
//                {
// showMessage("Please switch on your GPS ");
//                }

            }
        });

        seeling_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialog_area = new AlertDialog.Builder(
                        vp.mom.activitys.SellingActivity.this);

                // Set the dialog title

                dialog_area.setTitle("Select Category ! ")

                        .setSingleChoiceItems(allcategory_array, 0,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int item) {
                                        btnCategory.setText(allcategory_array[item]);
                                        categoryId=allcategory_id_array[item].toString().trim();

//                                        Log.e("value after select", " : "
//                                                + categoryId);

                                        getAllSUbCategory(categoryId);
                                        dialog.dismiss();

                                    }
                                });

                dialog_area.show();
            }
        });

        btnSubCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder dialog_area = new AlertDialog.Builder(
                        vp.mom.activitys.SellingActivity.this);

                // Set the dialog title

                dialog_area.setTitle("Select Sub Category ! ")

                        .setSingleChoiceItems(allsubcategory_array, 0,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int item) {

//                                        Log.e("value after select", " : "
//                                                + all_sub_category_id_array[item]);
                                        btnSubCategory.setText(allsubcategory_array[item]);


                                        subcategoryId = all_sub_category_id_array[item].toString().trim();
                                        getAllSizes(subcategoryId);
                                        dialog.dismiss();

                                    }
                                });

                dialog_area.show();
            }
        });

        btnSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (all_sizes_array != null && all_sizes_array.length > 0) {
                    final AlertDialog.Builder dialog_area = new AlertDialog.Builder(
                            vp.mom.activitys.SellingActivity.this);
                    dialog_area.setTitle("Select Size ! ")

                            .setSingleChoiceItems(all_sizes_array, 0,
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int item) {
                                            btnSize.setText(all_sizes_array[item]);

                                            itemSize = all_sizes_array[item].toString().trim();


                                            dialog.dismiss();

                                        }
                                    });

                    dialog_area.show();
                }
                else
                    Toast.makeText(vp.mom.activitys.SellingActivity.this, "Size is not available", Toast.LENGTH_SHORT).show();
            }


        });

        btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder dialog_area = new AlertDialog.Builder(
                        vp.mom.activitys.SellingActivity.this);
                dialog_area.setTitle("Select Colour ! ")

                        .setSingleChoiceItems(color_array, 0,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int item) {
                                        btnColor.setText(color_array[item]);

                                        itemColor=color_array[item].toString().trim();

                                        dialog.dismiss();

                                    }
                                });

                dialog_area.show();
            }
        });
        btnDeliveryType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder dialog_area = new AlertDialog.Builder(
                        vp.mom.activitys.SellingActivity.this);
                dialog_area.setTitle("Select delivery type ! ")
                        .setSingleChoiceItems(dilevery_type, 0,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int item) {
                                        btnDeliveryType.setText(dilevery_type[item]);

                                        DeliveryType = dilevery_type[item].toString().trim();

                                   if(item==1)
                                        {
                                            deliveryPriceFlag=true;
                                            shippingCost.setVisibility(View.GONE);
                                        }

                                        else
                                        {
                                            deliveryPriceFlag=false;
                                            shippingCost.setVisibility(View.VISIBLE);
                                        }

                                            dialog.dismiss();

                                    }
                                });

                dialog_area.show();
            }
        });


            try
            {
                if (!isDeviceSupportCamera()) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry! Your device doesn't support camera",
                            Toast.LENGTH_LONG).show();
                    // will close the app if the device does't have camera
                    finish();
                }
            }

            catch (Exception ex){}


        img_gallery1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePosition=0;

                    if (Build.VERSION.SDK_INT >= 23) {
                        if (checkAndRequestPermissions())
                            dialog.show();
                    }

                else
                dialog.show();

            }
        });

        img_gallery2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePosition=1;
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkAndRequestPermissions())
                        dialog.show();
                }

                else
                    dialog.show();
            }
        });
        img_gallery3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePosition=2;
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkAndRequestPermissions())
                        dialog.show();
                }

                else
                    dialog.show();
            }
        });
        img_gallery4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePosition = 3;
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkAndRequestPermissions())
                        dialog.show();
                }

                else
                    dialog.show();
            }
        });


        getAllCategory();


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(vp.mom.app.Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    handlePushNotification(intent);
                }
            }
        };

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkAndRequestPermissions())
                getLocation();
        }
        else
            getLocation();



    }
    private  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);

        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int memorycardPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
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
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                            perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        getLocation();
                      //  Log.d(TAG, "sms & location services permission granted");
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d(TAG, "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            showDialogOK("Camera,Galeery and  Location Services Permission required for this app",
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
//                            Toast.makeText(this, "Go to settings and enable permissions", 1000)
//                                    .show();
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


    private void handlePushNotification(Intent intent) {
        int type = intent.getIntExtra("type", -1);

        // if the push is of chat room message
        // simply update the UI unread messages count
        if (type == vp.mom.app.Config.PUSH_TYPE_CHATROOM) {
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
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
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

                if (item == 0) {
                    // takePicture();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUri = getOutputMediaFileUri();
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(intent,REQUEST_CAMERA);
                } else {

                    Crop.pickImage(vp.mom.activitys.SellingActivity.this);
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
                "MOMCamParam");

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {



        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
        else if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            //  Uri u = result.getData();
            beginCrop(fileUri);

        }
        else if (requestCode == GPS_REQ_CODE && resultCode == RESULT_OK) {
            if (Build.VERSION.SDK_INT >= 23) {
                     if (checkAndRequestPermissions())
                getLocation();
        }

        else
            getLocation();

        }
    }

    private void beginCrop(Uri source) {
        destination= getOutputMediaFileUri();
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {

            //profilePic.setImageURI(Crop.getOutput(result));

            imagePath=destination.toString();



            if (imagePosition == 0) {
                imageFileArray.put(imagePosition, imagePath);

                img_gallery1.setImageURI(Crop.getOutput(result));
            } else if (imagePosition == 1) {
                imageFileArray.put(imagePosition, imagePath);

                img_gallery2.setImageURI(Crop.getOutput(result));
            } else if (imagePosition == 2) {
                imageFileArray.put(imagePosition, imagePath);

                img_gallery3.setImageURI(Crop.getOutput(result));
            } else {
                imageFileArray.put(imagePosition, imagePath);

                img_gallery4.setImageURI(Crop.getOutput(result));
            }
            //   Log.e("handleCrop","handleCrop"+Crop.getOutput(result));

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    private void getLocation() {
            locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            location  = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            location  = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

           if(location!=null)
           {
               lat=location.getLatitude();
               lng=location.getLongitude();

               getlocation = new GetLocationAsync(location.getLatitude(), location.getLongitude());
               getlocation.execute();

           }
            else

            {
                showSettingsAlert();
            }


    }
    public void showSettingsAlert() {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                vp.mom.activitys.SellingActivity.this);
        alertDialog.setTitle("Alert !");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), GPS_REQ_CODE);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();



    }


    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location1) {
            location=location1;
        }

        public void onProviderDisabled(String provider) {
            Toast.makeText( getApplicationContext(), "GPS is Disabled.Please enable GPS", Toast.LENGTH_SHORT ).show();

            //     updateWithNewLocation(null);
        }

        public void onProviderEnabled(String provider) {
            Toast.makeText( getApplicationContext(), "GPS is Enabled", Toast.LENGTH_SHORT).show();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(vp.mom.app.Config.PUSH_NOTIFICATION));

//        if (Build.VERSION.SDK_INT >= 23) {
//            if (checkAndRequestPermissions())
//                getLocation();
//        }
//
//        else
//            getLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);


        try
        {
            locationManager.removeUpdates(locationListener);
        }
        catch (Exception ex){}

    }

    /**
     * Checking device has camera hardware or not
     * */
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }


    private boolean getValidation() {


        if(imageFileArray.size()<=0)
        {
            Toast.makeText(getApplicationContext(), "Please select at least one image", Toast.LENGTH_SHORT).show();
            return false;

        }

        ProductName=  inputProductName.getText().toString().trim();
        if(ProductName.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please enter product name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!(categoryId != null && categoryId.length() > 0))
        {
            Toast.makeText(getApplicationContext(), "Please select category", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!(subcategoryId != null && subcategoryId.length() > 0))
        {
            Toast.makeText(getApplicationContext(), "Please select sub-category", Toast.LENGTH_SHORT).show();
            return false;

        }
//        if(!(itemSize != null && itemSize.length() > 0))
//        {
//            showMessage("Please select item size");
//            return false;
//        }


        Brand=  inputBrand.getText().toString().trim();
//        if(Brand.isEmpty())
//        {
//            showMessage("Please enter brand name");
//
//            return false;
//        }
//        Company=  inputCompany.getText().toString().trim();
//        if(Company.isEmpty())
//        {
//            showMessage("Please enter company name");
//            return false;
//        }
        Quatanty=  inputQuatanty.getText().toString().trim();
        if(Quatanty.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please enter quatanty", Toast.LENGTH_SHORT).show();
            return false;
        }

        Discription=  inputDiscription.getText().toString().trim();
        if(Discription.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please enter discription", Toast.LENGTH_SHORT).show();
            return false;
        }

        strLocation=  inputLocation.getText().toString().trim();
        if(strLocation.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please enter location", Toast.LENGTH_SHORT).show();
            return false;
        }
        Price=  inputPrice.getText().toString().trim();
        if(Price.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter price", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!( itemType != null &&  itemType.length() > 0))
        {
            Toast.makeText(getApplicationContext(), "Please enter item type", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!( itemColor != null &&  itemColor.length() > 0))
        {
            Toast.makeText(getApplicationContext(), "Please enter item color", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!( DeliveryType != null &&  DeliveryType.length() > 0))
        {
            Toast.makeText(getApplicationContext(), "Please select delivery type", Toast.LENGTH_SHORT).show();
            return false;
        }


//        DeliveryType=  btnDeliveryType.getText().toString().trim();
//        if(DeliveryType.isEmpty())
//        {
//            showMessage("Please enter ");
//            return false;
//        }
        DeliveryCost=  inputDeliveryCost.getText().toString().trim();
        if(DeliveryCost.isEmpty())
        {
            if(deliveryPriceFlag)
            {
                DeliveryCost="0.0";
            }
           else
            {
                Toast.makeText(getApplicationContext(), "Please enter delivery cost", Toast.LENGTH_SHORT).show();
                return false;
            }


        }

        return  true;
    }

    public void init()

    {
//        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
//                .coordinatorLayoutnew);
        btnCategory= (Button) findViewById(R.id.btnCategory);
        btnSubCategory= (Button) findViewById(R.id.btnSubCategory);
        btnSize= (Button) findViewById(R.id.btnSize);
        btnColor= (Button) findViewById(R.id.btnColor);
        inputProductName= (EditText) findViewById(R.id.edttxtProductName);
        inputBrand= (EditText) findViewById(R.id.edttxtproductBrand);
      //  inputCompany= (EditText) findViewById(R.id.edttxtProductCompany);
        inputQuatanty= (EditText) findViewById(R.id.edttxtProductQuantity);
        inputDiscription= (EditText) findViewById(R.id.edttxtProductDescription);
        inputLocation= (EditText) findViewById(R.id.edttxtLocation);
        inputPrice= (EditText) findViewById(R.id.edttxtProdustPrice);
    //    inputDiscoutPrice= (EditText) findViewById(R.id.edttxtProdustDiscountPrice);
      //  inputDisCountPercentage= (EditText) findViewById(R.id.edttxtProdustDiscountPercentage);
        btnDeliveryType= (Button) findViewById(R.id.edttxtChooseDeliveryType);
        inputDeliveryCost= (EditText) findViewById(R.id.edttxtChooseDeliveryCost);

        ((RadioGroup)findViewById(R.id.fancy_radio_group)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton rb = (RadioButton) findViewById(checkedId);
                itemType=rb.getText().toString();

                if(itemType.equalsIgnoreCase("new"))
                    itemType="0";
                else
                    itemType="1";
            }
        });
        shippingCost=(LinearLayout) findViewById(R.id.shippingCost);

        img_gallery1= (ImageView) findViewById(R.id.img_gallery1);
        img_gallery2= (ImageView) findViewById(R.id.img_gallery2);
        img_gallery3= (ImageView) findViewById(R.id.img_gallery3);
        img_gallery4= (ImageView) findViewById(R.id.img_gallery4);
    }
    //getall category
    private void getAllCategory() {

        String tag_string_req = "getAllCharity";

        pDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                vp.mom.api.AppConfig.GET_ALL_CATEGORY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
            //    Log.e(TAG, "cHARITY Response: " + response);
                hideDialog();

                ArrayList<String> stringArrayListID = new ArrayList<String>();
                ArrayList<String> stringArrayListNAME = new ArrayList<String>();
                JSONObject jObj;
                try {
                    jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("status");

                    boolean paypalStatus = jObj.getBoolean("paypal_flag");

                    if(paypalStatus) {

                        if (status) {

                            JSONArray jsonArray = jObj.getJSONArray("items");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                stringArrayListID.add(jsonObject.getString("id"));
                                stringArrayListNAME.add(jsonObject.getString("category"));

                            }

                            allcategory_array = stringArrayListNAME.toArray(new String[stringArrayListNAME.size()]);
                            allcategory_id_array = stringArrayListID.toArray(new String[stringArrayListID.size()]);
                        } else {

                            // Error occurred in registration. Get the error
                            // message
                            String errorMsg = jObj.getString("status");
//                        Toast.makeText(getApplicationContext(),
//                                errorMsg, Toast.LENGTH_LONG).show();
                        }
                    }

                    else
                      showAlert();

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
                params.put("uid",mSession.getStringSessionData("uid"));
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(vp.mom.activitys.SellingActivity.this);
        builder.setMessage("Please add paypal Account before Selling item").setTitle("Alert !").setIcon(R.drawable.dialog_logo)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                        Intent setting=new Intent(vp.mom.activitys.SellingActivity.this, SettingFragment.class);
                        startActivity(setting);
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //getall SUB category
    private void getAllSUbCategory(final String mId) {
        String tag_string_req = "getAllSUbCategory";

        pDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                vp.mom.api.AppConfig.GET_ALL_SUB_CATEGORY, new Response.Listener<String>() {

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
                            stringArrayListNAME.add(jsonObject.getString("sub_category"));

                        }

                        allsubcategory_array=stringArrayListNAME.toArray(new String[stringArrayListNAME.size()]);
                        all_sub_category_id_array=stringArrayListID.toArray(new String[stringArrayListID.size()]);
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("status");
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
              //  Log.e(TAG, "getAllCharity Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();

                params.put("cid",mId);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    //getall get All Sizes
    private void getAllSizes(final String sId) {

        String tag_string_req = "getAllSizes";

        pDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                vp.mom.api.AppConfig.GET_ALL_SIZES, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
            //    Log.e(TAG, "cHARITY Response: " + response);
                hideDialog();

                ArrayList<String> stringArrayListSizeID = new ArrayList<String>();
                ArrayList<String> stringArrayListSizeNAME = new ArrayList<String>();
                JSONObject jObj;
                try {
                    jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("status");
                    if (status) {

                        JSONArray jsonArray = jObj.getJSONArray("items");
                        for (int i=0;i<jsonArray.length();i++)
                        {

                            JSONObject  jsonObject = jsonArray.getJSONObject(i);

                            stringArrayListSizeID.add(jsonObject.getString("id"));
                            stringArrayListSizeNAME.add(jsonObject.getString("size"));

                        }
                        all_sizes_array=stringArrayListSizeNAME.toArray(new String[stringArrayListSizeNAME.size()]);

                        all_sizes_id_arry=stringArrayListSizeID.toArray(new String[stringArrayListSizeID.size()]);
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("status");
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


                params.put("scid",sId);
                return params;
            }

        };
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

    /**
     * Uploading the file to server
     * */
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
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
            HttpPost httppost = new HttpPost(vp.mom.api.AppConfig.PRODUCT_REGISTRATION_NEW);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });


                ArrayList<String> imagearraylist=new ArrayList<>();

                imagearraylist.addAll(imageFileArray.values());
                for (int i=0;i<imagearraylist.size();i++)
                {
                    InputStream is = new URL( imagearraylist.get(i).toString() ).openStream();
                    Bitmap bitmap = BitmapFactory.decodeStream( is );


                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bos);
                    byte[] data = bos.toByteArray();


                    entity.addPart("files["+i+"]", new ByteArrayBody(data,
                            "image/jpeg", i+""+System.currentTimeMillis()));
                    bitmap.recycle();
                }

//                if(oneflag)
//                {
//                    File sourceFile = new File(imagearraylist.get(1));
//                    entity.addPart("files[]", new FileBody(sourceFile));
//                }
//                if(oneflag)
//                {
//                    File sourceFile = new File(imagearraylist.get(1));
//                    entity.addPart("files[]", new FileBody(sourceFile));
//                }
                // Extra parameters if you want to pass to server
                entity.addPart("name", new StringBody(ProductName));
                entity.addPart("company", new StringBody(""));
                entity.addPart("brand",new StringBody( Brand));
                entity.addPart("description",new StringBody( Discription));
                entity.addPart("catergory",new StringBody(subcategoryId));
                entity.addPart("user_id",new StringBody(mSession.getStringSessionData("uid")));
                entity.addPart("base_price",new StringBody( Price));
                //entity.addPart("discount",new StringBody( DiscoutPrice));
              //  entity.addPart("discount_percentage",new StringBody( DisCountPercentage));
                entity.addPart("total_quantity",new StringBody( Quatanty));
                entity.addPart("available_quantity",new StringBody(Quatanty));
                entity.addPart("delivery_cost",new StringBody( DeliveryCost));
                entity.addPart("delivery_type",new StringBody( DeliveryType));
                entity.addPart("latitude",new StringBody( ""+lat));
                entity.addPart("longitude",new StringBody( ""+lng));
                entity.addPart("location",new StringBody(strLocation));

                entity.addPart("size",new StringBody(itemSize));
                entity.addPart("is_used",new StringBody(itemType));
                entity.addPart("colour",new StringBody(itemColor));

                entity.addPart("AF",new StringBody("1"));
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
            Log.e(TAG, "Response from server: " + result);
            hideDialog();
            // showing the server response in an alert dialog
            showAlert(result);
            Log.d("Response", "Response from server: " + result);
            super.onPostExecute(result);
        }
        boolean flag=false;
        private void showAlert(String message) {
            String msg = "Something went wrong please try again";
                    try{
                        JSONObject jsondta=new JSONObject(message);
                        if(jsondta.getBoolean("status"))
                        {
                            msg="Your product successfully uploaded";
                            flag=true;
                        }

                    } catch (Exception e){

                        Log.d("Exception", "Exception" + e.toString());
                    }

            final AlertDialog.Builder builder = new AlertDialog.Builder(vp.mom.activitys.SellingActivity.this);
            builder.setMessage(msg).setTitle("Alert !").setIcon(R.drawable.dialog_logo)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            if(flag)
                                finish();
                            else
                                dialog.dismiss();


                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
    public Bitmap decodeFile(String filePath) {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);
        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;
        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, o2);
        return bitmap;
    }
    private class GetLocationAsync extends AsyncTask<String, Void, String> {

        // boolean duplicateResponse;
        double x, y;
        //     StringBuilder str;

        public GetLocationAsync(double latitude, double longitude) {
            // TODO Auto-generated constructor stub

            x = latitude;
            y = longitude;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            Address returnAddress = null;
            try {
                geocoder = new Geocoder(vp.mom.activitys.SellingActivity.this, Locale.ENGLISH);
                addresses = geocoder.getFromLocation(x, y, 1);


            }catch(IOException e){
                Log.e("tag", e.getMessage());
            }


            return null;

        }

        @Override
        protected void onPostExecute(String result) {

                try {

                    if (addresses != null) {


                        Log.e("onPostExecute", "onPostExecute" + addresses.get(0).getAddressLine(0)
                                + addresses.get(0).getAddressLine(1) + " ");

                        inputLocation.setText(addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getLocality()+", "
                                + addresses.get(0).getCountryName() + " ");

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    Log.e("onPostExecute", "onPostExecute" + e.toString());
                }

        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }


}
