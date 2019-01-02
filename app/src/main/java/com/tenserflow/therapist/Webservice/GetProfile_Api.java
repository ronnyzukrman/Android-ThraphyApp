package com.tenserflow.therapist.Webservice;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.RelativeLayout;

import com.google.gson.JsonObject;
import com.tenserflow.therapist.Global;
import com.tenserflow.therapist.Utils.DialogHelperClass;
import com.tenserflow.therapist.Utils.HelperPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Admin on 12/14/2018.
 */

public class GetProfile_Api {
    Context context;
    ProgressDialog progressDialog;

    String enterStatus;

    public void getProfile(Context registerScreen ,final String enterStatus, final GetProfileCallBack callBack) {
        this.context = registerScreen;
        this.enterStatus = enterStatus;

        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");


        Service service = Client.retrofit.create(Service.class);
        Call<JsonObject> call = service.getProfile(Global.token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if (response.isSuccessful())
                  callBack.onGetProfileStatus(response.body().toString());



            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {



                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
              //  DialogHelperClass.getSomethingError(context).show();
               callBack.onGetProfileFailure();




            }
        });




    }
    public interface GetProfileCallBack {
        void onGetProfileStatus(String jsonObject);
        void onGetProfileFailure();
    }


}
