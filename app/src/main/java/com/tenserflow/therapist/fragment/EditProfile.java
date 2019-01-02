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
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hbb20.CountryCodePicker;
import com.tenserflow.therapist.Global;
import com.tenserflow.therapist.MainActivity;
import com.tenserflow.therapist.R;
import com.tenserflow.therapist.Utils.DialogHelperClass;
import com.tenserflow.therapist.Utils.ImageViewerActivity;
import com.tenserflow.therapist.Utils.InternetConnectivity;
import com.tenserflow.therapist.Webservice.DeleteProfileImg_Api;
import com.tenserflow.therapist.Webservice.EditProfile_Api;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfile extends Fragment {


    @BindView(R.id.username_editProfile)
    EditText usernameEditProfile;
    @BindView(R.id.phoneno_editProfile)
    EditText phonenoEditProfile;
    @BindView(R.id.address_editProfile)
    EditText addressEditProfile;
    Unbinder unbinder;
    @BindView(R.id.email_editProfile)
    TextView emailEditProfile;
    @BindView(R.id.rel_editProfile)
    RelativeLayout relEditProfile;

    String name, phone_no, address, email, user_image;
    @BindView(R.id.rel_circleimage_profile_upper)
    RelativeLayout relCircleimageProfile;
    @BindView(R.id.circleview_nav)
    CircleImageView circleviewNav;


    Uri fileUri;
    private static final String IMAGE_DIRECTORY_NAME = "therapist";
    byte[] byteArray;
    String img_path;
    @BindView(R.id.ccp_edit)
    CountryCodePicker ccpEdit;
    @BindView(R.id.edt_phoneCode_editDetaill)
    EditText edtPhoneCodeEditDetaill;
    String country_code = "";
    @BindView(R.id.rel_camera_icon)
    RelativeLayout relCameraIcon;

    public EditProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        unbinder = ButterKnife.bind(this, view);
        changeTitle();

      /*  //-------------------backpress---------------------------------------------
        ((MainActivity)getActivity()).relBackMainactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/

        Bundle bundle = getArguments();
        if (bundle != null) {
            name = bundle.getString("name");
            phone_no = bundle.getString("phone_no");
            //   address = bundle.getString("address");
            email = bundle.getString("email");
            user_image = bundle.getString("user_image");
            country_code = bundle.getString("country_code");

            usernameEditProfile.setText(name);
            phonenoEditProfile.setText(phone_no);
            edtPhoneCodeEditDetaill.setText(country_code);
            //   addressEditProfile.setText(address);
            emailEditProfile.setText(email);
            if (!user_image.equalsIgnoreCase(""))
                //  Glide.with(getActivity()).load(user_image).apply(new RequestOptions()).into(circleviewNav);
                Log.e("cdwsdv csd", Global.user_name);
            if (!Global.userimage.equalsIgnoreCase(""))
                Glide.with(getActivity()).load(Global.userimage).apply(new RequestOptions()).into(circleviewNav);
        }


        edtPhoneCodeEditDetaill.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                String input = s.toString();

                if (!s.toString().startsWith("+")) {
                    edtPhoneCodeEditDetaill.setText("+" + s.toString());
                    Selection.setSelection(edtPhoneCodeEditDetaill.getText(), edtPhoneCodeEditDetaill.getText().length());
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {


            }
        });


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.txtview_send_request)
    public void onSaveClicked() {
        //  edtPhoneCodeEditDetaill


        if (usernameEditProfile.getText().toString().trim().equalsIgnoreCase("")) {
            usernameEditProfile.requestFocus();
            // usernameEditProfile.setError("Please enter username");
            Snackbar snackbar = Snackbar.make(relEditProfile, "Please enter name", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if (edtPhoneCodeEditDetaill.getText().toString().trim().equalsIgnoreCase("")) {
            edtPhoneCodeEditDetaill.requestFocus();
            //  phonenoEditProfile.setError("Please enter phone number");
            Snackbar snackbar = Snackbar.make(relEditProfile, "Please enter country code", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if (phonenoEditProfile.getText().toString().trim().equalsIgnoreCase("")) {
            phonenoEditProfile.requestFocus();
            //  phonenoEditProfile.setError("Please enter phone number");
            Snackbar snackbar = Snackbar.make(relEditProfile, "Please enter phone number", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if (phonenoEditProfile.getText().toString().trim().length() < 10) {
            phonenoEditProfile.requestFocus();
            //  phonenoEditProfile.setError("Please enter valid phone number");
            Snackbar snackbar = Snackbar.make(relEditProfile, "Please enter valid phone number", Snackbar.LENGTH_LONG);
            snackbar.show();
        } /*else if (addressEditProfile.getText().toString().trim().equalsIgnoreCase("")) {
            addressEditProfile.requestFocus();
            //  addressEditProfile.setError("Please enter address");
            Snackbar snackbar = Snackbar.make(relEditProfile, "Please enter address", Snackbar.LENGTH_LONG);
            snackbar.show();
        }*/
       /* else  if (img_path==null){
            Snackbar snackbar = Snackbar.make(getActivity().getWindow().getDecorView(), "Please select picture", Snackbar.LENGTH_LONG);
            snackbar.show();
        }*/
        else {

            if (img_path == null)
                img_path = "";

            // ccpEdit.getSelectedCountryCodeWithPlus()

            Global.hideKeyboard(getActivity());


            if (!InternetConnectivity.isConnected(getActivity())) {
                DialogHelperClass.getConnectionError(getActivity()).show();
            } else {


                EditProfile_Api editProfile = new EditProfile_Api();
                editProfile.edit_profile(getActivity(), usernameEditProfile.getText().toString().trim(), phonenoEditProfile.getText().toString().trim(), "", emailEditProfile.getText().toString().trim(), relEditProfile, img_path, edtPhoneCodeEditDetaill.getText().toString().trim(), byteArray);
            }
        }

    }


    @OnClick({R.id.rel_camera_icon, R.id.rel_circleimage_profile_upper})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rel_camera_icon:
                insertDummyContactWrapper();
                break;
            case R.id.rel_circleimage_profile_upper:
                ImageViewerActivity.start(getActivity(),Global.userimage,Global.KEY_IMAGE_URL);
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


        if (Global.userimage.equalsIgnoreCase("http://therapy.gangtask.com/public/images/default.jpg")) {


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
        } else {
            String[] colors = {"Take  photo", "Choose photo", "Remove photo"};
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
                    } else {
                        deleteProfielImg();
                    }
                }
            });
            builder.show();
        }


      /*  FilePickerBuilder.getInstance().setMaxCount(1)
                .setActivityTheme(R.style.LibAppTheme)
                .enableCameraSupport(true)
                //  .enableVideoPicker(true)
                //  .enableImagePicker(true)
                .pickPhoto(this);   // in fragment use this*/
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            Uri tempUri = getImageUri(this.getActivity(), imageBitmap);
            img_path = getRealPathFromURI(tempUri);

            Glide.with(this).load(new File(img_path)).apply(new RequestOptions()).into(circleviewNav);
        }

        if (requestCode == 2) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), contentURI);
                    Uri tempUri = getImageUri(getActivity(), bitmap);
//                    Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();

                    img_path = getRealPathFromURI(tempUri);
                    Glide.with(this).load(new File(img_path)).apply(new RequestOptions()).into(circleviewNav);


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }

/*
        if (resultCode == RESULT_OK && data != null) {
            ArrayList<String> photoPaths = new ArrayList<>();
            photoPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));


            if (photoPaths.size() != 0) {
                Uri photopath = Uri.parse(photoPaths.get(0));


                Glide.with(this).load(new File(photoPaths.get(0))).apply(new RequestOptions()).into(circleviewNav);
                Log.e("FBvdgbfd",photopath+"   hhh");
             //   img_path =  getRealPathFromURI(photopath);

               img_path = photopath.toString();

                   uriToByteArray(photopath.toString());



                //----------------------upload image api hit---------------------------------------------


               */
/* if(!InternetConnectivity.isConnected(getActivity()))
                {
                    DialogHelperClass.getConnectionError(getActivity()).show();
                }
                else {
                    UploadProfilePic uploadProfilePic = new UploadProfilePic();
                    uploadProfilePic.uploadPic(getActivity(), byteArray, token, user_id);
                }*//*


            }
        }
*/

    }

    public void uriToByteArray(String uri) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
        } catch (OutOfMemoryError e) {
        }

        byte[] buf = new byte[1024];
        int n;
        try {
            while (-1 != (n = fis.read(buf)))
                baos.write(buf, 0, n);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
        } catch (OutOfMemoryError e) {
        }

        byteArray = baos.toByteArray();
    }

    private void changeTitle() {

        ((MainActivity) getActivity()).userNameMainOptions.setText("Edit Profile");
        ((MainActivity) getActivity()).editbtnMain.setVisibility(View.GONE);
        ((MainActivity) getActivity()).relBackMainactivity.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).relMenuMainactivity.setVisibility(View.GONE);
        ((MainActivity) getActivity()).lockDrawer(true);
    }


    private void deleteProfielImg() {


        if (!InternetConnectivity.isConnected(getActivity())) {
            DialogHelperClass.getConnectionError(getActivity()).show();
        } else {

            DeleteProfileImg_Api delProfileImg_api = new DeleteProfileImg_Api();
            delProfileImg_api.deleteProfielImg(getActivity(), new DeleteProfileImg_Api.DelProfileImgCallBack() {

                @Override
                public void onDelProfileImgStatus(String response) {


                    try {

                        final JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("1")) {

                            String msg = jsonObject.getString("message");
                            Snackbar snackbar = Snackbar.make(relEditProfile, msg, Snackbar.LENGTH_LONG);
                            snackbar.show();
                            Global.userimage = "http://therapy.gangtask.com/public/images/default.jpg";

                            Glide.with(getActivity()).load(user_image).apply(new RequestOptions()).into(((MainActivity) getActivity()).circleviewNav);
                            Glide.with(getActivity()).load(Global.userimage).apply(new RequestOptions()).into(circleviewNav);
                        } else {
                            String msg = jsonObject.getString("message");
                            Snackbar snackbar = Snackbar.make(relEditProfile, msg, Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onDelProfileImgFailure() {

                }


            });
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


    //-------------------------------------------------

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