package com.example.gxl.photofinishing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.zhy.autolayout.AutoLayoutActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import Adapter.showphoto_listviewAdapter;
import Data.needMoveFile;
import Utils.fileUtils;
import myapplication.myApplication;

public class showPhoto extends AutoLayoutActivity implements View.OnClickListener {
    private ArrayList<String> filepathlist;
    private ListView showphoto_listview;
    private showphoto_listviewAdapter adapter;
    int delete_type = 0;
    RelativeLayout fanhui;
    TextView xuanze_text;
    TextView show_title;
    RelativeLayout delete;
    RelativeLayout wancheng;
    int gaibian_flag=0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    int Type_fanhuidaoyuanchu=0;
    int Type_zhijiedelete=1;
    int flag=Type_zhijiedelete;

    int qidong_flag=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//用来取消状态栏
        setContentView(R.layout.showphoto_listviewlayout);
        qidong_flag = getIntent().getIntExtra("flag", 0);
        fanhui = (RelativeLayout) findViewById(R.id.fanhui);
        wancheng = (RelativeLayout) findViewById(R.id.wancheng);
        xuanze_text = (TextView) findViewById(R.id.xuanze_text);
        show_title = (TextView) findViewById(R.id.show_title);
        delete = (RelativeLayout) findViewById(R.id.delete);
        showphoto_listview = (ListView) findViewById(R.id.showphoto_listview);
        fanhui.setOnClickListener(this);
        delete.setOnClickListener(this);
        wancheng.setOnClickListener(this);
        filepathlist = fileUtils.getExistFileList(Environment.getExternalStorageDirectory().getPath() + myApplication.move_file_path);
        if (qidong_flag == 1) {
            chagetomovefile_jiemian();
            adapter = new showphoto_listviewAdapter(showPhoto.this, filepathlist, 3);
            showphoto_listview.setAdapter(adapter);
            showphoto_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(needMoveFile.choose_item==-1)
                    {
                        needMoveFile.choose_item=position;
                        view.findViewById(position).setBackgroundResource(R.drawable.check_choose);
                    }else if(needMoveFile.choose_item==position)
                    {
                        needMoveFile.choose_item=-1;
                        view.findViewById(position).setBackgroundResource(R.drawable.check_unchoose);
                    }else
                    {
                        view.findViewById(position).setBackgroundResource(R.drawable.check_choose);
                        if(((parent.getFirstVisiblePosition()-1)<needMoveFile.choose_item)&&((parent.getLastVisiblePosition()+1)>needMoveFile.choose_item)) {
                            findViewById(needMoveFile.choose_item).setBackgroundResource(R.drawable.check_unchoose);
                        }
                        needMoveFile.choose_item=position;
                    }
                }
            });
        } else {
            adapter = new showphoto_listviewAdapter(showPhoto.this, filepathlist, 0);
            showphoto_listview.setAdapter(adapter);
            showphoto_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (delete_type == 0) {
                        Intent intent = new Intent(showPhoto.this, showPhotoDetail.class);
                        intent.putExtra("filepath", filepathlist.get(position));
                        startActivityForResult(intent, 1);
                    } else {
                        if (!needMoveFile.isinNeeddeletefile(filepathlist.get(position))) {
                            findViewById(position).setBackgroundResource(R.drawable.check_choose);
                            needMoveFile.addNeeddeletefile(filepathlist.get(position));
                        } else {
                           findViewById(position).setBackgroundResource(R.drawable.check_unchoose);
                            needMoveFile.removedeletefile(filepathlist.get(position));
                        }
                    }
                }
            });
            showphoto_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    chagetodelete_jiemian();
                    delete_type = 1;
                    adapter = new showphoto_listviewAdapter(showPhoto.this, filepathlist, 1);
                    showphoto_listview.setAdapter(adapter);
                    return true;
                }
            });
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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

    void chagetomovefile_jiemian()
    {
        fanhui.setVisibility(View.VISIBLE);
        xuanze_text.setVisibility(View.VISIBLE);
        delete.setVisibility(View.GONE);
        wancheng.setVisibility(View.VISIBLE);
        show_title.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fanhui:
                if(qidong_flag==1)
                {
                    Intent intent=new Intent();
                    setResult(1,intent);
                    finish();
                }else {
                    chagetonormal_jiemian();
                    delete_type = 0;
                    adapter = new showphoto_listviewAdapter(showPhoto.this, filepathlist, 0);
                    showphoto_listview.setAdapter(adapter);
                    needMoveFile.removealldeletefile();
                }
                break;

            case R.id.delete:
                if (needMoveFile.needdeleteFile.size() != 0) {
                    Createdialog();
                } else {
                    Toast.makeText(showPhoto.this, "请先选择删除项", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.wancheng:
                if(needMoveFile.choose_item==-1)
                {
                    Toast.makeText(showPhoto.this,"请先选择需要移动到的文件夹!",Toast.LENGTH_LONG).show();
                }else
                {
                    new moveNeedfile_task().execute(filepathlist.get(needMoveFile.choose_item));
                    Intent intent=new Intent();
                    setResult(2,intent);
                    finish();
                }

                break;
        }
    }

    /**
     * 将选好的图片移动到指定的文件夹
     */
    class moveNeedfile_task extends AsyncTask<String,Void,Void>
    {
        ProgressDialog MyDialog;
        @Override
        protected void onPreExecute() {
            MyDialog = ProgressDialog.show(showPhoto.this, " " , "正在移动中", true);
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(String... params) {
            Log.i("gxl",params[0]);
            MoveNeedfile(params[0]);
            needMoveFile.choose_item=-1;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            MyDialog.dismiss();
            Intent intent=new Intent();
            setResult(gaibian_flag, intent);
            finish();
            super.onPostExecute(aVoid);
        }
    }

    /**
     * 将整理好的文件夹还原到原来的文件夹中
     */
    class huanyuanfile_task extends AsyncTask<Void,Void,Void>
    {
        ProgressDialog MyDialog;
        @Override
        protected void onPreExecute() {
            MyDialog = ProgressDialog.show(showPhoto.this, " " , " 正在还原中", true);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            fileUtils.moveFilelist(needMoveFile.getdeletefile());
            needMoveFile.removealldeletefile();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            MyDialog.dismiss();
        }

    }

    /**
     * 将整理好的文件夹直接删除
     */
    class deletefile_task extends AsyncTask<Void,Void,Void>
    {

        ProgressDialog MyDialog;
        @Override
        protected void onPreExecute() {
            MyDialog = ProgressDialog.show(showPhoto.this, " " , "正在删除中 ", true);
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            fileUtils.deleteFilelist(needMoveFile.getdeletefile());
            needMoveFile.removealldeletefile();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            MyDialog.dismiss();
        }
    }



    public void MoveNeedfile(final String path) {
        List<String> list = needMoveFile.getNeedmoveFile();
        final List<File> needmovelistfile = new ArrayList<File>();
        for (String string : list) {
            needmovelistfile.add(new File(string));
        }
        File app_dic = new File(Environment.getExternalStorageDirectory()
                .getPath() + myApplication.move_file_path);
        if (!app_dic.exists()) {
            app_dic.mkdir();
        }
        File movefile = new File(path);
        if (!movefile.exists()) {
            movefile.mkdir();
        }
        for (File file : needmovelistfile) {
            fileUtils.copyFile(file.getAbsolutePath(),path+ "/" + file.getName());
        }
        needMoveFile.removeall();
        needMoveFile.clearPositemap();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (delete_type == 1) {
                chagetonormal_jiemian();
                delete_type = 0;
                adapter = new showphoto_listviewAdapter(showPhoto.this, filepathlist, 0);
                showphoto_listview.setAdapter(adapter);
                needMoveFile.removealldeletefile();
            } else {
                Intent intent=new Intent();
                setResult(gaibian_flag,intent);
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void Createdialog() {
//        final AlertDialog.Builder customizeDialog = new AlertDialog.Builder(showPhoto.this);
        final View dialogView = LayoutInflater.from(showPhoto.this).inflate(R.layout.delete_dialog, null);
        final Dialog dialog = new Dialog(showPhoto.this);
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
        delete_text.setText("确定删除"+needMoveFile.needdeleteFile.size()+"个文件?");
        final ImageView check_image= (ImageView)dialogView.findViewById(R.id.checkbox_image);

        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag==Type_zhijiedelete)
                {
                    flag=Type_fanhuidaoyuanchu;
                    check_image.setBackgroundResource(R.drawable.check_choose);
                }else
                {
                    flag=Type_zhijiedelete;
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
                delete_type = 0;
                if(flag==Type_zhijiedelete) {
                    Log.i("path","直接删除");
                    new deletefile_task().execute();
                }else
                {
                    Log.i("path", "返回到远处");
                    new huanyuanfile_task().execute();
                    gaibian_flag=2;

                }
                for(int i=0;i<needMoveFile.getdeletefile().size();i++)
                {
                    filepathlist.remove(needMoveFile.getdeletefile().get(i));
                }
                adapter = new showphoto_listviewAdapter(showPhoto.this, filepathlist, 0);
                showphoto_listview.setAdapter(adapter);
                chagetonormal_jiemian();
            }
        });
        dialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "showPhoto Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.gxl.photofinishing/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "showPhoto Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.gxl.photofinishing/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      switch (requestCode)
      {
          case 1:
              if(resultCode==1)
              {
                  filepathlist = fileUtils.getExistFileList(Environment.getExternalStorageDirectory().getPath() + myApplication.move_file_path);
                  adapter = new showphoto_listviewAdapter(showPhoto.this, filepathlist, 0);
                  showphoto_listview.setAdapter(adapter);
              }
              if(resultCode==2)
              {
                  filepathlist = fileUtils.getExistFileList(Environment.getExternalStorageDirectory().getPath() +  myApplication.move_file_path);
                  adapter = new showphoto_listviewAdapter(showPhoto.this, filepathlist, 0);
                  showphoto_listview.setAdapter(adapter);
                  gaibian_flag=2;
              }
              break;
      }
    }
}






