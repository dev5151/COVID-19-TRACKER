package com.dev5151.covid_19info.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.airbnb.lottie.LottieAnimationView;
import com.dev5151.covid_19info.R;
public class MeasuresFragment extends Fragment {

    LottieAnimationView animationViewHome, animationViewWash, animationViewMask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_measures, container, false);
        initView(view);

        animationViewHome.playAnimation();
        animationViewWash.playAnimation();
        animationViewMask.playAnimation();

        return view;
    }

    private void initView(View view) {
        animationViewHome = view.findViewById(R.id.animation_view_home);
        animationViewWash = view.findViewById(R.id.animation_view_wash);
        animationViewMask = view.findViewById(R.id.animation_view_mask);
    }

}
