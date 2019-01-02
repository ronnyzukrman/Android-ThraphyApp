package com.tenserflow.therapist.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.tenserflow.therapist.MainActivity;
import com.tenserflow.therapist.R;
import com.tenserflow.therapist.Utils.DialogHelperClass;
import com.tenserflow.therapist.Utils.InternetConnectivity;
import com.tenserflow.therapist.Webservice.ChangePassword_Api;
import com.tenserflow.therapist.Webservice.GetProfile_Api;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePassword extends Fragment {


    @BindView(R.id.edt_email_old_password)
    EditText edtEmailOldPassword;
    @BindView(R.id.edt_email_new_password)
    EditText edtEmailNewPassword;
    @BindView(R.id.edt_email_new_confirm_password)
    EditText edtEmailNewConfirmPassword;
    Unbinder unbinder;
    @BindView(R.id.rel_change_password)
    RelativeLayout relChangePassword;

    public ChangePassword() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        unbinder = ButterKnife.bind(this, view);

        changeTitle();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.txtview_send_request)
    public void onViewClicked() {

        if (edtEmailOldPassword.getText().toString().trim().equalsIgnoreCase("")) {
            edtEmailOldPassword.requestFocus();
           // edtEmailOldPassword.setError("Please enter old password");
            Snackbar snackbar = Snackbar.make(relChangePassword, "Please enter old password", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if (edtEmailOldPassword.getText().toString().trim().length() < 6) {
            edtEmailOldPassword.requestFocus();
           // edtEmailOldPassword.setError("Old password should be atleast 6 characters");
            Snackbar snackbar = Snackbar.make(relChangePassword, "Old password should be atleast 6 characters", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if (edtEmailNewPassword.getText().toString().trim().equalsIgnoreCase("")) {
            edtEmailNewPassword.requestFocus();
           // edtEmailNewPassword.setError("Please enter new password");
            Snackbar snackbar = Snackbar.make(relChangePassword, "Please enter new password", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if (edtEmailNewPassword.getText().toString().trim().length() < 6) {
            edtEmailNewPassword.requestFocus();
          //  edtEmailNewPassword.setError("New password should be atleast 6 characters");
            Snackbar snackbar = Snackbar.make(relChangePassword, "New password should be atleast 6 characters", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if (edtEmailNewConfirmPassword.getText().toString().trim().equalsIgnoreCase("")) {
            edtEmailNewConfirmPassword.requestFocus();
          //  edtEmailNewConfirmPassword.setError("Please enter confirm password");
            Snackbar snackbar = Snackbar.make(relChangePassword, "Please enter confirm password", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if (edtEmailNewConfirmPassword.getText().toString().trim().length() < 6) {
            edtEmailNewConfirmPassword.requestFocus();
          //  edtEmailNewConfirmPassword.setError("Confirm password should be atleast 6 characters");
            Snackbar snackbar = Snackbar.make(relChangePassword, "Confirm password should be atleast 6 characters", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if (!edtEmailNewPassword.getText().toString().trim().equalsIgnoreCase(edtEmailNewConfirmPassword.getText().toString().trim())) {
          //  edtEmailNewConfirmPassword.setError("Password mismatch");
            edtEmailNewConfirmPassword.requestFocus();
            Snackbar snackbar = Snackbar.make(relChangePassword, "Password does not match", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else {


            if(!InternetConnectivity.isConnected(getActivity()))
            {
                DialogHelperClass.getConnectionError(getActivity()).show();
            }
            else {


                ChangePassword_Api register_api = new ChangePassword_Api();
                register_api.changePassword(getActivity(), edtEmailOldPassword.getText().toString().trim(), edtEmailNewPassword.getText().toString().trim(), relChangePassword, new ChangePassword_Api.GetChangePasswordCallBack() {
                    @Override
                    public void onGetChangePasswordStatus(String response) {

                        try {

                            final JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase("1")) {
                                String msg = jsonObject.getString("message");
                                Snackbar snackbar = Snackbar.make(relChangePassword, msg, Snackbar.LENGTH_LONG);
                                snackbar.show();

                                edtEmailOldPassword.setText("");
                                edtEmailNewPassword.setText("");
                                edtEmailNewConfirmPassword.setText("");


                            } else {
                                String msg = jsonObject.getString("message");
                                Snackbar snackbar = Snackbar.make(relChangePassword, msg, Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }

                    @Override
                    public void onGetChangePasswordFailure() {

                    }
                });

            }
        }


    }

    private  void changeTitle()
    {

        ((MainActivity)getActivity()).userNameMainOptions.setText("Change Password");
        ((MainActivity)getActivity()).editbtnMain.setVisibility(View.GONE);
        ((MainActivity)getActivity()).relBackMainactivity.setVisibility(View.VISIBLE);
        ((MainActivity)getActivity()).relMenuMainactivity.setVisibility(View.GONE);
        ((MainActivity)getActivity()).lockDrawer(true);
    }

}
