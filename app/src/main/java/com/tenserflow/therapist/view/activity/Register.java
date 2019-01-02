package com.tenserflow.therapist.view.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.hbb20.CountryCodePicker;
import com.tenserflow.therapist.Global;
import com.tenserflow.therapist.R;
import com.tenserflow.therapist.Utils.DialogHelperClass;
import com.tenserflow.therapist.Utils.InternetConnectivity;
import com.tenserflow.therapist.Webservice.Register_Api;
import com.tenserflow.therapist.Webservice.SocialLogin_Api;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Admin on 12/11/2018.
 */

public class Register extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {


    @BindView(R.id.edt_name_signup)
    EditText edtNameSignup;
    @BindView(R.id.edt_phone_signup)
    EditText edtPhoneSignup;
    @BindView(R.id.edt_address_signup)
    EditText edtAddressSignup;
    @BindView(R.id.edt_email_signup)
    EditText edtEmailSignup;
    @BindView(R.id.edt_password_signup)
    TextInputEditText edtPasswordSignup;
    @BindView(R.id.edt_c_password_signup)
    TextInputEditText edtCPasswordSignup;
    @BindView(R.id.rel_top_signup)
    RelativeLayout relTopSignup;
    @BindView(R.id.txtview_dob)
    TextView txtviewDob;
    @BindView(R.id.check_remember_register)
    CheckBox checkRememberRegister;
    @BindView(R.id.loginButton_register)
    LoginButton loginButton;
    @BindView(R.id.ccp)
    CountryCodePicker ccp;
    @BindView(R.id.edt_phoneCode_signup)
    EditText edtPhoneCodeSignup;

    String country_code="";

    private String TAG = Register.class.getSimpleName();
    String date = "";
    private static final int RC_SIGN_IN = 0;

    // Google client to communicate with Google
    private GoogleApiClient mGoogleApiClient;
    GoogleSignInClient mGoogleSignInClient;
    CallbackManager callbackManager;
//    String country_code="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        ButterKnife.bind(this);


        callbackManager = CallbackManager.Factory.create();


        // configure sign in to request the user's ID
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Build a GoogleApiClient with access to the Google Sign-In API and the options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build()).addScope(Plus.SCOPE_PLUS_LOGIN).build();



        edtPhoneCodeSignup.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                String input = s.toString();

                if(!s.toString().startsWith("+")){
                    edtPhoneCodeSignup.setText("+"+s.toString());
                    Selection.setSelection(edtPhoneCodeSignup.getText(), edtPhoneCodeSignup.getText().length());
                }



               /* String prefix = "+";
                if (!s.toString().startsWith(prefix)) {
                    String cleanString;
                    String deletedPrefix = prefix.substring(0, prefix.length() - 1);
                    if (s.toString().startsWith(deletedPrefix)) {
                        cleanString = s.toString().replaceAll(deletedPrefix, "");
                    } else {
                        cleanString = s.toString().replaceAll(prefix, "");
                    }
                    edtPhoneCodeSignup.setText(prefix + cleanString);
                    edtPhoneCodeSignup.setSelection(prefix.length());
                }*/


            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {


            }
        });


    }

    private void setTxt(String s) {
        edtPhoneCodeSignup.setText(s);
    }


    @OnClick(R.id.txtview_signup_signup)
    public void onSignupClicked() {


        if (edtNameSignup.getText().toString().trim().equalsIgnoreCase("")) {
            edtNameSignup.requestFocus();
            //  edtNameSignup.setError("Please enter name");
            Snackbar snackbar = Snackbar.make(relTopSignup, "Please enter name", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else if (edtPhoneCodeSignup.getText().toString().trim().equalsIgnoreCase("")) {
            edtPhoneCodeSignup.requestFocus();
            // edtPhoneSignup.setError("Please enter phone number");
            Snackbar snackbar = Snackbar.make(relTopSignup, "Please enter country code", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else if (edtPhoneCodeSignup.getText().toString().trim().length()>6) {
            edtPhoneCodeSignup.requestFocus();
            // edtPhoneSignup.setError("Please enter phone number");
            Snackbar snackbar = Snackbar.make(relTopSignup, "Please enter validcountry code", Snackbar.LENGTH_LONG);
            snackbar.show();
        }else if (edtPhoneSignup.getText().toString().trim().equalsIgnoreCase("")) {
            edtPhoneSignup.requestFocus();
            // edtPhoneSignup.setError("Please enter phone number");
            Snackbar snackbar = Snackbar.make(relTopSignup, "Please enter phone number", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if (edtPhoneSignup.getText().toString().trim().length() < 10) {
            edtPhoneSignup.requestFocus();
            //  edtPhoneSignup.setError("Please enter valid phone number");
            Snackbar snackbar = Snackbar.make(relTopSignup, "Please enter valid phone number", Snackbar.LENGTH_LONG);
            snackbar.show();
        }/* else if (edtAddressSignup.getText().toString().trim().equalsIgnoreCase("")) {
            edtAddressSignup.requestFocus();
          //  edtAddressSignup.setError("Please enter address");
            Snackbar snackbar = Snackbar.make(relTopSignup, "Please enter address", Snackbar.LENGTH_LONG);
            snackbar.show();
        }*/ else if (date.equalsIgnoreCase("")) {
            Snackbar snackbar = Snackbar.make(relTopSignup, "Please select D.O.B", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if (edtEmailSignup.getText().toString().trim().equalsIgnoreCase("")) {
            edtEmailSignup.requestFocus();
            // edtEmailSignup.setError("Please enter email");
            Snackbar snackbar = Snackbar.make(relTopSignup, "Please enter email", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if (!Global.isValidEmail(edtEmailSignup.getText().toString().trim())) {
            edtEmailSignup.requestFocus();
            // edtEmailSignup.setError("Please enter valid email");
            Snackbar snackbar = Snackbar.make(relTopSignup, "Please enter valid email", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if (edtPasswordSignup.getText().toString().trim().equalsIgnoreCase("")) {
            edtPasswordSignup.requestFocus();
            // edtPasswordSignup.setError("Please enter password");
            Snackbar snackbar = Snackbar.make(relTopSignup, "Please enter password", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if (edtPasswordSignup.getText().toString().trim().length() < 6) {
            edtPasswordSignup.requestFocus();
            // edtPasswordSignup.setError("Password should be atleast 6 characters");
            Snackbar snackbar = Snackbar.make(relTopSignup, "Password should be atleast 6 characters", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if (edtCPasswordSignup.getText().toString().trim().equalsIgnoreCase("")) {
            edtCPasswordSignup.requestFocus();
            //  edtCPasswordSignup.setError("Please enter password");
            Snackbar snackbar = Snackbar.make(relTopSignup, "Please enter confirm password", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if (edtCPasswordSignup.getText().toString().trim().length() < 6) {
            edtCPasswordSignup.requestFocus();
            // edtCPasswordSignup.setError("Password should be atleast 6 characters");
            Snackbar snackbar = Snackbar.make(relTopSignup, "Confirm password should be atleast 6 characters", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if (!edtCPasswordSignup.getText().toString().trim().equalsIgnoreCase(edtPasswordSignup.getText().toString().trim())) {
            //  edtCPasswordSignup.setError("Password mismatch");
            edtCPasswordSignup.requestFocus();
            Snackbar snackbar = Snackbar.make(relTopSignup, "Password does not  match", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if (!checkRememberRegister.isChecked()) {
            Snackbar snackbar = Snackbar.make(relTopSignup, "Please accept terms and conditions", Snackbar.LENGTH_LONG);
            snackbar.show();

        } else {
            Global.hideKeyboard(this);

            //  boolean checkedVal = checkRememberRegister.isChecked();

            // ccp.getSelectedCountryCodeWithPlus()
            if (!InternetConnectivity.isConnected(this)) {
                DialogHelperClass.getConnectionError(this).show();
            } else {


                Register_Api register_api = new Register_Api();
                register_api.register(this, edtNameSignup.getText().toString().trim(), edtEmailSignup.getText().toString().trim(), edtCPasswordSignup.getText().toString().trim(), edtPhoneSignup.getText().toString().trim(), "", date, relTopSignup, edtPhoneCodeSignup.getText().toString().trim());
            }
        }

    }


    @OnClick({R.id.terms_of_use_txtview, R.id.txtview_privacy_policy, R.id.rel_dob, R.id.signin_register, R.id.rel_back_register, R.id.fb_login_register, R.id.google_login_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.terms_of_use_txtview:
                Intent intent = new Intent(this, Webview.class);
                intent.putExtra("way", "TermsOfUse");
                startActivity(intent);

                break;
            case R.id.txtview_privacy_policy:
                Intent intent1 = new Intent(this, Webview.class);
                intent1.putExtra("way", "PrivacyPolicy");
                startActivity(intent1);

                break;
            case R.id.rel_dob:
                Calendar c = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Log.e(TAG, "onDateSet: Day" + dayOfMonth);
                                Log.e(TAG, "onDateSet: month" + (monthOfYear + 1));
                                Log.e(TAG, "onDateSet: year" + year);
                                txtviewDob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                txtviewDob.setTextColor(Color.parseColor("#1a1b1b"));

                                date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
                datePickerDialog.show();
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                break;
            case R.id.signin_register:
                startActivity(new Intent(Register.this, Login.class));
                finish();
                break;

            case R.id.rel_back_register:
                startActivity(new Intent(this, Login.class));
                finish();
                break;

            case R.id.fb_login_register:
                loginButton.performClick();
                fb();
                break;
            case R.id.google_login_register:
                signIn();
                break;
        }
    }

/*    @OnClick(R.id.rel_back_register)
    public void onBackClicked() {


    }*/

/*    @OnClick({R.id.fb_login_register, R.id.google_login_register})
    public void onSocialLoginClicked(View view) {
        switch (view.getId()) {
            case R.id.fb_login_register:
                loginButton.performClick();
                fb();
                break;
            case R.id.google_login_register:
                signIn();
                break;
        }
    }*/

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                // updateUI(false);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                GoogleSignInAccount acct = result.getSignInAccount();

                String photo_url;
                if (acct.getPhotoUrl() == null) {
                    photo_url = "http://therapy.gangtask.com/public/images/default.jpg";
                } else {
                    photo_url = acct.getPhotoUrl().toString();

                }


                if (!InternetConnectivity.isConnected(this)) {
                    DialogHelperClass.getConnectionError(this).show();
                } else {

                    SocialLogin_Api register_api = new SocialLogin_Api();
                    register_api.social_login(this, acct.getId(), "g", acct.getDisplayName(), acct.getEmail(), relTopSignup, photo_url);

                }
            } else {


            }
        } else {

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
    }

    //----------------------fb-----------------------------------

    private void fb() {
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                //loginResult.getAccessToken();
                //loginResult.getRecentlyDeniedPermissions()
                //loginResult.getRecentlyGrantedPermissions()
                boolean loggedIn = AccessToken.getCurrentAccessToken() == null;
                //  Log.d("API123", loggedIn + " ??");
                //  Log.e("Fvredfved", loginResult.getAccessToken().getUserId());
                getUserProfile(AccessToken.getCurrentAccessToken(), Register.this);

               /* Intent intent = new Intent(Register.this, MainActivity.class);
                intent.putExtra("statusEntery", "Register");
                intent.putExtra("user_id", "1");
                intent.putExtra("token", "5555");
                startActivity(intent);
                finish();*/


                //  SocialLogin_Api register_api = new SocialLogin_Api();
                //  register_api.social_login(this,"107208764924215896575", "g","John","John@gmail.com",relTopSignin);


            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

    }


    private void getUserProfile(AccessToken currentAccessToken, final Context context) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("TAG", object.toString());
                        try {
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String email = object.getString("email");
                            String id = object.getString("id");
                            //String phone_number= object.getString("phone_number");
                            String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";
                           /* Log.e("SDVrfdvcd", first_name);
                            Log.e("SDVrfdvcd", last_name);*/


                            if (!InternetConnectivity.isConnected(Register.this)) {
                                DialogHelperClass.getConnectionError(Register.this).show();
                            } else {

                                SocialLogin_Api register_api = new SocialLogin_Api();
                                register_api.social_login(context, id, "f", first_name, email, relTopSignup, image_url);
                            }

                            //    txtUsername.setText("First Name: " + first_name + "\nLast Name: " + last_name);
                            //    txtEmail.setText(email);
                            //txtPhone.setText(phone_number);
                            //  Picasso.with(MainActivity.this).load(image_url).into(imageView);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Login.class));
        finish();
        //  super.onBackPressed();
    }
}
