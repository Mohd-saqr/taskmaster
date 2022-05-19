package net.gg.myapplication.Helper;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class Dialog extends DialogFragment {
    private String positiveText;
    private String negativeText;
    private String massage;
    private Activity activity;
    private Intent intent;


    public Dialog(String positiveText, String negativeText, Activity activity ,Intent intent,String msg) {
        this.positiveText = positiveText;
        this.negativeText = negativeText;
        this.activity = activity;
        this.intent=intent;
        this.massage=msg;
    }
    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(massage).setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
             startActivity(intent);
            }
        }).setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();
        return builder.create();
    }

}
