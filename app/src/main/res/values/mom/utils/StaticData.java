package vp.mom.utils;

import android.graphics.Color;

/**
 * Created by shivkanya.i on 15-01-2016.
 */
public class StaticData {

    public static String itemType="",itemCategory="",itemSubCategory="",itemsize="",itemcolor="";
    public  static  boolean itemFilterFlag=false;
    public static boolean shippingAddressFlag=false;

    public static String serachTab="item";


    public static boolean isColorDark(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return darkness >= 0.5;
    }
}
