package com.qdzl.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Administrator on 2016/7/20.
 */
public class SharedUtil {
    public static SharedPreferences getPreference(Context context){
        SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(context);
        return sp;
    }
    public static void saveUserAccount(Context context,String account,String pwd){
        SharedPreferences.Editor e=getPreference(context).edit();
        e.putString("account",account);
        e.putString("password",pwd);
        e.commit();
    }

    public static String[] getUserAccount(Context context){
        String[] user=new String[2];
        SharedPreferences sp=getPreference(context);
        user[0]=sp.getString("account",null);
        user[1]=sp.getString("password",null);

        return user;
    }
    public static void save(Context context,String key, Object value){
        SharedPreferences.Editor e=getPreference(context).edit();
        if(value instanceof  String){
            e.putString(key,(String)value);
        }else if (value instanceof Integer){
            e.putInt(key,(Integer)value);
        }else if (value instanceof Boolean){
            e.putBoolean(key,(Boolean)value);
        }else if (value instanceof Long){
            e.putLong(key,(Long)value);
        }
        e.commit();
    }
//    public static long getDownload(Context context){
//        return getPreference(context).getLong(Constants.Key.DOWNLOAD_ID,-1);
//    }
    public static void remove(Context context,String key){
        SharedPreferences.Editor  e = getPreference(context).edit();
        e.remove(key);
        e.commit();
    }
}
