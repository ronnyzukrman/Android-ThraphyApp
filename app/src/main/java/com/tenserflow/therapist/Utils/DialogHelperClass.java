package com.tenserflow.therapist.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Admin on 7/17/2018.
 */

public class DialogHelperClass {

    public static AlertDialog getConnectionError(Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("Alert!")
                .setCancelable(false)
                //set message
                .setMessage("Please check internet connection").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                }).create();

        return alertDialog;
    }

    public static AlertDialog getSomethingError(Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("Error!")
                .setCancelable(false)
                //set message
                .setMessage("Something went wrong.Please try again later").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                }).create();

                 return alertDialog;
    }

}
