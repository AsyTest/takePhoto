package com.wws.very.takephotodemo.photo;

import android.content.Context;
import android.support.annotation.StringRes;
import android.view.View;

/**
 * Created by admin on 2016/10/17.
 * toast
 */
public class ToastUtils {

    private static ToastUtils toastUtils;

//    public static void showContent(Context mContext,String mContent){
//        Toast.makeText(mContext,mContent,Toast.LENGTH_SHORT).show();
//    }

    public static void showContent(Context context, CharSequence msg) {
        android.widget.Toast.makeText(context, msg, android.widget.Toast.LENGTH_LONG).show();
    }

    public static void showContent(Context context, @StringRes int stringId) {
        android.widget.Toast.makeText(context, stringId, android.widget.Toast.LENGTH_LONG).show();
    }

    public static void showContent(View view, CharSequence msg) {
        showContent(view.getContext(), msg);
    }

    public static void showContent(View view, @StringRes int stringId) {
        showContent(view.getContext(), stringId);
    }

}
