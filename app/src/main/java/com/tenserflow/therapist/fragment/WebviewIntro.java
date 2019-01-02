package com.tenserflow.therapist.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.tenserflow.therapist.MainActivity;
import com.tenserflow.therapist.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebviewIntro extends Fragment {


    @BindView(R.id.webview_intro)
    WebView webviewIntro;
    Unbinder unbinder;

    public WebviewIntro() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_webview_intro, container, false);
        unbinder = ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        if (bundle!=null)
        {
        if (bundle.containsKey("keyEnter"))
        {
            String key = bundle.getString("keyEnter");
            if (key.equalsIgnoreCase("Introduction"))
            {
                changeTitle("Introduction");
            }
            else if (key.equalsIgnoreCase("Legal(Informed Consent)"))
            {
                changeTitle("Legal(Informed Consent)");
            }
            else if (key.equalsIgnoreCase("HIPPA"))
            {
                changeTitle("About Therapist");
            }
        }
        }

        WebSettings webSettings = webviewIntro.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webviewIntro.loadUrl("http://therapy.gangtask.com/policy.php");




        return view;
    }


    private void changeTitle(String str) {

        ((MainActivity) getActivity()).userNameMainOptions.setText(str);
        ((MainActivity) getActivity()).editbtnMain.setVisibility(View.GONE);
        ((MainActivity) getActivity()).relBackMainactivity.setVisibility(View.GONE);
        ((MainActivity) getActivity()).relMenuMainactivity.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).lockDrawer(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
