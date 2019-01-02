package com.tenserflow.therapist.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.tenserflow.therapist.Global;
import com.tenserflow.therapist.MainActivity;
import com.tenserflow.therapist.R;
import com.tenserflow.therapist.Utils.HelperPreferences;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Admin on 12/11/2018.
 */

public class Splash extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);

      //  calculateHashKey();
        Thread timer= new Thread()
        {
            public void run()
            {
                try
                {
                    //Display for 3 seconds
                    sleep(1800);
                }
                catch (InterruptedException e)
                {
                    // TODO: handle exception
                    e.printStackTrace();
                }

               String user_id =  HelperPreferences.get(Splash.this).getString("user_id");
               String token =  HelperPreferences.get(Splash.this).getString("token");
               String enter_way =  HelperPreferences.get(Splash.this).getString("enter_way");
                if (user_id==null || user_id.equalsIgnoreCase(""))
                {
                    startActivity(new Intent(Splash.this,Login.class));
                    finish();
                }
                else
                {
                    Global.user_id = user_id;
                    Global.token = token;
                    Global.enterWayVar=enter_way;

                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    intent.putExtra("statusEntery", "Login");
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("token", token.toString());
                    startActivity(intent);
                    finish();
                }


            }
        };
        timer.start();
    }

    private void calculateHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.tenserflow.therapist",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash123456:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.e("DSFcsrfdvfd",e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.e("DSFcsrfdvfd1232345",e.getMessage());
        }
    }

}
