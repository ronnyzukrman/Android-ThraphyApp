package com.tenserflow.therapist.Webservice;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.RelativeLayout;

import com.google.gson.JsonObject;
import com.tenserflow.therapist.Global;
import com.tenserflow.therapist.MainActivity;
import com.tenserflow.therapist.R;
import com.tenserflow.therapist.Utils.DialogHelperClass;
import com.tenserflow.therapist.Utils.HelperPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Admin on 12/13/2018.
 */

public class Register_Api {

    Context context;
    String username, password, email_reg, phone_number, address, dob,ccpVal;
    ProgressDialog progressDialog;
    RelativeLayout relTopSignup;


    public void register(Context registerScreen, final String username, final String email_reg, final String password, final String phone_number, final String address, final String dob,final RelativeLayout relTopSignup,String ccpVal) {
        this.context = registerScreen;
        this.username = username;
        this.password = password;
        this.email_reg = email_reg;
        this.phone_number = phone_number;
        this.address = address;
        this.dob = dob;
        this.relTopSignup = relTopSignup;
        this.ccpVal = ccpVal;


        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        Service service = Client.retrofit.create(Service.class);
        Call<JsonObject> call = service.register(context.getString(R.string.auth_value),username, email_reg, password, phone_number, address, dob,ccpVal,"");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if (response.isSuccessful())
                {


                try {

                    final JSONObject jsonObject = new JSONObject(response.body().toString());
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {

                        String msg = jsonObject.getString("message");
                        Snackbar snackbar = Snackbar.make(relTopSignup, msg, Snackbar.LENGTH_LONG);
                        snackbar.show();
                        final String user_id = jsonObject.getString("user_id").toString();
                        final String token = jsonObject.getString("token").toString();


                        HelperPreferences.get(context).saveString("user_id",user_id);
                        HelperPreferences.get(context).saveString("token",token);
                        HelperPreferences.get(context).saveString("enter_way","simple");

                        Global.user_id = user_id;
                        Global.token = token;
                        Global.enterWayVar="simple";



                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(context, MainActivity.class);
                                intent.putExtra("statusEntery", "Register");
                                intent.putExtra("user_id", user_id);
                                intent.putExtra("token", token.toString());
                                context.startActivity(intent);
                                ((Activity) context).finish();
                            }
                        }, Snackbar.LENGTH_LONG + 1000);
                    }
                    else
                    {
                        String msg = jsonObject.getString("message");
                        Snackbar snackbar = Snackbar.make(relTopSignup, msg, Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {


                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                DialogHelperClass.getSomethingError(context).show();

            }
        });




    }
}