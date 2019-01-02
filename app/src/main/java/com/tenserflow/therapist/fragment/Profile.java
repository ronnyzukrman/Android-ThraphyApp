package com.tenserflow.therapist.fragment;


import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tenserflow.therapist.Global;
import com.tenserflow.therapist.MainActivity;
import com.tenserflow.therapist.R;
import com.tenserflow.therapist.Utils.DialogHelperClass;
import com.tenserflow.therapist.Utils.InternetConnectivity;
import com.tenserflow.therapist.Webservice.GetProfile_Api;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {


    @BindView(R.id.rel_profile)
    RelativeLayout relProfile;
    Unbinder unbinder;
    @BindView(R.id.txtview_username_profile)
    TextView txtviewUsernameProfile;
    @BindView(R.id.txt_address_profile)
    TextView txtAddressProfile;
    @BindView(R.id.txt_mail_profile)
    TextView txtMailProfile;
    @BindView(R.id.txt_phone_profile)
    TextView txtPhoneProfile;
    @BindView(R.id.txt_address_profile_val)
    TextView txtAddressProfileVal;
    @BindView(R.id.txt_mail_profile_val)
    TextView txtMailProfileVal;
    @BindView(R.id.txt_phone_profile_val)
    TextView txtPhoneProfileVal;

    String user_id, name, email, phone_no, address, dob, user_image,country_code;
    @BindView(R.id.circleview_nav)
    CircleImageView circleviewNav;
    private View view;

    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(view==null){
            view = inflater.inflate(R.layout.fragment_profile, container, false);
            unbinder = ButterKnife.bind(this, view);

        }

        unbinder = ButterKnife.bind(this, view);

        changeTitle();



        /*if (Global.user_email.equalsIgnoreCase(""))
        {

                 getProfileApi();

        }
        else {



            txtAddressProfileVal.setText(Global.user_address);
            txtMailProfileVal.setText(Global.user_email);
            txtPhoneProfileVal.setText(Global.user_phone_no);
            txtviewUsernameProfile.setText(Global.user_name);
                if (!Global.userimage.equalsIgnoreCase(""))
                {
                    Glide.with(getActivity()).load(Global.userimage).apply(new RequestOptions()).into(circleviewNav);
                }


        }*/

        ((MainActivity) getActivity()).editbtnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MainActivity) getActivity()).userNameMainOptions.setText("Edit Profile");
                ((MainActivity) getActivity()).editbtnMain.setVisibility(View.GONE);
                ((MainActivity) getActivity()).relBackMainactivity.setVisibility(View.VISIBLE);
                ((MainActivity) getActivity()).relMenuMainactivity.setVisibility(View.GONE);

                EditProfile editProfile = new EditProfile();
                Bundle bundle = new Bundle();
                bundle.putString("name", Global.user_name);
                bundle.putString("phone_no", Global.user_phone_no);
              //  bundle.putString("address", Global.user_address);
                bundle.putString("email", Global.user_email);
                bundle.putString("user_image", Global.userimage);
                bundle.putString("country_code",country_code);

                editProfile.setArguments(bundle);

                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.container, editProfile);
                // ft.addToBackStack(null);
                 ft.commit();
            }
        });




        return view;
    }

    private void getProfileApi() {


        if (!InternetConnectivity.isConnected(getActivity())) {
            DialogHelperClass.getConnectionError(getActivity()).show();
        } else {


            GetProfile_Api getProfile_api = new GetProfile_Api();
            getProfile_api.getProfile(getActivity(), "Profile", new GetProfile_Api.GetProfileCallBack() {

                @Override
                public void onGetProfileStatus(String jsonObject) {
                    try {
                        JSONObject jsonObject1 = new JSONObject(jsonObject);
                        String status = jsonObject1.getString("status");
                        String message = jsonObject1.getString("message");
                        //  Snackbar snackbar = Snackbar.make(getActivity().getWindow().getDecorView(), message, Snackbar.LENGTH_LONG);
                        //  snackbar.show();
                        if (status.equalsIgnoreCase("1")) {
                            Global.profileApiHit = "false";

                            JSONObject jsonObject2 = jsonObject1.getJSONObject("user_details");
                            user_id = jsonObject2.getString("user_id");
                            name = jsonObject2.getString("name");
                            email = jsonObject2.getString("email");
                            phone_no = jsonObject2.getString("phone_no");
                        //    address = jsonObject2.getString("address");
                            dob = jsonObject2.getString("dob");
                            user_image = jsonObject2.getString("user_image");
                            country_code = jsonObject2.getString("country_code");

                            Log.e("DVsrfvfsrd", name);
                            Log.e("DVsrfvfsrd", user_image);


                            Global.userimage = user_image;
                            Global.user_phone_no = phone_no;
                            Global.user_email = email;
                            Global.user_name = name;


                            try {
                                String c_code = "";
                                try {
                                    c_code = country_code.replace("\"", "");
                                } catch (Exception e) {

                                }


                              //  txtAddressProfileVal.setText(address);
                                txtMailProfileVal.setText(email);
                                txtPhoneProfileVal.setText(c_code + "  " + phone_no);
                                txtviewUsernameProfile.setText(name);


                                ((MainActivity) getActivity()).userEmailNav.setText(email);
                                ((MainActivity) getActivity()).txtviewUsernameNav.setText(name);

                               Glide.with(getActivity()).load(user_image).apply(new RequestOptions()).into(circleviewNav);
                                Glide.with(getActivity()).load(user_image).apply(new RequestOptions()).into(((MainActivity) getActivity()).circleviewNav);
                            } catch (Exception e) {

                            }

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onGetProfileFailure() {

                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private  void changeTitle()
    {

            ((MainActivity)getActivity()).userNameMainOptions.setText("Profile");
            ((MainActivity)getActivity()).editbtnMain.setVisibility(View.VISIBLE);
            ((MainActivity)getActivity()).relBackMainactivity.setVisibility(View.GONE);
            ((MainActivity)getActivity()).relMenuMainactivity.setVisibility(View.VISIBLE);
            ((MainActivity)getActivity()).lockDrawer(false);
    }


    @Override
    public void onResume() {
        super.onResume();
        getProfileApi();
    }
}
