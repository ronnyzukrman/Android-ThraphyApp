package com.tenserflow.therapist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tenserflow.therapist.R;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Admin on 12/24/2018.
 */

public class Date_time_Adapter extends RecyclerView.Adapter<Date_time_Adapter.ViewHolder> {

    Context context;
    ArrayList<HashMap<String, String>> hashMapDateTime = new ArrayList<>();


    public Date_time_Adapter(Context bookingDetail, ArrayList<HashMap<String, String>> hashMapDateTime) {
        this.context = bookingDetail;
        this.hashMapDateTime = hashMapDateTime;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_time_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.txtviewDateAdpt.setText(hashMapDateTime.get(position).get("date"));
        holder.txtviewSETimeAdpt.setText(hashMapDateTime.get(position).get("s_time")+" - "+hashMapDateTime.get(position).get("e_time"));

    }


    @Override
    public int getItemCount() {
        return hashMapDateTime.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_cross_date_time) ImageView imgCrossDateTime;
        @BindView(R.id.txtview_dateAdpt) TextView txtviewDateAdpt;
        @BindView(R.id.txtview_s_e_timeAdpt) TextView txtviewSETimeAdpt;
        Unbinder unbinder;


        @OnClick(R.id.img_cross_date_time)
        public void onViewClicked() {

            hashMapDateTime.remove(getAdapterPosition());
            notifyDataSetChanged();
        }

        public ViewHolder(View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }
    }
}
