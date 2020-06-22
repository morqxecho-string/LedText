package com.machacode.oscarmorquecho.ledtext.utils;

import android.content.Context;
import android.graphics.Typeface;

import com.machacode.oscarmorquecho.ledtext.models.LedTextRealm;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static android.text.TextUtils.isEmpty;

public class Utilities {
    public static final int DEFAULT = 1;
    public  static final int CUSTOM = 2;

    public static Realm createRealmInstance(Context context) {
        Realm.init(context);
        RealmConfiguration config = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);
        return Realm.getInstance(config);
    }

    public static int getNextIdLedTextRealm(Realm realm) {
        Number currentIdNum = realm.where(LedTextRealm.class).max("id");
        return (currentIdNum == null) ? 1 : currentIdNum.intValue() + 1;
    }

    public static Typeface getTypeFace(String fontPath, int TYPE_FONT, Context context) {
        Typeface typeface = Typeface.SANS_SERIF;
        if(TYPE_FONT == DEFAULT){
            switch(fontPath){
                case "sans_serif":
                    typeface = Typeface.SANS_SERIF;
                    break;

                case "monospace":
                    typeface = Typeface.MONOSPACE;
                    break;

                case "serif":
                    typeface = Typeface.SERIF;
                    break;

                case "default_bold":
                    typeface = Typeface.DEFAULT_BOLD;
                    break;

                case "default":
                    typeface = Typeface.DEFAULT;
                    break;
            }
            return typeface;
        }else if(TYPE_FONT == CUSTOM){
            return Typeface.createFromAsset(context.getAssets(), fontPath);
        } else {
            return typeface;
        }
    }

    static boolean isNotEmpty(CharSequence str) {
        return !isEmpty(str);
    }

    public static String getColorFromInt(int colour){
        String hexVal = Integer.toHexString(colour);
        String val = "0xff000000" + Integer.parseInt(String.valueOf(colour),16);
        return val;
    }
}
