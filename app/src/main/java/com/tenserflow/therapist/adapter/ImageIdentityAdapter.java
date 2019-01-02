package com.tenserflow.therapist.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tenserflow.therapist.Global;
import com.tenserflow.therapist.R;
import com.tenserflow.therapist.Utils.ImageViewerActivity;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Admin on 12/26/2018.
 */

public class ImageIdentityAdapter extends RecyclerView.Adapter<ImageIdentityAdapter.ViewHolder> {
    Context context;
    ArrayList<String> arrayListImg = new ArrayList<>();


    public ImageIdentityAdapter(FragmentActivity activity, ArrayList<String> arrayListImg) {
        this.context = activity;
        this.arrayListImg = arrayListImg;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_identity_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (arrayListImg.size() != 0)
            Glide.with(context).load(new File(arrayListImg.get(position))).apply(new RequestOptions()).into(holder.imgviewDocs);

    }


    @Override
    public int getItemCount() {
        return arrayListImg.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgview_docs)
        ImageView imgviewDocs;
        Unbinder unbinder;

        @OnClick({R.id.imgview_cross_imgAdpt,R.id.imgview_docs})
        public void onViewClicked(View view) {

            switch (view.getId())
            {
                case R.id.imgview_cross_imgAdpt:
                    try {
                        arrayListImg.remove(getLayoutPosition());
                        notifyDataSetChanged();
                    }catch (Exception e){
                    }
                    break;

                case R.id.imgview_docs:
                    ImageViewerActivity.start(context, arrayListImg.get(getAdapterPosition()),Global.KEY_IMAGE_PATH);
                    break;
            }



        }

        public ViewHolder(View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }
    }
}
