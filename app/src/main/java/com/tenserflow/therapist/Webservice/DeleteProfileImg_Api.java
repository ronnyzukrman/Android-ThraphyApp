package com.tenserflow.therapist.Webservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.RelativeLayout;

import com.google.gson.JsonObject;
import com.tenserflow.therapist.Global;
import com.tenserflow.therapist.Utils.DialogHelperClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Admin on 12/18/2018.
 */

public class DeleteProfileImg_Api {

    Context context;
    ProgressDialog progressDialog;


    public void deleteProfielImg(Context registerScreen, final DelProfileImgCallBack callBack) {
        this.context = registerScreen;

       progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");


        Service service = Client.retrofit.create(Service.class);
        Call<JsonObject> call = service.delete_profile_img(Global.token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if (response.isSuccessful())
                callBack.onDelProfileImgStatus(response.body().toString());


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {



                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                DialogHelperClass.getSomethingError(context).show();

                callBack.onDelProfileImgFailure();


            }
        });



    }
    public interface DelProfileImgCallBack {
        void onDelProfileImgStatus(String jsonObject);
        void onDelProfileImgFailure();
    }


}
