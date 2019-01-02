package com.tenserflow.therapist.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tenserflow.therapist.Global;
import com.tenserflow.therapist.R;
import com.tenserflow.therapist.fragment.Detail;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Admin on 12/13/2018.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    Context context;

    public HomeAdapter(FragmentActivity activity) {
        this.context = activity;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_adapter, parent, false);


        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 5;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cardview_home)
        CardView cardviewHome;
        Unbinder unbinder;

        @OnClick(R.id.cardview_home)
        public void onViewClicked() {
            Global.changeFragment(context, new Detail());
        }


        public ViewHolder(View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);

        }
    }

}
