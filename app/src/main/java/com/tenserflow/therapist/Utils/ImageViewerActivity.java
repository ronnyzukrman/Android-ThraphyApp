package com.tenserflow.therapist.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tenserflow.therapist.Global;
import com.tenserflow.therapist.R;


import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by rohan on 12/7/17.
 */

public class ImageViewerActivity extends AppCompatActivity {

    @BindView(R.id.img_full_screen)
    ImageView imvFullScreen;

//    int imgUrl;
    String imageString="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Global.hideTitleBar(this);
        setContentView(R.layout.activity_image_viewer);

        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
//            imgUrl = bundle.getInt(Global.KEY_IMAGE_URL);

            if (bundle.containsKey(Global.KEY_IMAGE_URL))
            {
                imageString=bundle.getString(Global.KEY_IMAGE_URL);
                if (imageString!=null&&!imageString.equalsIgnoreCase(""))
                {
                    Glide.with(this).load(imageString).apply(new RequestOptions()).into(imvFullScreen);
                }
            }
            else if (bundle.containsKey(Global.KEY_IMAGE_PATH))
            {
                imageString=bundle.getString(Global.KEY_IMAGE_PATH);
                if (imageString!=null&&!imageString.equalsIgnoreCase(""))
                {
                    Glide.with(this).load(new File(imageString)).apply(new RequestOptions()).into(imvFullScreen);
                }
            }


            /*else{
            *//*    File f = new File(imageString);
                Drawable d = Drawable.createFromPath(f.getAbsolutePath());
                imvFullScreen.setImageDrawable(d);*//*
                imvFullScreen.setImageResource(imgUrl);
            }*/

        }
    }


    @OnClick(R.id.imv_close)
    void onClickClose() {
        finish();
    }



    public static void start(Context context,String url,String way_url_path) {
        Intent intent = new Intent(context, ImageViewerActivity.class);
        intent.putExtra(way_url_path, url);
        context.startActivity(intent);
    }


    public static void start(Context context, String url) {
        Intent intent = new Intent(context, ImageViewerActivity.class);
        intent.putExtra(Global.KEY_IMAGE_URL, url);
        context.startActivity(intent);
    }

}
