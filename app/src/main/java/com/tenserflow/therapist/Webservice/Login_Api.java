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

public class Login_Api {


    Context context;
    String username, password, email_reg, phone_number, address, dob;
    ProgressDialog progressDialog;
    RelativeLayout relTopSignup;
    boolean checkedVal;

    public void login(Context registerScreen, final String email_reg, final String password, final RelativeLayout relTopSignup, final boolean checkedVal) {
        this.context = registerScreen;
        this.password = password;
        this.email_reg = email_reg;
        this.relTopSignup = relTopSignup;
        this.checkedVal = checkedVal;

        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        Service service = Client.retrofit.create(Service.class);
        Call<JsonObject> call = service.login(context.getString(R.string.auth_value),  email_reg, password);
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


                      /*  SharedPreferences preferences = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_WORLD_WRITEABLE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("Authentication_Id",userid.getText().toString());
                        editor.putString("Authentication_Password",password.getText().toString());
                        editor.putString("Authentication_Status","true");
                        editor.apply();
                        */

                        final String user_id = jsonObject.getString("user_id").toString();
                        final String token = jsonObject.getString("token").toString();

                        Global.user_id = user_id;
                        Global.token = token;

                        Global.enterWayVar="simple";
                        if (checkedVal)
                        {
                            HelperPreferences.get(context).saveString("user_id",user_id);
                            HelperPreferences.get(context).saveString("token",token);
                            HelperPreferences.get(context).saveString("enter_way","simple");
                        }





                        String msg = jsonObject.getString("message");
                        Snackbar snackbar = Snackbar.make(relTopSignup, msg, Snackbar.LENGTH_LONG);
                        snackbar.show();


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