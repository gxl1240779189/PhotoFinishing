package com.example.gxl.photofinishing;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

import adapter.GuideViewpaper;

/**
 * Created by gxl on 2016/4/3.
 */
public class GuideActivity extends Activity {
    List<View> views = new ArrayList<View>();
    ViewPager viewpaper;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.guide_layout);
        viewpaper = (ViewPager) findViewById(R.id.viewPager);
        LayoutInflater layoutInflater = LayoutInflater.from(GuideActivity.this);
        views.add(layoutInflater.inflate(R.layout.guide1, null));
        views.add(layoutInflater.inflate(R.layout.guide2, null));
        views.add(layoutInflater.inflate(R.layout.guide3, null));
        GuideViewpaper vp = new GuideViewpaper(views);
        viewpaper.setAdapter(vp);
    }
}
