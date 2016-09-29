package vp.mom.activitys;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;

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
import vp.mom.api.AppConfig;
import vp.mom.app.AppController;
import vp.mom.imageupload.AndroidMultiPartEntity;

/**
 * Created by pallavi.b on 18-Feb-16.
 */
public class SellingActivityEditProfile extends AppCompatActivity {
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 0x4;
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo1.jpg";
  //  LocationManager locationManager ;
   // public static final int REQUEST_CODE_GALLERY = 0x1;
  //  public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
  //  public static final int REQUEST_CODE_CROP_IMAGE = 0x3;
  //  private File mFileTemp;
    private CoordinatorLayout coordinatorLayout;
    String imagePath;
 //   GetLocationAsync getlocation;
    private static final String TAG = SellingActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    String[] allcategory_array, allcategory_id_array, allsubcategory_array, all_sub_category_id_array;
    String[] all_sizes_array, all_sizes_id_arry;
    String[] color_array = {"Black", "Blue", "Brown", "Green", "Multi colour", "Orange", "Beige", "pink",
            " Purple", "Red", "Silver", "White", "Gold", "Non Specific"};
    String[] dilevery_type = {"Via courier", "Meet in Person","Boths"};
    Button btnCategory, btnSubCategory, btnSize, btnColor, btnDeliveryType;
    String categoryId, subcategoryId, itemType, itemSize = "", itemColor;
    EditText inputProductName, inputBrand, inputQuatanty, inputDiscription, inputLocation, inputPrice,
            inputDeliveryCost;
    LinearLayout shippingCost;
    String ProductName, Brand = "", Quatanty, Discription, strLocation, Price,
            DeliveryType, DeliveryCost;
    private static final int GPS_REQ_CODE = 2512;
    // String selectedPath;
    long totalSize = 0;
    boolean deliveryPriceFlag = false;
    HashMap imageFileArray;
 //   LocationManager locationManager;
    //Location location;
    int imagePosition = 0;
    Double lat, lng;

   // private Geocoder geocoder;
   // private List<Address> addresses;
    AlertDialog dialog;
    ImageView img_gallery1, img_gallery2, img_gallery3, img_gallery4;
    RadioButton radioNew, radioUsed;

    //18feb

    boolean imageflag1 = false, imageflag2 = false, imageflag3 = false, imageflag4 = false,imageArrayFlag=false;
    String imageID1, imageID2, imageID3, imageID4;

   // String imageId = "";
    String orderid, productid;
    private vp.mom.api.SessionManager session;
    StringBuilder sb;

   // private GoogleApiClient client;
   Uri fileUri,destination;
    public  static  final int REQUEST_CAMERA=0x1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selling_edit_profile);
        session = new vp.mom.api.SessionManager(this);
        captureImageInitialization();
        imageFileArray = new HashMap();
        sb = new StringBuilder();
        pDialog = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        pDialog.setCancelable(false);
        // mSession=new SessionManager(SellingActivityEditProfile.this);

        init();

        Intent intent = getIntent();
        productid = intent.getStringExtra("product_id");
        //18feb
        getSellingEditProfile();
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

                    new EditFileToServer().execute();
//
//                registerProduct(ProductName,Company,Brand,Discription,categoryId,mSession.getStringSessionData("uid"),Price ,DiscoutPrice,DisCountPercentage,Quatanty,"1987"
//                        ,DeliveryCost,DeliveryType,lat,lng,Location);
                }
//                else if (getValidation() && lat == null && lng == null) {
//                    showMessage("Please switch on your GPS ");
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
                        vp.mom.activitys.SellingActivityEditProfile.this);

                // Set the dialog title

                dialog_area.setTitle("Select Cataegoty ! ")

                        .setSingleChoiceItems(allcategory_array, 0,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int item) {
                                        btnCategory.setText(allcategory_array[item]);
                                        categoryId = allcategory_id_array[item].toString().trim();

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
                        vp.mom.activitys.SellingActivityEditProfile.this);

                // Set the dialog title

                dialog_area.setTitle("Select Sub Cataegoty ! ")

                        .setSingleChoiceItems(allsubcategory_array, 0,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int item) {

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
                final AlertDialog.Builder dialog_area = new AlertDialog.Builder(
                        vp.mom.activitys.SellingActivityEditProfile.this);
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
        });

        btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder dialog_area = new AlertDialog.Builder(
                        vp.mom.activitys.SellingActivityEditProfile.this);
                dialog_area.setTitle("Select Colour ! ")

                        .setSingleChoiceItems(color_array, 0,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int item) {
                                        btnColor.setText(color_array[item]);

                                        itemColor = color_array[item].toString().trim();

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
                        vp.mom.activitys.SellingActivityEditProfile.this);
                dialog_area.setTitle("Select delivery type ! ")
                        .setSingleChoiceItems(dilevery_type, 0,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int item) {
                                        btnDeliveryType.setText(dilevery_type[item]);

                                        DeliveryType = dilevery_type[item].toString().trim();

                                        if (item == 1) {
                                            deliveryPriceFlag = true;
                                            shippingCost.setVisibility(View.GONE);
                                        } else {
                                            deliveryPriceFlag = true;
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
                imagePosition = 0;
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
                imagePosition = 1;
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
                imagePosition = 2;
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


    }
    private  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);

      //  int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int memorycardPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();

//        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
//        }

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
                        Log.d(TAG, "sms & location services permission granted");
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
//    private void pickImageFromSource(Sources source) {
//        RxImagePicker.with(this).requestImage(source)
//                .flatMap(uri -> {
//                    return    Observable.just(uri);
//
//                })
//                .subscribe(result -> {
//                  //  Toast.makeText(SellingActivityEditProfile.this, String.format("Result: %s", result), Toast.LENGTH_LONG).show();
////                    if (result instanceof Bitmap) {
////                        ivPickedImage.setImageBitmap((Bitmap) result);
////                    } else {
////
//                    Log.e("estimate",""+result.toString());
//
//                    imagePath=result.toString();
//
//                    if (imagePosition == 0) {
//                        imageFileArray.put(imagePosition, imagePath);
//                        Glide.with(SellingActivityEditProfile.this)
//                                .load(result) // works for File or Uri
//                                .crossFade()
//                                .into(img_gallery1);
//
//                        if (imageflag1) {
//                            sb.append(imageID1);
//                            sb.append(",");
//                        }
//                        imageArrayFlag=true;
//
//                    } else if (imagePosition == 1) {
//                        imageFileArray.put(imagePosition, imagePath);
//                        Glide.with(SellingActivityEditProfile.this)
//                                .load(result) // works for File or Uri
//                                .crossFade()
//                                .into(img_gallery2);
//                        if (imageflag2) {
//                            sb.append(imageID2);
//                            sb.append(",");
//                        }
//                        imageArrayFlag=true;
//                    } else if (imagePosition == 2) {
//                        imageFileArray.put(imagePosition, imagePath);
//                        Glide.with(SellingActivityEditProfile.this)
//                                .load(result) // works for File or Uri
//                                .crossFade()
//                                .into(img_gallery3);
//                        if (imageflag3) {
//                            sb.append(imageID3);
//                            sb.append(",");
//                        }
//                        imageArrayFlag=true;
//
//                    } else {
//                        imageFileArray.put(imagePosition, imagePath);
//                        Glide.with(SellingActivityEditProfile.this)
//                                .load(result) // works for File or Uri
//                                .crossFade()
//                                .into(img_gallery4);
//                        if (imageflag4) {
//                            sb.append(imageID4);
//                            sb.append(",");
//                        }
//                        imageArrayFlag=true;
//                    }
//
//
//                    if (imagePosition == 0) {
//                        imageFileArray.put(imagePosition, imagePath);
//                        Glide.with(SellingActivityEditProfile.this)
//                                .load(result) // works for File or Uri
//                                .crossFade()
//                                .into(img_gallery1);
//                    } else if (imagePosition == 1) {
//                        imageFileArray.put(imagePosition, imagePath);
//                        Glide.with(SellingActivityEditProfile.this)
//                                .load(result) // works for File or Uri
//                                .crossFade()
//                                .into(img_gallery2);
//                    } else if (imagePosition == 2) {
//                        imageFileArray.put(imagePosition, imagePath);
//                        Glide.with(SellingActivityEditProfile.this)
//                                .load(result) // works for File or Uri
//                                .crossFade()
//                                .into(img_gallery3);
//                    } else {
//                        imageFileArray.put(imagePosition, imagePath);
//                        Glide.with(SellingActivityEditProfile.this)
//                                .load(result) // works for File or Uri
//                                .crossFade()
//                                .into(img_gallery4);
//                    }
//                }, throwable -> {
//                    //  Toast.makeText(FacebookList.this, String.format("Error: %s", throwable), Toast.LENGTH_LONG).show();
//                });
//    }

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

    }

    private void beginCrop(Uri source) {
        destination= getOutputMediaFileUri();
        Crop.of(source, destination).asSquare().start(this);
    }
    public static Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }
    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {


            imagePath=destination.toString();

            if (imagePosition == 0) {
                imageFileArray.put(imagePosition, imagePath);
//                Glide.with(SellingActivityEditProfile.this)
//                        .load(result) // works for File or Uri
//                        .crossFade()
//                        .into(img_gallery1);
                img_gallery1.setImageURI(Crop.getOutput(result));
                if (imageflag1) {
                    sb.append(imageID1);
                    sb.append(",");
                }
                imageArrayFlag=true;

            } else if (imagePosition == 1) {
                imageFileArray.put(imagePosition, imagePath);
//                Glide.with(SellingActivityEditProfile.this)
//                        .load(result) // works for File or Uri
//                        .crossFade()
//                        .into(img_gallery2);
                img_gallery2.setImageURI(Crop.getOutput(result));
                if (imageflag2) {
                    sb.append(imageID2);
                    sb.append(",");
                }
                imageArrayFlag=true;
            } else if (imagePosition == 2) {
                imageFileArray.put(imagePosition, imagePath);
//                Glide.with(SellingActivityEditProfile.this)
//                        .load(result) // works for File or Uri
//                        .crossFade()
//                        .into(img_gallery3);
                img_gallery3.setImageURI(Crop.getOutput(result));
                if (imageflag3) {
                    sb.append(imageID3);
                    sb.append(",");
                }
                imageArrayFlag=true;

            } else {
                imageFileArray.put(imagePosition, imagePath);
//                Glide.with(SellingActivityEditProfile.this)
//                        .load(result) // works for File or Uri
//                        .crossFade()
//                        .into(img_gallery4);
                img_gallery4.setImageURI(Crop.getOutput(result));
                if (imageflag4) {
                    sb.append(imageID4);
                    sb.append(",");
                }
                imageArrayFlag=true;
            }



        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private File createTempFile() {
        return new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + "_image.jpeg");
    }
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
                // camera


//                long time = System.currentTimeMillis();

//                String state = Environment.getExternalStorageState();
//                if (Environment.MEDIA_MOUNTED.equals(state)) {
//                    mFileTemp = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME + time);
//                } else {
//                    mFileTemp = new File(getFilesDir(), TEMP_PHOTO_FILE_NAME + time);
//                }

//                if (item == 0) {
//                    takePicture();
//
//                } else {
//                    openGallery();
//
//                }

                if (item == 0) {
                    // takePicture();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUri = getOutputMediaFileUri();
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(intent,REQUEST_CAMERA);
                } else {

                    Crop.pickImage(vp.mom.activitys.SellingActivityEditProfile.this);
                }
            }
        });

        dialog = builder.create();
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



    @Override
    protected void onResume() {
        super.onResume();
     //   getLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
    //    locationManager.removeUpdates(locationListener);
    }

    /**
     * Checking device has camera hardware or not
     */
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


//        if (imageFileArray.size() <= 0) {
//            showMessage("Please select at least one image");
//            return false;
//
//        }

        ProductName = inputProductName.getText().toString().trim();
        if (ProductName.isEmpty()) {
            showMessage("Please enter product name");
            return false;
        }

//        if (!(categoryId != null && categoryId.length() > 0)) {
//            showMessage("Please select category");
//            return false;
//        }

        if (!(subcategoryId != null && subcategoryId.length() > 0)) {
            showMessage("Please select sub-category");
            return false;
        }
//        if(!(itemSize != null && itemSize.length() > 0))
//        {
//            showMessage("Please select item size");
//            return false;
//        }


        Brand = inputBrand.getText().toString().trim();

        Quatanty = inputQuatanty.getText().toString().trim();
        if (Quatanty.isEmpty()) {
            showMessage("Please enter quatanty");
            return false;
        }

        Discription = inputDiscription.getText().toString().trim();
        if (Discription.isEmpty()) {
            showMessage("Please enter discription");
            return false;
        }
        strLocation = inputLocation.getText().toString().trim();
        if (strLocation.isEmpty()) {
            showMessage("Please enter location");
            return false;
        }
        Price = inputPrice.getText().toString().trim();
        if (Price.isEmpty()) {
            showMessage("Please enter price");
            return false;
        }

        if (!(itemType != null && itemType.length() > 0)) {
            showMessage("Please enter item type ");
            return false;
        }
        if (!(itemColor != null && itemColor.length() > 0)) {
            showMessage("Please enter item color");
            return false;
        }
        if (!(DeliveryType != null && DeliveryType.length() > 0)) {
            showMessage("Please select delivery type");
            return false;
        }

        DeliveryCost = inputDeliveryCost.getText().toString().trim();
        if (DeliveryCost.isEmpty()) {
            if (deliveryPriceFlag) {
                DeliveryCost = "0.0";
            } else {
                showMessage("Please enter delivery cost");
                return false;
            }


        }

        return true;
    }

    private void showMessage(String msg) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, msg, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);

        textView.setTextColor(Color.YELLOW);

        snackbar.show();

    }

    public void init()

    {
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayoutnew);
        btnCategory = (Button) findViewById(R.id.btnCategory);
        btnSubCategory = (Button) findViewById(R.id.btnSubCategory);
        btnSize = (Button) findViewById(R.id.btnSize);
        btnColor = (Button) findViewById(R.id.btnColor);
        inputProductName = (EditText) findViewById(R.id.edttxtProductName);
        inputBrand = (EditText) findViewById(R.id.edttxtproductBrand);
        //  inputCompany= (EditText) findViewById(R.id.edttxtProductCompany);
        inputQuatanty = (EditText) findViewById(R.id.edttxtProductQuantity);
        inputDiscription = (EditText) findViewById(R.id.edttxtProductDescription);
        inputLocation = (EditText) findViewById(R.id.edttxtLocation);
        inputPrice = (EditText) findViewById(R.id.edttxtProdustPrice);
        //    inputDiscoutPrice= (EditText) findViewById(R.id.edttxtProdustDiscountPrice);
        //  inputDisCountPercentage= (EditText) findViewById(R.id.edttxtProdustDiscountPercentage);
        btnDeliveryType = (Button) findViewById(R.id.edttxtChooseDeliveryType);
        inputDeliveryCost = (EditText) findViewById(R.id.edttxtChooseDeliveryCost);

        radioNew = (RadioButton) findViewById(R.id.radioNew);
        radioUsed = (RadioButton) findViewById(R.id.radioUsed);

        ((RadioGroup) findViewById(R.id.fancy_radio_group)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton rb = (RadioButton) findViewById(checkedId);
                itemType = rb.getText().toString();

                if (itemType.equalsIgnoreCase("new"))
                    itemType = "0";
                else
                    itemType = "1";

                //  showMessage(""+checkedId);

            }
        });
        shippingCost = (LinearLayout) findViewById(R.id.shippingCost);

        img_gallery1 = (ImageView) findViewById(R.id.img_gallery1);
        img_gallery2 = (ImageView) findViewById(R.id.img_gallery2);
        img_gallery3 = (ImageView) findViewById(R.id.img_gallery3);
        img_gallery4 = (ImageView) findViewById(R.id.img_gallery4);
    }

    //getall category
    private void getAllCategory() {

        String tag_string_req = "getAllCharity";

        pDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.GET_ALL_CATEGORY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
              //  /(TAG, "cHARITY Response: " + response);
                hideDialog();

                ArrayList<String> stringArrayListID = new ArrayList<String>();
                ArrayList<String> stringArrayListNAME = new ArrayList<String>();
                JSONObject jObj;
                try {
                    jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("status");
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
                params.put("uid",session.getStringSessionData("uid"));
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    //getall SUB category
    private void getAllSUbCategory(final String mId) {
        String tag_string_req = "getAllSUbCategory";

        pDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.GET_ALL_SUB_CATEGORY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
              //  Log.e(TAG, "getAllSUbCategory Response: " + response);
                hideDialog();

                ArrayList<String> stringArrayListID = new ArrayList<String>();
                ArrayList<String> stringArrayListNAME = new ArrayList<String>();
                JSONObject jObj;
                try {
                    jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("status");
                    if (status) {

                        JSONArray jsonArray = jObj.getJSONArray("items");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            stringArrayListID.add(jsonObject.getString("id"));
                            stringArrayListNAME.add(jsonObject.getString("sub_category"));

                        }

                        allsubcategory_array = stringArrayListNAME.toArray(new String[stringArrayListNAME.size()]);
                        all_sub_category_id_array = stringArrayListID.toArray(new String[stringArrayListID.size()]);
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
             //   Log.e(TAG, "getAllSUbCategory Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("cid", mId);
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
                AppConfig.GET_ALL_SIZES, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
             //   Log.e(TAG, "GET_ALL_SIZES Response: " + response);
                hideDialog();

                ArrayList<String> stringArrayListSizeID = new ArrayList<String>();
                ArrayList<String> stringArrayListSizeNAME = new ArrayList<String>();
                JSONObject jObj;
                try {
                    jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("status");
                    if (status) {

                        JSONArray jsonArray = jObj.getJSONArray("items");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            stringArrayListSizeID.add(jsonObject.getString("id"));
                            stringArrayListSizeNAME.add(jsonObject.getString("size"));

                        }
                        all_sizes_array = stringArrayListSizeNAME.toArray(new String[stringArrayListSizeNAME.size()]);

                        all_sizes_id_arry = stringArrayListSizeID.toArray(new String[stringArrayListSizeID.size()]);
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
              //  Log.e(TAG, "GET_ALL_SIZES Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();


                params.put("scid", sId);
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

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();

       ;
    }

    /**
     * Uploading the file to server
     */
    private class EditFileToServer extends AsyncTask<Void, Integer, String> {
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
            HttpPost httppost = new HttpPost(AppConfig.UPDATE_PRODUCT);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });


                if(imageArrayFlag) {
                    ArrayList<String> imagearraylist = new ArrayList<>();

                    imagearraylist.addAll(imageFileArray.values());

                    for (int i = 0; i < imagearraylist.size(); i++) {

                        InputStream is = new URL( imagearraylist.get(i).toString() ).openStream();
                        Bitmap bitmap = BitmapFactory.decodeStream( is );
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bos);
                        byte[] data = bos.toByteArray();


                        entity.addPart("files["+i+"]", new ByteArrayBody(data,
                                "image/jpeg", i+""+System.currentTimeMillis()));
                        bitmap.recycle();
                    }
                }

                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                }

             //   String sel_cat=sb.toString();

                entity.addPart("img_id", new StringBody("" +sb));

//                }
                // Extra parameters if you want to pass to server
                entity.addPart("id", new StringBody(productid));
                entity.addPart("name", new StringBody(ProductName));
                entity.addPart("company", new StringBody(""));
                entity.addPart("brand", new StringBody(Brand));
                entity.addPart("description", new StringBody(Discription));
                entity.addPart("catergory", new StringBody(subcategoryId));
//                entity.addPart("user_id", new StringBody(mSession.getStringSessionData("uid")));
                entity.addPart("base_price", new StringBody(Price));
                //entity.addPart("discount",new StringBody( DiscoutPrice));
                //  entity.addPart("discount_percentage",new StringBody( DisCountPercentage));
                entity.addPart("total_quantity", new StringBody(Quatanty));
                entity.addPart("available_quantity", new StringBody(Quatanty));
                entity.addPart("delivery_cost", new StringBody(DeliveryCost));
                entity.addPart("delivery_type", new StringBody(DeliveryType));
                entity.addPart("latitude", new StringBody("" + lat));
                entity.addPart("longitude", new StringBody("" + lng));
                entity.addPart("location", new StringBody(strLocation));

                entity.addPart("size", new StringBody(itemSize));
                entity.addPart("is_used", new StringBody(itemType));
                entity.addPart("colour", new StringBody(itemColor));

                entity.addPart("AF", new StringBody("true"));
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
         //   Log.e(TAG, "Response from server: " + result);
            hideDialog();
            // showing the server response in an alert dialog
            showAlert(result);

            super.onPostExecute(result);
        }

        boolean flag = false;


        private void showAlert(String message) {
            String msg = "Something went wrong please try again";

            try {
                JSONObject jsondta = new JSONObject(message);


                if (jsondta.getString("status").equalsIgnoreCase("true")) {
                    msg = "Your product is updated successfully";
                    flag = true;
                }

            } catch (Exception e) {
            }

            final AlertDialog.Builder builder = new AlertDialog.Builder(vp.mom.activitys.SellingActivityEditProfile.this);

            builder.setMessage(msg).setTitle("Alert !") .setIcon(R.drawable.dialog_logo)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            if (flag)
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

    //18feb
    private void getSellingEditProfile() {

        String tag_string_req = "getSellingEditProfile";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.GET_SELLINGEDITPROFILE, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                //   Log.e("reportProduct", "reportProduct Response: " + response.toString());
                //	hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("status");
                    // Check for error node in json
                    if (status) {
                        //Toast.makeText(PrivacyPolicy.this, "Your concern about this product is recorded", Toast.LENGTH_SHORT).show();
                    //    Log.v("res", "" + jObj);
                        JSONArray jsonArray = jObj.getJSONArray("items");


                        //  if(!jsonArray.getJSONObject(0).isNull("productimages")) {
                        inputProductName.setText(jsonArray.getJSONObject(0).getString("name"));
                     //   Log.v("input name", "" + inputProductName);

                        String colorame=jsonArray.getJSONObject(0).getString("colour");
                        btnColor.setText(colorame);

                        itemColor=colorame;

                      //  Log.v("btnColor", "" + btnColor);
                        btnCategory.setText(jsonArray.getJSONObject(0).getString("cat_name"));
                      //  Log.v("btnCategory", "" + btnCategory);
                        String deliveryType=jsonArray.getJSONObject(0).getString("delivery_type");
                        btnDeliveryType.setText(deliveryType);

                        DeliveryType=deliveryType;

//                        Log.v("btnDeliveryType", "" + btnDeliveryType);
                        btnSubCategory.setText(jsonArray.getJSONObject(0).getString("sub_category"));
  //                      Log.v("btnSubCategory", "" + btnSubCategory);
                        btnSize.setText(jsonArray.getJSONObject(0).getString("size"));
    //                    Log.v("btnSize", "" + btnSize);
                        inputBrand.setText(jsonArray.getJSONObject(0).getString("brand"));
      //                  Log.v("inputBrand", "" + inputBrand);
                        inputQuatanty.setText(jsonArray.getJSONObject(0).getString("available_quantity"));
        //                Log.v("inputQuatanty", "" + inputQuatanty);
                        inputDiscription.setText(jsonArray.getJSONObject(0).getString("description"));
          //              Log.v(" inputDiscription", "" + inputDiscription);
                        inputLocation.setText(jsonArray.getJSONObject(0).getString("location"));
            //            Log.v("inputLocation", "" + inputLocation);
                        inputPrice.setText(jsonArray.getJSONObject(0).getString("base_price"));
              //          Log.v("inputPrice", "" + inputPrice);
                        inputDeliveryCost.setText(jsonArray.getJSONObject(0).getString("delivery_cost"));
                //        Log.v("inputDeliveryCost", "" + inputDeliveryCost);


                        if(jsonArray.getJSONObject(0).getString("delivery_type").equalsIgnoreCase("Meet in Person"))
                            shippingCost.setVisibility(View.GONE);
                        subcategoryId=jsonArray.getJSONObject(0).getString("catergory");


                        if (jsonArray.getJSONObject(0).getString("is_used").equals("0"))
                            radioNew.setChecked(true);
                        else
                            radioUsed.setChecked(true);

                        JSONObject imagefeed;
                        JSONArray imagefile = jsonArray.getJSONObject(0).getJSONArray("images");
                        for (int j = 0; j < imagefile.length(); j++) {

                            imagefeed = (JSONObject) imagefile.get(j);


                            if (j == 0) {
                                Picasso.with(vp.mom.activitys.SellingActivityEditProfile.this)
                                        .load(imagefeed.getString("path").replace("th_", ""))
                                        .placeholder(R.drawable.img_circle_placeholder)
                                        .into(img_gallery1);

                                imageID1 = imagefeed.getString("id");
                                imageflag1 = true;

                            } else if (j == 1) {

                                Picasso.with(vp.mom.activitys.SellingActivityEditProfile.this)
                                        .load(imagefeed.getString("path").replace("th_", ""))
                                        .placeholder(R.drawable.img_circle_placeholder)
                                        .into(img_gallery2);

                                imageID2 = imagefeed.getString("id");
                                imageflag2 = true;
                            } else if (j == 2) {

                                Picasso.with(vp.mom.activitys.SellingActivityEditProfile.this)
                                        .load(imagefeed.getString("path").replace("th_", ""))
                                        .placeholder(R.drawable.img_circle_placeholder)
                                        .into(img_gallery3);
                                imageID3 = imagefeed.getString("id");
                                imageflag3 = true;
                            } else {

                                Picasso.with(vp.mom.activitys.SellingActivityEditProfile.this)
                                        .load(imagefeed.getString("path").replace("th_", ""))
                                        .placeholder(R.drawable.img_circle_placeholder)
                                        .into(img_gallery4);
                                imageID4 = imagefeed.getString("id");
                                imageflag4 = true;
                            }

                        }


                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(vp.mom.activitys.SellingActivityEditProfile.this,
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(vp.mom.activitys.SellingActivityEditProfile.this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //   Log.e("ReportProduct", "ReportProduct Error: " + error.getMessage());
                Toast.makeText(vp.mom.activitys.SellingActivityEditProfile.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //	hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
//                params.put("order_id",orderid);
                params.put("pid", productid);

               /* params.put("order_id", orderid);
                params.put("product_id", productid);*/
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

}
