package com.example.gxl.photofinishing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.zhy.autolayout.AutoLayoutActivity;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import adapter.guanlimenu_adapter;
import application.MyApplication;
import data.shezhiSharedprefrence;

/**
 * Created by Administrator on 2016/5/5 0005.
 */
public class AddPhotoSourceActivity extends AutoLayoutActivity implements View.OnClickListener {
    RelativeLayout wancheng;
    RelativeLayout fanhui;
    ListView listview;
    ArrayList<File> filedizhi = new ArrayList<File>();

    ArrayList<File> yuanlaidizhi=new ArrayList<File>();
    shezhiSharedprefrence shezhisp;
    int gaibian=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//用来取消状态栏
        setContentView(R.layout.tianjiamenu_layout);
        wancheng = (RelativeLayout) findViewById(R.id.wancheng);
        fanhui = (RelativeLayout) findViewById(R.id.fanhui);
        wancheng.setOnClickListener(this);
        fanhui.setOnClickListener(this);
        listview = (ListView) findViewById(R.id.showphoto_listview);
        shezhisp = new shezhiSharedprefrence(AddPhotoSourceActivity.this);
        filedizhi = (ArrayList<File>) shezhisp.returnhistroyFile();
        try {
            yuanlaidizhi= MyApplication.getPhoto_yuan();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if(new File("/storage/emulated/0/Pictures").exists())
        {
            yuanlaidizhi.add(new File("/storage/emulated/0/Pictures"));
        }
        if(new File("/storage/emulated/0/Snapseed").exists())
        {
            yuanlaidizhi.add(new File("/Storage/emulated/0/Snapseed"));
        }
        for (File file:
                filedizhi) {
            if(yuanlaidizhi.contains(file))
            {
                yuanlaidizhi.remove(file);
            }
        }
        if(yuanlaidizhi.size()!=0) {
            guanlimenu_adapter adapter = new guanlimenu_adapter(AddPhotoSourceActivity.this, yuanlaidizhi);
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (shezhisp.isExist(yuanlaidizhi.get(position).getAbsolutePath())) {
                        view.findViewById(position).setBackgroundResource(R.drawable.check_unchoose);
                        shezhisp.delete(yuanlaidizhi.get(position).getAbsolutePath());
                    } else {
                        view.findViewById(position).setBackgroundResource(R.drawable.check_choose);
                        shezhisp.save(yuanlaidizhi.get(position).getAbsolutePath());
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wancheng:
                Intent intent = new Intent();
                setResult(2, intent);
                finish();
                break;

            case R.id.fanhui:
                finish();
                break;
        }
    }


}
