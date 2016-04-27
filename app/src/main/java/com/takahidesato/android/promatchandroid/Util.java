package com.takahidesato.android.promatchandroid;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by tsato on 4/26/16.
 */
public class Util {


    public static String decodeBase64(String string) {
        byte[] data1 = Base64.decode(string, Base64.DEFAULT);
        String result = null;
        try {
            result = new String(data1, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
