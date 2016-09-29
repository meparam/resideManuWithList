package vp.mom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.ArrayList;

import za.co.riggaroo.materialhelptutorial.TutorialItem;
import za.co.riggaroo.materialhelptutorial.tutorial.MaterialTutorialActivity;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2300;
    private vp.mom.api.SessionManager session;
   /// UserSharedPref mypref;
    ImageView splashlogo;
   public Animation mIvSplashAnim;
    SharedPreferences prefs = null;
    private static final String PREF_NAME = "MOMSession";
    private static final int REQUEST_CODE = 1234;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splasch_screen);
        mIvSplashAnim = AnimationUtils.loadAnimation(this, R.anim.splash);
        splashlogo=(ImageView) findViewById(R.id.splashlogo);
        session = new vp.mom.api.SessionManager(getApplicationContext());
        splashlogo.startAnimation(mIvSplashAnim);

        prefs = getSharedPreferences(vp.mom.api.SessionManager.PREF_NAME, MODE_PRIVATE);
        //      mypref =new UserSharedPref(this);

//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.edreamz.materialmenudrawer",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.e("YourKeyHash :", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//               // System.out.Println("YourKeyHash: ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }



        new Handler().postDelayed(new Runnable() {



            @Override
            public void run() {

                if (session.getBooleanSessionData("isLoggedin")) {

                    Intent i = new Intent(vp.mom.SplashScreen.this, vp.mom.MainActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                    finish();
                }
                else  if (prefs.getBoolean("firstrun", true)) {
                    prefs.edit().putBoolean("firstrun", false).commit();
                    Intent mainAct = new Intent(vp.mom.SplashScreen.this, MaterialTutorialActivity.class);
                    mainAct.putParcelableArrayListExtra(MaterialTutorialActivity.MATERIAL_TUTORIAL_ARG_TUTORIAL_ITEMS, getTutorialItems(vp.mom.SplashScreen.this));
                    startActivityForResult(mainAct, REQUEST_CODE);


                }
                else
                {
                    Intent i = new Intent(vp.mom.SplashScreen.this, vp.mom.LoginActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //    super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            Intent i = new Intent(vp.mom.SplashScreen.this, vp.mom.LoginActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
            finish();

        }
    }
    private ArrayList<TutorialItem> getTutorialItems(Context context) {
        TutorialItem tutorialItem1 = new TutorialItem("its the fun way to shop ",
                context.getString(R.string.app_name),
                R.color.slide_1, R.drawable.tutorial_logo,  R.drawable.tutorial_logo);

        TutorialItem tutorialItem2 = new TutorialItem("sell an item just by taking a picture",
                context.getString(R.string.app_name),
                R.color.slide_2, R.drawable.tut_page_2_front,  R.drawable.tut_page_2_background);

        TutorialItem tutorialItem3 = new TutorialItem("buy an item with just one tap",
                context.getString(R.string.app_name),
                R.color.slide_3, R.drawable.tut_page_3_foreground,  R.drawable.tut_page_3_foreground);

        TutorialItem tutorialItem4 = new TutorialItem("Follow people and brands",
                context.getString(R.string.app_name),
                R.color.slide_4, R.drawable.tutorial_logo,  R.drawable.tutorial_logo);


        ArrayList<TutorialItem> tutorialItems = new ArrayList<>();
        tutorialItems.add(tutorialItem1);
        tutorialItems.add(tutorialItem2);
        tutorialItems.add(tutorialItem3);
        tutorialItems.add(tutorialItem4);

        return tutorialItems;
    }

}
