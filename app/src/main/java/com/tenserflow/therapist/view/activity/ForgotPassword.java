package com.tenserflow.therapist.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.tenserflow.therapist.Global;
import com.tenserflow.therapist.MainActivity;
import com.tenserflow.therapist.R;
import com.tenserflow.therapist.Utils.DialogHelperClass;
import com.tenserflow.therapist.Utils.InternetConnectivity;
import com.tenserflow.therapist.Webservice.ChangePassword_Api;
import com.tenserflow.therapist.Webservice.ForgotPassword_Api;
import com.tenserflow.therapist.Webservice.Login_Api;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Admin on 12/12/2018.
 */

public class ForgotPassword extends AppCompatActivity {
    @BindView(R.id.edt_email_forgot_password)
    EditText edtEmailForgotPassword;
    @BindView(R.id.rel_forgot_password)
    RelativeLayout relForgotPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        ButterKnife.bind(this);


    }

    @OnClick(R.id.rel_back_forgot_password)
    public void onBackPressClicked() {
        onBackPressed();
    }

    @OnClick(R.id.txtview_send_request)
    public void onSendRequestClicked() {

        if (edtEmailForgotPassword.getText().toString().trim().equalsIgnoreCase("")) {
          //  edtEmailForgotPassword.setError("Please enter email");
            Snackbar snackbar = Snackbar.make(relForgotPassword, "Please enter email", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if (!Global.isValidEmail(edtEmailForgotPassword.getText().toString().trim())) {
          //  edtEmailForgotPassword.setError("Please enter valid email");
            Snackbar snackbar = Snackbar.make(relForgotPassword, "Please enter valid email", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else {
            Global.hideKeyboard(this);


            if (!InternetConnectivity.isConnected(this)) {
                DialogHelperClass.getConnectionError(this).show();
            } else {


                ForgotPassword_Api register_api = new ForgotPassword_Api();
                register_api.forgot(this, edtEmailForgotPassword.getText().toString().trim(), relForgotPassword, new ForgotPassword_Api.GetForgotPasswordCallBack() {
                    @Override
                    public void onGetForgotPasswordStatus(String response) {
                        try {

                            final JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase("1")) {
                                String msg = jsonObject.getString("message");
                                Snackbar snackbar = Snackbar.make(getWindow().getDecorView(), msg, Snackbar.LENGTH_LONG);
                                snackbar.show();

                                edtEmailForgotPassword.setText("");

                            } else {
                                String msg = jsonObject.getString("message");
                                Snackbar snackbar = Snackbar.make(getWindow().getDecorView(), msg, Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onGetForgotPasswordFailure() {

                    }
                });
            }
        }
    }
}

