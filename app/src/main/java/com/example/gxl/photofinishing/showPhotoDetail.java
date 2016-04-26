package com.example.gxl.photofinishing;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.autolayout.AutoLayoutActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.LogRecord;

import javax.security.auth.login.LoginException;

import Adapter.showPhotoDetailGridviewAdapter;
import Adapter.showphoto_listviewAdapter;
import Data.needMoveFile;
import Utils.fileUtils;
import myapplication.myApplication;

/**
 * Created by gxl on 2016/4/15.
 */
public class showPhotoDetail extends AutoLayoutActivity implements View.OnClickListener{
    String filepath;
    GridView gridView;
    showPhotoDetailGridviewAdapter adapter;
    int Type_delete=0;
    int Type_show=1;
    int flag=Type_show;
    RelativeLayout fanhui;
    TextView xuanze_text;
    EditText show_title;
    RelativeLayout delete;
    ArrayList<String> filepathlist;

    int Type_fanhuidaoyuanchu=0;
    int Type_zhijiedelete=1;
    int flag_checkbox=Type_zhijiedelete;
    String first_title;
    Boolean isFocus;

    int xiugai_flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//用来取消状态栏
        setContentView(R.layout.showphoto_detail);
        fanhui = (RelativeLayout) findViewById(R.id.fanhui);
        xuanze_text = (TextView) findViewById(R.id.xuanze_text);
        show_title = (EditText) findViewById(R.id.show_title);
        delete = (RelativeLayout) findViewById(R.id.delete);
        delete.setOnClickListener(this);
        fanhui.setOnClickListener(this);
        gridView= (GridView) findViewById(R.id.gridview);
        filepath=getIntent().getStringExtra("filepath");
        filepathlist=fileUtils.getExistImageList(filepath);
        show_title.setText(filepath.substring(filepath.lastIndexOf("/") + 1, filepath.length()));
        first_title=filepath.substring(filepath.lastIndexOf("/") + 1, filepath.length());
        adapter =new showPhotoDetailGridviewAdapter(showPhotoDetail.this, filepathlist);
        gridView.setAdapter(adapter);
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                flag = Type_delete;
                chagetodelete_jiemian();
                return false;
            }
        });

        show_title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    show_title.setFocusable(true);
                    isFocus = true;
                    Log.i("path", isFocus + "");
                } else {
                    isFocus = false;
                    Log.i("path", isFocus + "");
                }
            }
        });



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (flag == Type_delete) {
                    if (!needMoveFile.isinNeeddeletePhoto(filepathlist.get(position))) {
                        view.findViewById(R.id.ImageView_tip).setVisibility(
                                View.VISIBLE);
                        needMoveFile.addNeeddeletePhoto(filepathlist.get(position));
                    } else {
                        view.findViewById(R.id.ImageView_tip).setVisibility(
                                View.GONE);
                        needMoveFile.removedeletePhoto(filepathlist.get(position));
                    }
                }
//                else {
//                    Intent intent = new Intent(showPhotoDetail.this, test.class);
//                    intent.putStringArrayListExtra("listfilepath", filepathlist);
//                    intent.putExtra("position", position);
//                    startActivity(intent);
//                }
            }
        });
    }
    void chagetodelete_jiemian() {
        fanhui.setVisibility(View.VISIBLE);
        xuanze_text.setVisibility(View.VISIBLE);
        delete.setVisibility(View.VISIBLE);
        show_title.setVisibility(View.GONE);
    }

    void chagetonormal_jiemian() {
        fanhui.setVisibility(View.GONE);
        xuanze_text.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
        show_title.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (flag==Type_delete) {
                chagetonormal_jiemian();
                flag = Type_show;
                adapter =new showPhotoDetailGridviewAdapter(showPhotoDetail.this, filepathlist);
                gridView.setAdapter(adapter);
                needMoveFile.removealldeletePhoto();
            } else {
                Intent intent=new Intent();
                if(!first_title.equals(show_title.getText().toString()))
                {
                    Toast.makeText(showPhotoDetail.this,"修改目录",Toast.LENGTH_LONG).show();
                    renameToNewFile(show_title.getText().toString());
                    setResult(1, intent);
                }else if(xiugai_flag==1)
                {
                   setResult(2,intent);
                }else
                {
                    setResult(0,intent);
                }


                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private boolean renameToNewFile(String path)
    {
        File srcDir = new File(Environment.getExternalStorageDirectory().getPath() +  myApplication.move_file_path+"/"+first_title);  //就文件夹路径
        boolean isOk = srcDir.renameTo(new File(Environment.getExternalStorageDirectory().getPath() +  myApplication.move_file_path+"/"+path));  //dest新文件夹路径，通过renameto修改
        System.out.println("renameToNewFile is OK ? :" +isOk);
        return isOk;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fanhui:
                chagetonormal_jiemian();
                flag = Type_show;
                adapter =new showPhotoDetailGridviewAdapter(showPhotoDetail.this, fileUtils.getExistImageList(filepath));
                gridView.setAdapter(adapter);
                needMoveFile.removealldeletePhoto();
                break;

            case R.id.delete:
                if (needMoveFile.needdeletePhoto.size() != 0) {
                    Createdialog();
                } else {
                    Toast.makeText(showPhotoDetail.this, "请先选择删除项", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
    public void Createdialog() {
        final View dialogView = LayoutInflater.from(showPhotoDetail.this).inflate(R.layout.delete_dialog, null);
        final Dialog dialog = new Dialog(showPhotoDetail.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth());
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setContentView(dialogView);
        final RelativeLayout checkbox = (RelativeLayout) dialogView.findViewById(R.id.checkbox);
        RelativeLayout quxiao = (RelativeLayout) dialogView.findViewById(R.id.quxiao);
        RelativeLayout queding= (RelativeLayout) dialogView.findViewById(R.id.queding);
        TextView delete_text= (TextView) dialogView.findViewById(R.id.shanchutext);
        delete_text.setText("确定删除"+needMoveFile.needdeletePhoto.size()+"个文件?");
        final ImageView check_image= (ImageView)dialogView.findViewById(R.id.checkbox_image);

        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag_checkbox==Type_zhijiedelete)
                {
                    flag_checkbox=Type_fanhuidaoyuanchu;
                    check_image.setBackgroundResource(R.drawable.check_choose);
                }else
                {
                    flag_checkbox=Type_zhijiedelete;
                    check_image.setBackgroundResource(R.drawable.check_unchoose);
                }
            }
        });
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
                flag=Type_show;
                if(flag_checkbox==Type_zhijiedelete) {
                    Log.i("path","直接删除");
                    fileUtils.deleteFilelist(needMoveFile.getdeletePhoto());
                }else
                {
                    Log.i("path","返回到远处");
                    fileUtils.movePhotolist(needMoveFile.getdeletePhoto());
                }
                for(int i=0;i<needMoveFile.getdeletePhoto().size();i++)
                {
                    filepathlist.remove(needMoveFile.getdeletePhoto().get(i));
                }
                adapter =new showPhotoDetailGridviewAdapter(showPhotoDetail.this, filepathlist);
                gridView.setAdapter(adapter);
                needMoveFile.removealldeletePhoto();
                chagetonormal_jiemian();
                xiugai_flag=1;
            }
        });
        dialog.show();
    }


}
