package com.tenserflow.therapist.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tenserflow.therapist.MainActivity;
import com.tenserflow.therapist.R;
import com.tenserflow.therapist.adapter.ImageAdapter;
import com.tenserflow.therapist.adapter.ReviewAdapter;
import com.tenserflow.therapist.adapter.VideoAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class Detail extends Fragment {


    @BindView(R.id.recyclerview_review_detail)
    RecyclerView recyclerviewReviewDetail;
    Unbinder unbinder;

    ReviewAdapter reviewAdapter;
    @BindView(R.id.recyclerview_videos)
    RecyclerView recyclerviewVideos;
    @BindView(R.id.recyclerview_images)
    RecyclerView recyclerviewImages;

    public Detail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        unbinder = ButterKnife.bind(this, view);

        changeTitle();

        //----------------------review adapter---------------------------------------------------
        reviewAdapter = new ReviewAdapter(getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerviewReviewDetail.setLayoutManager(mLayoutManager);
        recyclerviewReviewDetail.setItemAnimator(new DefaultItemAnimator());
        recyclerviewReviewDetail.setAdapter(reviewAdapter);

        //--------------------Video Adapter-------------------------------------
        VideoAdapter videoAdapter = new VideoAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerviewVideos.setLayoutManager(layoutManager);
        recyclerviewVideos.setItemAnimator(new DefaultItemAnimator());
        recyclerviewVideos.setAdapter(videoAdapter);

        //----------------------Image Adapter------------------------------------------------
        ImageAdapter imageAdapter = new ImageAdapter();
        RecyclerView.LayoutManager layoutManager_img = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerviewImages.setLayoutManager(layoutManager_img);
        recyclerviewImages.setItemAnimator(new DefaultItemAnimator());
        recyclerviewImages.setAdapter(imageAdapter);


        return view;
    }

    private void changeTitle() {

        ((MainActivity) getActivity()).userNameMainOptions.setText("Detail");
        ((MainActivity) getActivity()).editbtnMain.setVisibility(View.GONE);
        ((MainActivity) getActivity()).relBackMainactivity.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).relMenuMainactivity.setVisibility(View.GONE);
        ((MainActivity) getActivity()).lockDrawer(true);


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.txtview_book_now)
    public void onViewClicked() {

        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container, new BookingDetail());
        // ft.addToBackStack(null);
        ft.commit();
    }
}
