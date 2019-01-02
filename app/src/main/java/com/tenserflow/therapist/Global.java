package com.tenserflow.therapist;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.bumptech.glide.request.RequestOptions;
import com.tenserflow.therapist.Utils.ImageViewerActivity;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Admin on 12/12/2018.
 */

public class Global {

    public static final java.lang.String KEY_IMAGE_URL ="th.image.url.view" ;
    public static final java.lang.String KEY_IMAGE_PATH ="th.image.path.view" ;
    public static String user_id="";
 public static String token="";
 public static String user_name="";
 public static String userimage="";
 public static String user_email="";
 public static String user_address="";
 public static String user_phone_no="";
 public static String profileApiHit="false";
 public static String enterWayVar="";

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static MultipartBody.Part convertImageTomultipart(String path, String name) {
        Log.e("ImagePath",path);
        File file = new File(path);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData(name, file.getName(), requestFile);

        return body;
    }

    public static void changeFragment(Context context, Fragment fragment)
    {
        FragmentManager fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container,fragment)
            //    .addToBackStack(null)
                .commit();

    }

       public static RequestOptions getGlideOptions() {
        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.transform(new CircleCrop());
        requestOptions.skipMemoryCache(true);
        requestOptions.fitCenter();
      //  requestOptions.placeholder(R.drawable.default_image);
      //  requestOptions.error(R.drawable.default_image);
        return requestOptions;
    }

    public static void hideTitleBar(ImageViewerActivity cnxt) {
        cnxt.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cnxt.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }
}
