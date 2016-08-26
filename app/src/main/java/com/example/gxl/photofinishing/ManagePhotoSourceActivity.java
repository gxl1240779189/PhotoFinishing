package com.example.gxl.photofinishing;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;
import java.util.List;

import adapter.ManagePhotoSourceListviewAdapter;
import bean.PhotoSourceBean;
import data.needMoveFile;
import de.greenrobot.event.EventBus;
import eventbustype.AddPhotoSourceEventType;
import eventbustype.FirstEventType;
import eventbustype.TestEventType;
import utils.DbUtils;

/**
 * 修改照片软件来源的Activity
 */

public class ManagePhotoSourceActivity extends AutoLayoutActivity implements View.OnClickListener {
    RelativeLayout fanhui;
    RelativeLayout tianjia;
    ListView listview;
    List<PhotoSourceBean> filedizhi = new ArrayList<PhotoSourceBean>();
    ManagePhotoSourceListviewAdapter adapter;
    int gaibian = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//用来取消状态栏
        setContentView(R.layout.guanlimenu_layout);
        EventBus.getDefault().register(this);
        fanhui = (RelativeLayout) findViewById(R.id.fanhui);
        tianjia = (RelativeLayout) findViewById(R.id.tianjia);
        fanhui.setOnClickListener(this);
        tianjia.setOnClickListener(this);
        listview = (ListView) findViewById(R.id.showphoto_listview);
        filedizhi = DbUtils.GetPhotoSourceList();
        adapter = new ManagePhotoSourceListviewAdapter(ManagePhotoSourceActivity.this, filedizhi);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gaibian=1;
                if (filedizhi.get(position).getChooseState()) {
                    view.findViewById(position).setBackgroundResource(R.drawable.check_unchoose);
                    DbUtils.ChangePhotoSourceState(false, filedizhi.get(position).getSourcePath());
                    filedizhi.get(position).setChooseState(false);
                } else {
                    view.findViewById(position).setBackgroundResource(R.drawable.check_choose);
                    DbUtils.ChangePhotoSourceState(true, filedizhi.get(position).getSourcePath());
                    filedizhi.get(position).setChooseState(true);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fanhui:
                EventBus.getDefault().post(new FirstEventType(gaibian));
                finish();
                break;

            case R.id.tianjia:
                Intent tianjiaintent = new Intent(ManagePhotoSourceActivity.this, AddPhotoSourceActivity.class);
                startActivityForResult(tianjiaintent, 1);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            EventBus.getDefault().post(new FirstEventType(gaibian));
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * EventBus的事件处理函数
     *
     * @param event
     */
    public void onEventMainThread(AddPhotoSourceEventType event) {
        List<String> list = event.getList();
        for (String item :
                list) {
            PhotoSourceBean photoSourceBean = new PhotoSourceBean();
            photoSourceBean.setSourcePath(item);
            photoSourceBean.setChooseState(true);
            if (!DbUtils.IsExist(item)) {
                photoSourceBean.save();
            }
            filedizhi.add(photoSourceBean);
        }
        adapter.notifyDataSetChanged();
        gaibian=1;
    }

}
