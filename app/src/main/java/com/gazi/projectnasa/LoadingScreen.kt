package com.gazi.projectnasa

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.view.LayoutInflater
import android.view.Window

class LoadingScreen (myActivity : Activity) {

    val activity : Activity = myActivity;
    lateinit var dialog : Dialog;

    fun startLoadingDialog(){
        val builder = AlertDialog.Builder(activity);
        val inflater : LayoutInflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.loading_screen, null))
        builder.setCancelable(true);

        dialog = Dialog(activity,android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_screen);
        dialog.show();
    }

    fun dismissDialog(){
        dialog.dismiss();
    }
}