package com.example.gxl.photofinishing;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zhy.autolayout.AutoLayoutActivity;

import java.io.File;
import java.util.ArrayList;

import adapter.guanlimenu_adapter;
import data.shezhiSharedprefrence;

/**
 * Created by Administrator on 2016/5/4 0004.
 */

public class guanlimenuActivity extends AutoLayoutActivity implements View.OnClickListener {
    RelativeLayout fanhui;
    RelativeLayout tianjia;
    ListView listview;
    ArrayList<File> filedizhi = new ArrayList<File>();
    shezhiSharedprefrence shezhisp;
    int gaibian=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//用来取消状态栏
        setContentView(R.layout.guanlimenu_layout);
        fanhui = (RelativeLayout) findViewById(R.id.fanhui);
        tianjia = (RelativeLayout) findViewById(R.id.tianjia);
        fanhui.setOnClickListener(this);
        tianjia.setOnClickListener(this);
        listview = (ListView) findViewById(R.id.showphoto_listview);
        shezhisp = new shezhiSharedprefrence(guanlimenuActivity.this);
        filedizhi = (ArrayList<File>) shezhisp.returnhistroyFile();
        guanlimenu_adapter adapter = new guanlimenu_adapter(guanlimenuActivity.this, filedizhi);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gaibian=2;
                Toast.makeText(guanlimenuActivity.this, filedizhi.get(position).getAbsolutePath(), Toast.LENGTH_SHORT).show();
                if (shezhisp.isExist(filedizhi.get(position).getAbsolutePath())) {
                    view.findViewById(position).setBackgroundResource(R.drawable.check_unchoose);
                    shezhisp.delete(filedizhi.get(position).getAbsolutePath());
                } else {
                    view.findViewById(position).setBackgroundResource(R.drawable.check_choose);
                    shezhisp.save(filedizhi.get(position).getAbsolutePath());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fanhui:
                Intent intent = new Intent();
                setResult(gaibian, intent);
                finish();
                break;

            case R.id.tianjia:
                Intent tianjiaintent=new Intent(guanlimenuActivity.this,tianjiamenuActivity.class);
                startActivityForResult(tianjiaintent,1);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
                Intent intent=new Intent();
                setResult(gaibian,intent);
                finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
             if (resultCode == 2) {
                 gaibian=2;
                 filedizhi = (ArrayList<File>) shezhisp.returnhistroyFile();
                 guanlimenu_adapter adapter = new guanlimenu_adapter(guanlimenuActivity.this, filedizhi);
                 listview.setAdapter(adapter);
                }
                break;
        }
    }
}
