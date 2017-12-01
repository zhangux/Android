package com.qdzl.util;

/**
 * Created by QDZL on 2017/11/29.
 */
public class TextUtils {
    public static boolean verStr(String str){
        if(str!=null&&str.trim()!=""){
            return true;
        }
        return false;
    }
}
