package com.tenserflow.therapist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.tenserflow.therapist.Webservice.GetProfile_Api;
import com.tenserflow.therapist.adapter.CustomDrawerAdapter;
import com.tenserflow.therapist.fragment.BookingDetail;
import com.tenserflow.therapist.fragment.ChangePassword;
import com.tenserflow.therapist.fragment.Detail;
import com.tenserflow.therapist.fragment.EditProfile;
import com.tenserflow.therapist.fragment.Home;
import com.tenserflow.therapist.fragment.Profile;
import com.tenserflow.therapist.fragment.WebviewIntro;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity /*implements NavigationView.OnNavigationItemSelectedListener*/ {

    @BindView(R.id.user_name_mainOptions)
    public TextView userNameMainOptions;
    @BindView(R.id.header_mainAct)
    RelativeLayout headerMainAct;
    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    public DrawerLayout drawerLayout;

    FragmentManager fm;
    FragmentTransaction ft;
    @BindView(R.id.navigation_drawer_list)
   public ListView navigationDrawerList;
    @BindView(R.id.rel_menu_mainactivity)
    public RelativeLayout relMenuMainactivity;
    public @BindView(R.id.editbtn_main)
    ImageView editbtnMain;
    @BindView(R.id.rel_back_mainactivity)
    public RelativeLayout relBackMainactivity;
    @BindView(R.id.circleview_nav)
    public CircleImageView circleviewNav;
    @BindView(R.id.txtview_username_nav)
    public TextView txtviewUsernameNav;
    @BindView(R.id.user_email_nav)
    public   TextView userEmailNav;

    /* @BindView(R.id.drawer_itemName)
     public TextView drawerItemName;
 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //-----------------setting view dashboard on first time----------------------
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("statusEntery")) {
          /* if (intent.getStringExtra("statusEntery").equalsIgnoreCase("Login"))
            {*/
            fm = getSupportFragmentManager();
            ft = fm.beginTransaction();
            ft.replace(R.id.container, new Home());
          //  ft.addToBackStack(null);   // on back press white screens comes when not comment
            ft.commit();
          /*  }
            else if (intent.getStringExtra("statusEntery").equalsIgnoreCase("Register"))
            {
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                ft.replace(R.id.container, new Profile());
              //  ft.addToBackStack(null);
                ft.commit();
            }*/

        }


        getProfileApi();
        ArrayList<String> dataList = new ArrayList<String>();
        if (Global.enterWayVar.equalsIgnoreCase("simple"))
        {

            dataList.add("Home");
            dataList.add("Introduction");
            dataList.add("Legal(Informed Consent)");
            dataList.add("Alerts");
            dataList.add("HIPPA");
            dataList.add("Profile");
            dataList.add("Change Password");
            dataList.add("Chat");
            dataList.add("Contact");
            dataList.add("Logout");
        }
        else
        {
            dataList.add("Home");
            dataList.add("Introduction");
            dataList.add("Legal(Informed Consent)");
            dataList.add("Alerts");
            dataList.add("HIPPA");
            dataList.add("Profile");
            dataList.add("Chat");
            dataList.add("Contact");
            dataList.add("Logout");
        }



        navigationDrawerList.setAdapter(new CustomDrawerAdapter(
                this,
                R.layout.custom_drawer_item,
                dataList, navigationDrawerList, drawerLayout,"0"));


    }


    @OnClick(R.id.rel_menu_mainactivity)
    public void onMenuClicked() {

        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START);
        }

    }






      /*  //---to handle drawer------------------------------------
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);


            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Global.hideKeyboard(MainActivity.this);
            }
        };
        toggle.syncState();

        drawerLayout.setDrawerListener(toggle);


        navView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) MainActivity.this);
        //-------------to_select_home_first-------------------
        navView.getMenu().getItem(0).setChecked(true);

*/



   /* @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    @OnClick(R.id.rel_menu_mainactivity)
    public void onMenuClicked() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }*/

    @Override
    public void onBackPressed() {
      //  super.onBackPressed();


       /* FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
*/

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.container);

        if (f instanceof Profile)
        {
            Global.changeFragment(this,new Home());
            userNameMainOptions.setText("Home");
            editbtnMain.setVisibility(View.GONE);
            relBackMainactivity.setVisibility(View.GONE);
            relMenuMainactivity.setVisibility(View.VISIBLE);
        }
        else if (f instanceof EditProfile)
        {
            Global.changeFragment(this,new Profile());
            Global.hideKeyboard(this);


           userNameMainOptions.setText("Profile");
           editbtnMain.setVisibility(View.VISIBLE);
           relBackMainactivity.setVisibility(View.GONE);
           relMenuMainactivity.setVisibility(View.VISIBLE);
        }
        else if (f instanceof ChangePassword)
        {
            Global.changeFragment(this,new Home());

            userNameMainOptions.setText("Home");
            editbtnMain.setVisibility(View.GONE);
            relBackMainactivity.setVisibility(View.GONE);
            relMenuMainactivity.setVisibility(View.VISIBLE);
        }
        else if (f instanceof Detail)
        {
            Global.changeFragment(this,new Home());

            userNameMainOptions.setText("Home");
            editbtnMain.setVisibility(View.GONE);
            relBackMainactivity.setVisibility(View.GONE);
            relMenuMainactivity.setVisibility(View.VISIBLE);
        }
        else if (f instanceof WebviewIntro)
        {
            Global.changeFragment(this,new Home());

            userNameMainOptions.setText("Home");
            editbtnMain.setVisibility(View.GONE);
            relBackMainactivity.setVisibility(View.GONE);
            relMenuMainactivity.setVisibility(View.VISIBLE);
        }
        else if (f instanceof BookingDetail)
        {
            Global.changeFragment(this,new Detail());
        }
        else
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Do you want to Exit?");

            // alert.setMessage("Message");

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //Your action here
                     finish();
                }
            });

            alert.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });

            alert.show();

         //   super.onBackPressed();

        }

    }

    @OnClick(R.id.rel_back_mainactivity)
    public void onViewClicked() {

        onBackPressed();


    }



    private void getProfileApi() {
        GetProfile_Api getProfile_api = new GetProfile_Api();
        getProfile_api.getProfile(this, "Main", new GetProfile_Api.GetProfileCallBack() {
            @Override
            public void onGetProfileStatus(String jsonObject) {
                try {


                    JSONObject jsonObject1 = new JSONObject(jsonObject);
                    String status = jsonObject1.getString("status");
                    String message = jsonObject1.getString("message");


                    if (status.equalsIgnoreCase("1")) {

                        JSONObject jsonObject2 = jsonObject1.getJSONObject("user_details");
                        Global.user_name = jsonObject2.getString("name");
                        Global.user_email = jsonObject2.getString("email");
                        Global.userimage = jsonObject2.getString("user_image");
                        Global.user_phone_no = jsonObject2.getString("phone_no");
                        Global.user_address = jsonObject2.getString("address");


                        userEmailNav.setText(Global.user_email);
                        txtviewUsernameNav.setText(Global.user_name);

                        Glide.with(MainActivity.this).load(Global.userimage)

                                .apply(new RequestOptions()).into(circleviewNav);


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

    public void lockDrawer(boolean isLocked) {
        if (!isLocked) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }else{
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

}
