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

public class SocialLogin_Api {



    Context context;
    String password, social_id, name, email,type,photo_url_;
    ProgressDialog progressDialog;
    RelativeLayout relTopSignup;

    public void social_login(Context registerScreen, final String social_id, final String type, final String name, final String email, final RelativeLayout relTopSignup,String photo_url) {
        this.context = registerScreen;
        this.social_id = social_id;
        this.type = type;
        this.name = name;
        this.email = email;
        this.relTopSignup = relTopSignup;
        this.photo_url_ = photo_url;

        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        Service service = Client.retrofit.create(Service.class);
        Call<JsonObject> call = service.social_login(context.getString(R.string.auth_value),  social_id,type,name, email,"","","","",photo_url_,"","","link");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {




              if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }


                if (response.isSuccessful())

                {
                    if (response.isSuccessful())
                    {


                    try {


                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("1")) {
                            String message = jsonObject.getString("message");
                            String user_id = jsonObject.getString("user_id");
                            String token = jsonObject.getString("token");

                            Global.token = token;
                            Global.user_id = user_id;
                            Global.enterWayVar = "social" + type;

                            Log.e("Vfdfvbdf",token);

                            HelperPreferences.get(context).saveString("user_id",user_id);
                            HelperPreferences.get(context).saveString("token",token);
                            HelperPreferences.get(context).saveString("enter_way","social" + type);

                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("statusEntery", "Register");
                            intent.putExtra("user_id", Global.user_id);
                            intent.putExtra("token", Global.token);
                            context.startActivity(intent);
                            ((Activity) context).finish();

                      /*Snackbar snackbar = Snackbar.make(relTopSignup, "Sign In Successful", Snackbar.LENGTH_LONG);
                      snackbar.show();*/
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
