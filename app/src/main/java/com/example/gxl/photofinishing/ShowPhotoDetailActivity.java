package com.example.gxl.photofinishing;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;

import adapter.showPhotoDetailGridviewAdapter;
import data.needMoveFile;
import de.greenrobot.event.EventBus;
import eventbustype.ChaKanPhotoType;
import eventbustype.FirstEventType;
import eventbustype.ShowPhotoDetailType;
import utils.fileUtils;
import customview.DialogBuilder;

/**
 * Created by gxl on 2016/4/15.
 */
public class ShowPhotoDetailActivity extends AutoLayoutActivity implements View.OnClickListener{
    String filepath;
    GridView gridView;
    showPhotoDetailGridviewAdapter adapter;
    int Type_delete=0;
    int Type_show=1;
    int flag=Type_show;


    RelativeLayout fanhui;//返回
    RelativeLayout xuanze;//选择
    RelativeLayout quxiao;//取消
    RelativeLayout quanxuan;//全选
    RelativeLayout show_move_detail;
    TextView quanxuan_text;
    TextView huanyuan_textview;
    TextView shanchu_textview;


    TextView xuanze_text;
    TextView show_title;
    RelativeLayout delete;
    ArrayList<String> filepathlist;

    String first_title;
    Boolean isFocus;

    int xiugai_flag=0;
    int quanxuan_flag = 0;//当前是否全选

    int delete_flag=4;
    int huanyuan_flag=5;


    int Type_zhijiedelete = 1;
    int Type_huanyuan=2;
    int caozuo_flag = Type_zhijiedelete;

    private final int ReturnDelete=1;
    private final int ReturnRestore=2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//用来取消状态栏
        EventBus.getDefault().register(this);
        setContentView(R.layout.showphoto_detail);
        fanhui = (RelativeLayout) findViewById(R.id.fanhui);
        xuanze= (RelativeLayout) findViewById(R.id.xuanze);
        show_move_detail= (RelativeLayout) findViewById(R.id.show_move_detail);
        huanyuan_textview= (TextView) findViewById(R.id.huanyuan);
        shanchu_textview= (TextView) findViewById(R.id.delete);
        quxiao = (RelativeLayout) findViewById(R.id.quxiao);
        quanxuan = (RelativeLayout) findViewById(R.id.quanxuan);
        quanxuan_text= (TextView) findViewById(R.id.quanxuan_text);
        TextView show_title= (TextView) findViewById(R.id.show_title);

        fanhui.setOnClickListener(this);
        xuanze.setOnClickListener(this);
        quxiao.setOnClickListener(this);
        quanxuan.setOnClickListener(this);
        huanyuan_textview.setOnClickListener(this);
        shanchu_textview.setOnClickListener(this);

        gridView= (GridView) findViewById(R.id.gridview);
        filepath=getIntent().getStringExtra("filepath");
        filepathlist=fileUtils.getExistImageList(filepath);
        show_title.setText(filepath.substring(filepath.lastIndexOf("/") + 1, filepath.length()));
        adapter =new showPhotoDetailGridviewAdapter(ShowPhotoDetailActivity.this, filepathlist);
        gridView.setAdapter(adapter);
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                flag = Type_delete;
                chagetodelete_jiemian();
                return false;
            }
        });


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (flag == Type_delete) {
                    if (!needMoveFile.isinNeeddeletePhoto(filepathlist.get(position))) {
                        view.findViewById(position).setVisibility(
                                View.VISIBLE);
                        needMoveFile.addNeeddeletePhoto(filepathlist.get(position));
                    } else {
                        view.findViewById(position).setVisibility(
                                View.GONE);
                        needMoveFile.removedeletePhoto(filepathlist.get(position));
                    }
                }else
                {
                    Intent intent = new Intent(ShowPhotoDetailActivity.this, ArrangePhotoViewerActivity.class);
                    intent.putStringArrayListExtra("image_urls", filepathlist);
                    intent.putExtra("image_index", position);
                    startActivityForResult(intent, 1);
                }
            }
        });
    }
    void chagetodelete_jiemian() {
        show_move_detail.setVisibility(View.VISIBLE);
        fanhui.setVisibility(View.GONE);
        xuanze.setVisibility(View.GONE);
        quxiao.setVisibility(View.VISIBLE);
        quanxuan.setVisibility(View.VISIBLE);
        flag=Type_delete;
    }

    void chagetonormal_jiemian() {
        show_move_detail.setVisibility(View.GONE);
        fanhui.setVisibility(View.VISIBLE);
        xuanze.setVisibility(View.VISIBLE);
        quxiao.setVisibility(View.GONE);
        quanxuan.setVisibility(View.GONE);
        flag=Type_show;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (flag==Type_delete) {
                chagetonormal_jiemian();
                flag = Type_show;
                for (int i = gridView.getFirstVisiblePosition(); i <= gridView.getLastVisiblePosition(); i++) {
                    gridView.findViewById(i).setVisibility(
                            View.GONE);
                }
                needMoveFile.removealldeletePhoto();
            } else {
                Intent intent=new Intent();
                setResult(xiugai_flag,intent);
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fanhui:
                finish();
                break;
            case R.id.xuanze:
                chagetodelete_jiemian();
                flag=Type_delete;
                break;

            case R.id.quxiao:
                chagetonormal_jiemian();
                for (int i = gridView.getFirstVisiblePosition(); i <= gridView.getLastVisiblePosition(); i++) {
                    gridView.findViewById(i).setVisibility(
                            View.GONE);
                }
                needMoveFile.removealldeletePhoto();
                flag=Type_show;
                break;
            case R.id.quanxuan:
                if (quanxuan_flag == 0) {
                    quanxuan_text.setText("全不选");
                    quanxuan_flag = 1;
                    for (int i = gridView.getFirstVisiblePosition(); i <= gridView.getLastVisiblePosition(); i++) {
                        gridView.findViewById(i).setVisibility(
                                View.VISIBLE);
                    }
                    for (int i=0;i<filepathlist.size();i++)
                    {
                        needMoveFile.addNeeddeletePhoto(filepathlist.get(i));
                    }
                } else {
                    quanxuan_text.setText("全选");
                    quanxuan_flag = 0;
                    for (int i = gridView.getFirstVisiblePosition(); i <= gridView.getLastVisiblePosition(); i++) {
                        gridView.findViewById(i).setVisibility(
                                View.GONE);
                    }
                    needMoveFile.removealldeletePhoto();
                }
                break;

            case R.id.huanyuan:
                if(needMoveFile.getdeletePhoto().size()!=0) {
                    caozuo_flag=Type_huanyuan;
                    Createdialog(huanyuan_flag);
                }else
                {
                    Toast.makeText(ShowPhotoDetailActivity.this,"请至少选择一项",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.delete:
                if(needMoveFile.getdeletePhoto().size()!=0) {
                    caozuo_flag = Type_zhijiedelete;
                    Createdialog(delete_flag);
                }else
                {
                    Toast.makeText(ShowPhotoDetailActivity.this,"请至少选择一项",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 弹出提示对话框
     */
    public void Createdialog(int view_flag) {
        final View dialogView = LayoutInflater.from(ShowPhotoDetailActivity.this).inflate(R.layout.delete_dialog, null);
        final Dialog dialog = new Dialog(ShowPhotoDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth());
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setContentView(dialogView);
        RelativeLayout quxiao = (RelativeLayout) dialogView.findViewById(R.id.quxiao);
        RelativeLayout queding = (RelativeLayout) dialogView.findViewById(R.id.queding);
        TextView dialog_title= (TextView) dialogView.findViewById(R.id.dialog_title);
        TextView delete_text = (TextView) dialogView.findViewById(R.id.shanchutext);
        dialog_title.setText("确认删除");
        delete_text.setText("删除的照片将不能再次还原");
        if(view_flag==huanyuan_flag)
        {
            dialog_title.setText("确认还原");
            delete_text.setText("还原后照片将会回到原位置");
        }
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (flag == Type_zhijiedelete) {
                    Log.i("path", "直接删除");
                    new deletefile_task().execute();
                    EventBus.getDefault().post(new ShowPhotoDetailType(Type_zhijiedelete));
                } else {
                    Log.i("path", "返回到远处");
                    new huanyuanfile_task().execute();
                    EventBus.getDefault().post(new ShowPhotoDetailType(Type_huanyuan));
                }
            }
        });
        dialog.show();
    }

    /**
     * 将整理好的照片还原到原来的文件夹中
     */
    class huanyuanfile_task extends AsyncTask<Void, Void, Void> {
        Dialog MyDialog;
        Dialog finishDialog;

        @Override
        protected void onPreExecute() {
            MyDialog = DialogBuilder.createLoadingDialog(ShowPhotoDetailActivity.this, "正在还原照片");
            finishDialog = DialogBuilder.createLoadingfinishDialog(ShowPhotoDetailActivity.this, "已完成");
            MyDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            fileUtils.movePhotolist(needMoveFile.getdeletePhoto());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            MyDialog.dismiss();
            finishDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finishDialog.dismiss();
                }
            }, 500);
            for (int i = 0; i < needMoveFile.getdeletePhoto().size(); i++) {
                filepathlist.remove(needMoveFile.getdeletePhoto().get(i));
            }
            needMoveFile.removealldeletePhoto();
            adapter.notifyDataSetChanged();
            chagetonormal_jiemian();
            xiugai_flag=2;
        }
    }

    /**
     * 将整理好的文件夹直接删除
     */
    class deletefile_task extends AsyncTask<Void, Void, Void> {

        Dialog MyDialog;
        Dialog finishDialog;

        @Override
        protected void onPreExecute() {
            MyDialog = DialogBuilder.createLoadingDialog(ShowPhotoDetailActivity.this, "正在删除照片");
            finishDialog = DialogBuilder.createLoadingfinishDialog(ShowPhotoDetailActivity.this, "已完成");
            MyDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            fileUtils.deleteFilelist(needMoveFile.getdeletePhoto());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            MyDialog.dismiss();
            finishDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finishDialog.dismiss();
                }
            }, 500);
            for (int i = 0; i < needMoveFile.getdeletePhoto().size(); i++) {
                filepathlist.remove(needMoveFile.getdeletePhoto().get(i));
            }
            needMoveFile.removealldeletePhoto();
            adapter.notifyDataSetChanged();
            chagetonormal_jiemian();
            xiugai_flag=1;
        }
    }

    /**
     * EventBus的事件处理函数
     *
     * @param event
     *     private final int ReturnDelete=1;
    private final int ReturnRestore=2;
     */
    public void onEventMainThread(ChaKanPhotoType event) {
        switch (event.getmFlag()) {
            case ReturnDelete:
                filepathlist=fileUtils.getExistImageList(filepath);
                adapter =new showPhotoDetailGridviewAdapter(ShowPhotoDetailActivity.this, filepathlist);
                gridView.setAdapter(adapter);
                break;
            case ReturnRestore:
                filepathlist=fileUtils.getExistImageList(filepath);
                adapter =new showPhotoDetailGridviewAdapter(ShowPhotoDetailActivity.this, filepathlist);
                gridView.setAdapter(adapter);
                EventBus.getDefault().post(new FirstEventType(1));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       switch (requestCode)
       {
           case 1:
               if(resultCode==1)
               {
                   filepathlist=fileUtils.getExistImageList(filepath);
                   adapter =new showPhotoDetailGridviewAdapter(ShowPhotoDetailActivity.this, filepathlist);
                   gridView.setAdapter(adapter);
                   xiugai_flag=2;
               }
               if(resultCode==2)
               {
                   filepathlist=fileUtils.getExistImageList(filepath);
                   adapter =new showPhotoDetailGridviewAdapter(ShowPhotoDetailActivity.this, filepathlist);
                   gridView.setAdapter(adapter);
                   xiugai_flag=1;
               }
               break;
       }
    }
}
