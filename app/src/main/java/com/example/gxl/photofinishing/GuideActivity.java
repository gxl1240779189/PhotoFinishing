package com.example.gxl.photofinishing;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.activeandroid.ActiveAndroid;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import adapter.GuideViewpaper_Adapter;
import application.MyApplication;
import bean.PhotoSourceBean;
import fragment.GuideOneFragment;
import fragment.GuideThreeFragment;
import android.support.v4.app.FragmentManager;
import fragment.GuideTwoFragment;

/**
 * 第一次进来指导用户操作的ViewPaper
 */
public class GuideActivity extends FragmentActivity {
    private ViewPager viewPager;
    private List<Fragment> list=new ArrayList<>();
    private GuideOneFragment guideOneFragment;
    private GuideTwoFragment guideTwoFragment;
    private GuideThreeFragment guideThreeFragment;
    private GuideViewpaper_Adapter adapter;

    private Button btn_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.guide_layout);
        btn_login=(Button) findViewById(R.id.btn_login);
        viewPager=(ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(3);
        if(guideOneFragment==null){
            guideOneFragment=new GuideOneFragment();
            list.add(guideOneFragment);
        }
        if(guideTwoFragment==null){
            guideTwoFragment=new GuideTwoFragment();
            list.add(guideTwoFragment);
        }
        if(guideThreeFragment==null){
            guideThreeFragment=new GuideThreeFragment();
            list.add(guideThreeFragment);
        }
        adapter=new GuideViewpaper_Adapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                if (arg0 == 2) {
                    btn_login.setVisibility(View.VISIBLE);
                } else {
                    btn_login.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent HomeIntent=new Intent(GuideActivity.this,LoadPhotoToViewActivity.class);
                startActivity(HomeIntent);
                finish();
            }
        });
//        Intent HomeIntent=new Intent(GuideActivity.this,LoadPhotoToViewActivity.class);
//        startActivity(HomeIntent);
        InitPhotoSource();
//        finish();
    }

    /**
     * 初始化默认的相册源地址
     */
    public void InitPhotoSource() {
        ArrayList<File> PhotoSourceList = null;
        try {
            PhotoSourceList = MyApplication.getPhoto_yuan();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        ActiveAndroid.beginTransaction();
        try {
            for (File file :
                    PhotoSourceList) {
                PhotoSourceBean item = new PhotoSourceBean();
                item.setSourcePath(file.getAbsolutePath());
                item.setChooseState(true);
                item.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }
}
