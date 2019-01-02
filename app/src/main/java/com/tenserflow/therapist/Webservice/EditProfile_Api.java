package com.tenserflow.therapist.Webservice;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.gson.JsonObject;
import com.tenserflow.therapist.Global;
import com.tenserflow.therapist.MainActivity;
import com.tenserflow.therapist.R;
import com.tenserflow.therapist.Utils.DialogHelperClass;
import com.tenserflow.therapist.Utils.HelperPreferences;
import com.tenserflow.therapist.fragment.Home;
import com.tenserflow.therapist.fragment.Profile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Admin on 12/14/2018.
 */

public class EditProfile_Api {


    Context context;
    String phone_no, name,address,email;
    ProgressDialog progressDialog;
    RelativeLayout relEditProfile;
    String img_path,ccpVal;
    MultipartBody.Part imagefile_new;
    byte[] byteArray;
    String hitSataus="";

    public void edit_profile(Context registerScreen, final String name, final String phone_no, final String address, final String email, final RelativeLayout relEditProfile, String img_path,String ccpVal,byte[] byteArray ) {
        this.context = registerScreen;
        this.name = name;
        this.phone_no = phone_no;
        this.address = address;
        this.email = email;
        this.relEditProfile = relEditProfile;
        this.img_path = img_path;
        this.ccpVal = ccpVal;
        this.byteArray = byteArray;

        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        MultipartBody.Part image1 =null;

      //  Global.convertImageTomultipart(byteArray.toString(),"user_image");


      try {


          if (img_path==null  || img_path.equalsIgnoreCase(""))
          {
              hitSataus="img_null";
              File file = new File("");
              RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
              imagefile_new = MultipartBody.Part.createFormData("user_image", file.getName(), requestFile);

          }
          else
          {
              hitSataus="img_not_null";

              File file = new File(img_path);
              RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
              imagefile_new = MultipartBody.Part.createFormData("user_image", file.getName(), requestFile);

          }

      } catch (Exception e){

      }





        RequestBody nameRB  = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody emailRB  = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody phone_noRB  = RequestBody.create(MediaType.parse("text/plain"), phone_no);
        RequestBody addressRB  = RequestBody.create(MediaType.parse("text/plain"), address);
        RequestBody dobRB  = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody ccpValRB  = RequestBody.create(MediaType.parse("text/plain"), ccpVal);
        RequestBody flagRB  = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody localRB  = RequestBody.create(MediaType.parse("text/plain"), "local");

        Service service = Client.retrofit.create(Service.class);
        Call<JsonObject> call =  null;
        if (hitSataus.equalsIgnoreCase("img_null"))
        {
            call = service.editProfile( Global.token,nameRB , emailRB,phone_noRB ,addressRB,dobRB,"",ccpValRB,flagRB,localRB);
        }
        else
        {
           call = service.editProfile1( Global.token,nameRB , emailRB,phone_noRB ,addressRB,dobRB,imagefile_new,ccpValRB,flagRB,localRB);

        }

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
                        Snackbar snackbar = Snackbar.make(relEditProfile, msg, Snackbar.LENGTH_LONG);
                        snackbar.show();
                        Global.profileApiHit="false";
                       new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                ((MainActivity)context).onBackPressed();

                            }
                        }, Snackbar.LENGTH_LONG + 1000);
                    }
                    else
                    {
                        String msg = jsonObject.getString("message");
                        Snackbar snackbar = Snackbar.make(relEditProfile, msg, Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("DVsfdv",e.getMessage());
                }
                }
                else
                {

                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("DVsfdv",t.getMessage());
               if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                DialogHelperClass.getSomethingError(context).show();





            }
        });




    }

}
