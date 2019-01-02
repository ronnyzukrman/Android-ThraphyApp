package com.tenserflow.therapist.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.util.Util;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
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
import com.tenserflow.therapist.Global;
import com.tenserflow.therapist.R;
import com.tenserflow.therapist.Utils.DialogHelperClass;
import com.tenserflow.therapist.Utils.InternetConnectivity;
import com.tenserflow.therapist.Webservice.Login_Api;
import com.tenserflow.therapist.Webservice.SocialLogin_Api;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Admin on 12/11/2018.
 */

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    private static final int RC_SIGN_IN = 0;
    @BindView(R.id.fb_login)
    ImageView fbLogin;
    @BindView(R.id.loginButton)
    LoginButton loginButton;
    @BindView(R.id.check_remember_login)
    CheckBox checkRememberLogin;
    @BindView(R.id.edt_password_signin)
    TextInputEditText edtPasswordSignin;
    @BindView(R.id.etPasswordLayout)
    TextInputLayout etPasswordLayout;

    // Google client to communicate with Google
    private GoogleApiClient mGoogleApiClient;
    GoogleSignInClient mGoogleSignInClient;
    CallbackManager callbackManager;

    @BindView(R.id.edt_email_signin)
    EditText edtEmailSignin;
    @BindView(R.id.rel_top_signin)
    RelativeLayout relTopSignin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        ButterKnife.bind(this);



        // fb();

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


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @OnClick({R.id.fb_login, R.id.google_login, R.id.rel_signup, R.id.txtview_forgot_password, R.id.txtview_signin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rel_signup:
                startActivity(new Intent(Login.this, Register.class));
                finish();
                break;
            case R.id.txtview_forgot_password:
                startActivity(new Intent(Login.this, ForgotPassword.class));
                break;
            case R.id.txtview_signin:
                if (edtEmailSignin.getText().toString().trim().equalsIgnoreCase("")) {
                    edtEmailSignin.requestFocus();
                   // edtEmailSignin.setError("Please enter email");
                    Snackbar snackbar = Snackbar.make(relTopSignin, "Please enter email", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (!Global.isValidEmail(edtEmailSignin.getText().toString().trim())) {
                    edtEmailSignin.requestFocus();
                  //  edtEmailSignin.setError("Please enter valid email");
                    Snackbar snackbar = Snackbar.make(relTopSignin, "Please enter valid email", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (edtPasswordSignin.getText().toString().trim().equalsIgnoreCase("")) {
                    edtPasswordSignin.requestFocus();
                   // edtPasswordSignin.setError("Please enter password");
                    Snackbar snackbar = Snackbar.make(relTopSignin, "Please enter password", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (edtPasswordSignin.getText().toString().trim().length() < 6) {
                    edtPasswordSignin.requestFocus();
                   // edtPasswordSignin.setError("Password should be atleast 6 characters");
                    Snackbar snackbar = Snackbar.make(relTopSignin, "Password should be atleast 6 characters", Snackbar.LENGTH_LONG);
                    snackbar.show();

                } else {


                    Global.hideKeyboard(this);

                    boolean checkedVal = checkRememberLogin.isChecked();

                    if (!InternetConnectivity.isConnected(this)) {
                        DialogHelperClass.getConnectionError(this).show();
                    } else {


                        Login_Api register_api = new Login_Api();
                        register_api.login(this, edtEmailSignin.getText().toString().trim(), edtPasswordSignin.getText().toString().trim(), relTopSignin, checkedVal);
                    }
                }
                break;
            case R.id.fb_login:
                LoginManager.getInstance().logOut();
                loginButton.performClick();
                fb();
                break;
            case R.id.google_login:
                signIn();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

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
               /* Log.e("DVSvffdsv", acct.getDisplayName());
                Log.e("DVSvffdsv", acct.getEmail());
                Log.e("DVSvffdsv", acct.getId());*/
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
                    register_api.social_login(this, acct.getId(), "g", acct.getDisplayName(), acct.getEmail(), relTopSignin, photo_url);
                }

            } else {


            }
        } else {

        }
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

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    //----------------------fb-----------------------------------

    private void fb() {

       // LoginManager.getInstance().logOut();

        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                //loginResult.getAccessToken();
                //loginResult.getRecentlyDeniedPermissions()
                //loginResult.getRecentlyGrantedPermissions()
                boolean loggedIn = AccessToken.getCurrentAccessToken() == null;
                Log.d("API123", loggedIn + " ??");
                Log.e("Fvredfved", loginResult.getAccessToken().getUserId());
                getUserProfile(AccessToken.getCurrentAccessToken(), Login.this);

               /* Intent intent = new Intent(Login.this, MainActivity.class);
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
                Log.e("SFBvfdsb", exception.getMessage());
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
                            Log.e("SDVrfdvcd", last_name);
                            Log.e("SDVrfdvcd", email);
                            Log.e("SDVrfdvcd", image_url);*/



                            if (!InternetConnectivity.isConnected(Login.this)) {
                                DialogHelperClass.getConnectionError(Login.this).show();
                            } else {

                                SocialLogin_Api register_api = new SocialLogin_Api();
                                register_api.social_login(context, id, "f", first_name, email, relTopSignin, image_url);
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
}
