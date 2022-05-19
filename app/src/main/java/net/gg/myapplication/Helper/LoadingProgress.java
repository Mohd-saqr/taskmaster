package net.gg.myapplication.Helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import net.gg.myapplication.R;

public class LoadingProgress {
     Activity activity;
     AlertDialog dialog;

    public LoadingProgress(Activity activity1){
        activity=activity1;
    }


    public void startLoading(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.progressbar,null));
        builder.setCancelable(false);
        dialog=builder.create();
        dialog.show();
    }

    public void stopLoading(){
      dialog.dismiss();
    }
}
