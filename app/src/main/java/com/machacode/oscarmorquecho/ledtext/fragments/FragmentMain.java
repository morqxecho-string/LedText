package com.machacode.oscarmorquecho.ledtext.fragments;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.machacode.oscarmorquecho.ledtext.R;
import com.machacode.oscarmorquecho.ledtext.utils.ScrollTextView;

import java.util.Objects;

public class FragmentMain extends Fragment implements Animation.AnimationListener{

    public FragmentMain() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initAll(view);
        return view;
    }

    public void initAll(View view){
        Activity activity = getActivity();
        if (activity != null) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        Display display = Objects.requireNonNull(activity).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        ScrollTextView tvTextDisplayed = view.findViewById(R.id.tvTextDisplayed);

        Animation animBlink = AnimationUtils.loadAnimation(activity, R.anim.blink);

        tvTextDisplayed.setText("Holaaaaaaaaaaaaa");
        tvTextDisplayed.setHeight((size.y / 10) * 4);
        float TEXT_SIZE = 20;
        tvTextDisplayed.setTextSize(TEXT_SIZE);
        tvTextDisplayed.startScroll();

        animBlink.setAnimationListener(this);
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        // if (animation == animBlink) { }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            //Restore the fragment's state here
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);


        //Save the fragment's state here
    }
}
