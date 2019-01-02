package com.tenserflow.therapist.fragment;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.tenserflow.therapist.MainActivity;
import com.tenserflow.therapist.R;
import com.tenserflow.therapist.adapter.Date_time_Adapter;
import com.tenserflow.therapist.adapter.ImageIdentityAdapter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookingDetail extends Fragment {


    @BindView(R.id.recyclerview_date_time_bd)
    RecyclerView recyclerviewDateTimeBd;
    Unbinder unbinder;
    @BindView(R.id.recyclerview_image_identity)
    RecyclerView recyclerviewImageIdentity;


    RecyclerView.LayoutManager layoutManager_img_identity;
    ImageIdentityAdapter imageIdentityAdapter;
    Date_time_Adapter date_time_adapter;

    byte[] byteArray;
    String img_path;

    ArrayList<String> arrayListImgs = new ArrayList<>();
    ArrayList<HashMap<String, String>> hashMapDateTime = new ArrayList<>();
    @BindView(R.id.s_timepicker)
    TimePicker sTimepicker;

    public BookingDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking_detail, container, false);
        unbinder = ButterKnife.bind(this, view);

        changeTitle();

        //----------------------Date & Time Adapter------------------------------------------------
        date_time_adapter = new Date_time_Adapter(getActivity(), hashMapDateTime);
        RecyclerView.LayoutManager layoutManager_img = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerviewDateTimeBd.setLayoutManager(layoutManager_img);
        recyclerviewDateTimeBd.setItemAnimator(new DefaultItemAnimator());
        recyclerviewDateTimeBd.setAdapter(date_time_adapter);

        //----------------------Image Adapter------------------------------------------------
        imageIdentityAdapter = new ImageIdentityAdapter(getActivity(), arrayListImgs);
        layoutManager_img_identity = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerviewImageIdentity.setLayoutManager(layoutManager_img_identity);
        recyclerviewImageIdentity.setItemAnimator(new DefaultItemAnimator());
        recyclerviewImageIdentity.setAdapter(imageIdentityAdapter);


        return view;
    }


    private void changeTitle() {

        ((MainActivity) getActivity()).userNameMainOptions.setText("Booking Detail");
        ((MainActivity) getActivity()).editbtnMain.setVisibility(View.GONE);
        ((MainActivity) getActivity()).relBackMainactivity.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).relMenuMainactivity.setVisibility(View.GONE);
        ((MainActivity) getActivity()).lockDrawer(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.img_license_upload, R.id.img_insurance_upload, R.id.rel_add_date_time,R.id.rel_starttime_booking_detail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_license_upload:
                insertDummyContactWrapper();
                break;
            case R.id.img_insurance_upload:
                insertDummyContactWrapper();
                break;
            case R.id.rel_add_date_time:

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("date", "12 Nov, 1993");
                hashMap.put("s_time", "08:00 AM");
                hashMap.put("e_time", "10:00 PM");

                hashMapDateTime.add(hashMap);
                date_time_adapter.notifyDataSetChanged();

                break;

            case  R.id.rel_starttime_booking_detail:
                Log.e("DSvcwsdvfdc","Dvscxdfsc");
              //  sTimepicker.performClick();

                break;
        }
    }


    //************************************************Camera Permission**********************************************
    @TargetApi(Build.VERSION_CODES.M)
    private void insertDummyContactWrapper() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 11);
        } else
            selectImage();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 11:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectImage();

                } else {
                    // Permission Denied
                    Toast.makeText(getActivity(), "CAMERA Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    // **********************image picker gallery or camera********************
    private void selectImage() {


        String[] colors = {"Take  photo", "Choose photo"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Action");

        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]

                if (which == 0) {
                    takePhotoFromCamera();
                } else if (which == 1) {
                    choosePhotoFromGallary();
                }

            }
        });
        builder.show();


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            Uri tempUri = getImageUri(this.getActivity(), imageBitmap);
            img_path = getRealPathFromURI(tempUri);
            arrayListImgs.add(img_path);
            imageIdentityAdapter.notifyDataSetChanged();

            //   Glide.with(this).load(new File(img_path)).apply(new RequestOptions()).into(circleviewNav);
        }

        if (requestCode == 2) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), contentURI);
                    Uri tempUri = getImageUri(getActivity(), bitmap);
//                    Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();

                    img_path = getRealPathFromURI(tempUri);
                    arrayListImgs.add(img_path);
                    imageIdentityAdapter.notifyDataSetChanged();
                    //      Glide.with(this).load(new File(img_path)).apply(new RequestOptions()).into(circleviewNav);


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(),
                inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    private void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, 2);
    }


}
