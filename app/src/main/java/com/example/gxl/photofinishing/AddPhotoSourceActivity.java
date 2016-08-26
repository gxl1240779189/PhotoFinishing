package com.example.gxl.photofinishing;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhy.autolayout.AutoLayoutActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import adapter.AddPhotoSourceListviewAdapter;
import adapter.ManagePhotoSourceListviewAdapter;
import bean.PhotoSourceBean;
import data.needMoveFile;
import de.greenrobot.event.EventBus;
import eventbustype.AddPhotoSourceEventType;
import eventbustype.FirstEventType;
import utils.DbUtils;

/**
 * 添加照片来源的Activity
 */
public class AddPhotoSourceActivity extends AutoLayoutActivity implements View.OnClickListener {
    RelativeLayout wancheng;
    RelativeLayout fanhui;
    ListView listview;
    ProgressBar progressBar;
    TextView ShowPhotoSourceDetail;

    ArrayList<String> AddPhotoSourceList = new ArrayList<String>();
    List<PhotoSourceBean> filedizhi = new ArrayList<PhotoSourceBean>();
    AddPhotoSourceListviewAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//用来取消状态栏
        setContentView(R.layout.tianjiamenu_layout);
        wancheng = (RelativeLayout) findViewById(R.id.wancheng);
        fanhui = (RelativeLayout) findViewById(R.id.fanhui);
        progressBar = (ProgressBar) findViewById(R.id.Progress);
        ShowPhotoSourceDetail = (TextView) findViewById(R.id.ShowPhotoSourceDetail);
        wancheng.setOnClickListener(this);
        fanhui.setOnClickListener(this);
        listview = (ListView) findViewById(R.id.showphoto_listview);
        filedizhi = DbUtils.GetPhotoSourceList();
        new GetPhotoSourceTask().execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wancheng:
                if (needMoveFile.getWillAddPhotoSource() != null && needMoveFile.getWillAddPhotoSource().size() != 0) {
                    EventBus.getDefault().post(new AddPhotoSourceEventType(needMoveFile.getWillAddPhotoSource()));
                }
                needMoveFile.RemoveAllWillAddPhotoSource();
                finish();
                break;

            case R.id.fanhui:
                needMoveFile.RemoveAllWillAddPhotoSource();
                finish();
                break;
        }
    }


    /**
     * 获取手机中存在图片的文件夹
     */
    public class GetPhotoSourceTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new AddPhotoSourceListviewAdapter(AddPhotoSourceActivity.this, AddPhotoSourceList);
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (needMoveFile.IsWillAddPhotoSource(AddPhotoSourceList.get(position))) {
                        view.findViewById(position).setBackgroundResource(R.drawable.check_unchoose);
                        needMoveFile.RemoveWillAddPhotoSource(AddPhotoSourceList.get(position));
                    } else {
                        view.findViewById(position).setBackgroundResource(R.drawable.check_choose);
                        needMoveFile.AddWillAddPhotoSource(AddPhotoSourceList.get(position));
                    }
                }
            });
            progressBar.setVisibility(View.GONE);
            ShowPhotoSourceDetail.setText("已找到" + AddPhotoSourceList.size() + "个结果");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver mContentResolver = AddPhotoSourceActivity.this.getContentResolver();

            //只查询jpeg和png的图片
            Cursor mCursor = mContentResolver.query(mImageUri, null,
                    MediaStore.Images.Media.MIME_TYPE + "=? or "
                            + MediaStore.Images.Media.MIME_TYPE + "=?",
                    new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);

            if (mCursor != null) {
                while (mCursor.moveToNext()) {
                    //获取图片的路径
                    String path = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));

                    //获取该图片的父路径名
                    String parentName = new File(path).getParentFile().getAbsolutePath();


                    //根据父路径名将图片放入到mGruopMap中
                    if (!AddPhotoSourceList.contains(parentName)) {
                        if (!IsExist(filedizhi, parentName)) {
                            AddPhotoSourceList.add(parentName);
                        }
                    }
                }
            }
            return null;
        }

    }

    /**
     * 判断当前地址是否已经加到选择的相片源
     *
     * @param filedizhi
     * @param parentName
     * @return
     */
    public Boolean IsExist(List<PhotoSourceBean> filedizhi, String parentName) {
        for (PhotoSourceBean item :
                filedizhi) {
            if (item.getSourcePath().equals(parentName)) {
                return true;
            }
        }
        if (parentName.contains("/storage/emulated/0/0FunPic")) {
            return true;
        }
        return false;
    }
}