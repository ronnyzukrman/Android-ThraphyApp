package com.tenserflow.therapist.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tenserflow.therapist.Global;
import com.tenserflow.therapist.MainActivity;
import com.tenserflow.therapist.R;
import com.tenserflow.therapist.adapter.CustomDrawerAdapter;
import com.tenserflow.therapist.adapter.HomeAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {


    @BindView(R.id.recyclerview_home)
    RecyclerView recyclerviewHome;
    Unbinder unbinder;

    HomeAdapter mAdapter;

    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        unbinder = ButterKnife.bind(this, view);

        changeTitle();


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



        ((MainActivity)getActivity()).navigationDrawerList.setAdapter(new CustomDrawerAdapter(
                getActivity(),
                R.layout.custom_drawer_item,
                dataList, ((MainActivity)getActivity()).navigationDrawerList, ((MainActivity)getActivity()).drawerLayout,"1"));




        mAdapter = new HomeAdapter(getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerviewHome.setLayoutManager(mLayoutManager);
        recyclerviewHome.setItemAnimator(new DefaultItemAnimator());
        recyclerviewHome.setAdapter(mAdapter);



        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private  void changeTitle()
    {

        ((MainActivity)getActivity()).userNameMainOptions.setText("Home");
        ((MainActivity)getActivity()).editbtnMain.setVisibility(View.GONE);
        ((MainActivity)getActivity()).relBackMainactivity.setVisibility(View.GONE);
        ((MainActivity)getActivity()).relMenuMainactivity.setVisibility(View.VISIBLE);
        ((MainActivity)getActivity()).lockDrawer(false);






    }
}
