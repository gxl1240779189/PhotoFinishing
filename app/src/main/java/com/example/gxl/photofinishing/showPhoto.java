package com.example.gxl.photofinishing;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Selection;
import android.text.Spannable;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zhy.autolayout.AutoLayoutActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import Presenter.ShowPhotoPresenter;
import adapter.showphoto_listviewAdapter;
import application.myApplication;
import data.needMoveFile;
import de.greenrobot.event.EventBus;
import eventbustype.FirstEventType;
import eventbustype.ShowPhotoDetailType;
import utils.fileUtils;
import customview.myDialog;
import view.ShowPhotoInterface;

public class showPhoto extends AutoLayoutActivity implements View.OnClickListener, ShowPhotoInterface {
    private ArrayList<String> filepathlist;
    private ListView showphoto_listview;
    private showphoto_listviewAdapter adapter;
    int delete_type = 0;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    RelativeLayout fanhui;//返回
    RelativeLayout xuanze;//选择
    RelativeLayout quxiao;//取消
    RelativeLayout quanxuan;//全选
    TextView quanxuan_text; //全选文字

    TextView huanyuan_textview;
    TextView shanchu_textview;
    RelativeLayout show_move_detail;

    int gaibian_flag = 0;
    int quanxuan_flag = 0;//当前是否全选
    int delete_flag = 4;
    int huanyuan_flag = 5;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    int Type_zhijiedelete = 1;
    int Type_huanyuan = 2;
    int flag = Type_zhijiedelete;

    private ShowPhotoPresenter mShowPhotoPresenter;

    private final int ReturnDelete=1;
    private final int ReturnRestore=2;
    private EventBus mEventBus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//用来取消状态栏
        EventBus.getDefault().register(this);
        setContentView(R.layout.showphoto_listviewlayout);
        InitView();
        mShowPhotoPresenter = new ShowPhotoPresenter(this);
        mShowPhotoPresenter.InitListview(0);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fanhui:
                finish();
                break;
            case R.id.xuanze:
                delete_type = 1;
                LoadListviewSuccess(filepathlist, 1);
                break;

            case R.id.quxiao:
                delete_type = 0;
                LoadListviewSuccess(filepathlist, 0);
                break;
            case R.id.quanxuan:
                if (quanxuan_flag == 0) {
                    quanxuan_text.setText("全不选");
                    quanxuan_flag = 1;
                    for (int i = showphoto_listview.getFirstVisiblePosition(); i <= showphoto_listview.getLastVisiblePosition(); i++) {
                        showphoto_listview.findViewById(i).setBackgroundResource(R.drawable.check_choose);
                    }
                    for (int i = 0; i < filepathlist.size(); i++) {
                        needMoveFile.addNeeddeletefile(filepathlist.get(i));
                    }
                } else {
                    quanxuan_text.setText("全选");
                    quanxuan_flag = 0;
                    for (int i = showphoto_listview.getFirstVisiblePosition(); i <= showphoto_listview.getLastVisiblePosition(); i++) {
                        showphoto_listview.findViewById(i).setBackgroundResource(R.drawable.check_unchoose);
                    }
                    needMoveFile.removealldeletefile();
                }
                break;

            case R.id.huanyuan:
                if (needMoveFile.getdeletefile().size() != 0) {
                    flag = Type_huanyuan;
                    Createdialog(huanyuan_flag);
                } else {
                    Toast.makeText(showPhoto.this, "请至少选择一项", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.delete:
                if (needMoveFile.getdeletefile().size() != 0) {
                    flag = Type_zhijiedelete;
                    Createdialog(delete_flag);
                } else {
                    Toast.makeText(showPhoto.this, "请至少选择一项", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //初始化View和设置监听，资源
    @Override
    public void InitView() {
        imageLoader.init(ImageLoaderConfiguration.createDefault(showPhoto.this));
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.yujiazai)
                .showImageForEmptyUri(R.drawable.yujiazai)
                .showImageOnFail(R.drawable.yujiazai).cacheInMemory()
                .cacheOnDisc().displayer(new RoundedBitmapDisplayer(20))
                .displayer(new FadeInBitmapDisplayer(300)).build();
        show_move_detail = (RelativeLayout) findViewById(R.id.show_move_detail);
        huanyuan_textview = (TextView) findViewById(R.id.huanyuan);
        shanchu_textview = (TextView) findViewById(R.id.delete);

        fanhui = (RelativeLayout) findViewById(R.id.fanhui);
        xuanze = (RelativeLayout) findViewById(R.id.xuanze);
        quxiao = (RelativeLayout) findViewById(R.id.quxiao);
        quanxuan = (RelativeLayout) findViewById(R.id.quanxuan);
        quanxuan_text = (TextView) findViewById(R.id.quanxuan_text);
        showphoto_listview = (ListView) findViewById(R.id.showphoto_listview);
        fanhui.setOnClickListener(this);
        xuanze.setOnClickListener(this);
        quanxuan.setOnClickListener(this);
        quxiao.setOnClickListener(this);
        huanyuan_textview.setOnClickListener(this);
        shanchu_textview.setOnClickListener(this);
    }

    //数据成功加载到ListView中
    @Override
    public void LoadListviewSuccess(ArrayList<String> filemap, int mflag) {
        filepathlist = filemap;
        if (mflag == 1) {    //变成删除界面
            ChangeToDelete();
        } else {             //变成正常界面
            ChangeToNormal();
        }
        adapter = new showphoto_listviewAdapter(showPhoto.this, filepathlist, mflag);
        showphoto_listview.setAdapter(adapter);
        adapter.setMenuListener(new showphoto_listviewAdapter.MenuListener() {
            @Override
            public void delete(int position) {
                needMoveFile.addNeeddeletefile(filepathlist.get(position));
                flag = Type_zhijiedelete;
                Createdialog(delete_flag);
            }

            @Override
            public void huanyuan(int position) {
                needMoveFile.addNeeddeletefile(filepathlist.get(position));
                flag = Type_huanyuan;
                Createdialog(huanyuan_flag);
            }

            @Override
            public void chongmingming(int position) {
                Create_rename_dialog(filepathlist.get(position));
            }

            @Override
            public void beifen(int position) {

            }
        });
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
                ChangeToDelete();
                delete_type = 1;
                adapter = new showphoto_listviewAdapter(showPhoto.this, filepathlist, 1);
                showphoto_listview.setAdapter(adapter);
                return true;
            }
        });
    }

    //数据返回为空，显示一个没有数据的界面
    @Override
    public void LoadlistviewFail() {

    }

    @Override
    public void ChangeToNormal() {
        show_move_detail.setVisibility(View.GONE);
        fanhui.setVisibility(View.VISIBLE);
        xuanze.setVisibility(View.VISIBLE);
        quxiao.setVisibility(View.GONE);
        quanxuan.setVisibility(View.GONE);
    }

    @Override
    public void ChangeToDelete() {
        show_move_detail.setVisibility(View.VISIBLE);
        fanhui.setVisibility(View.GONE);
        xuanze.setVisibility(View.GONE);
        quxiao.setVisibility(View.VISIBLE);
        quanxuan.setVisibility(View.VISIBLE);
    }


    /**
     * 将整理好的文件夹还原到原来的文件夹中
     */
    class huanyuanfile_task extends AsyncTask<Void, Void, Void> {
        Dialog MyDialog;
        Dialog finishDialog;

        @Override
        protected void onPreExecute() {
            MyDialog = myDialog.createLoadingDialog(showPhoto.this, "正在还原照片");
            finishDialog = myDialog.createLoadingfinishDialog(showPhoto.this, "已完成");
            MyDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            fileUtils.moveFilelist(needMoveFile.getdeletefile());
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
            for (int i = 0; i < needMoveFile.getdeletefile().size(); i++) {
                filepathlist.remove(needMoveFile.getdeletefile().get(i));
            }
            needMoveFile.removealldeletefile();
            adapter = new showphoto_listviewAdapter(showPhoto.this, filepathlist, 0);
            showphoto_listview.setAdapter(adapter);
            adapter.setMenuListener(new showphoto_listviewAdapter.MenuListener() {
                @Override
                public void delete(int position) {
                    needMoveFile.addNeeddeletefile(filepathlist.get(position));
                    flag = Type_zhijiedelete;
                    Createdialog(delete_flag);
                }

                @Override
                public void huanyuan(int position) {
                    needMoveFile.addNeeddeletefile(filepathlist.get(position));
                    flag = Type_huanyuan;
                    Createdialog(huanyuan_flag);
                }

                @Override
                public void chongmingming(int position) {
                    Create_rename_dialog(filepathlist.get(position));
                }

                @Override
                public void beifen(int position) {

                }
            });
            ChangeToNormal();
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
            MyDialog = myDialog.createLoadingDialog(showPhoto.this, "正在删除照片");
            finishDialog = myDialog.createLoadingfinishDialog(showPhoto.this, "已完成");
            MyDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            fileUtils.deleteFilelist(needMoveFile.getdeletefile());
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
            for (int i = 0; i < needMoveFile.getdeletefile().size(); i++) {
                filepathlist.remove(needMoveFile.getdeletefile().get(i));
            }
            needMoveFile.removealldeletefile();
            adapter = new showphoto_listviewAdapter(showPhoto.this, filepathlist, 0);
            showphoto_listview.setAdapter(adapter);
            adapter.setMenuListener(new showphoto_listviewAdapter.MenuListener() {
                @Override
                public void delete(int position) {
                    needMoveFile.addNeeddeletefile(filepathlist.get(position));
                    flag = Type_zhijiedelete;
                    Createdialog(delete_flag);
                }

                @Override
                public void huanyuan(int position) {
                    needMoveFile.addNeeddeletefile(filepathlist.get(position));
                    flag = Type_huanyuan;
                    Createdialog(huanyuan_flag);
                }

                @Override
                public void chongmingming(int position) {
                    Create_rename_dialog(filepathlist.get(position));
                }

                @Override
                public void beifen(int position) {

                }
            });
            ChangeToNormal();
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
            fileUtils.copyFile(file.getAbsolutePath(), path + "/" + file.getName());
        }
        needMoveFile.removeall();
        needMoveFile.clearPositemap();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (delete_type == 1) {
                delete_type = 0;
                LoadListviewSuccess(filepathlist, 0);
                needMoveFile.removealldeletefile();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 弹出提示对话框
     */
    public void Createdialog(int view_flag) {
        final View dialogView = LayoutInflater.from(showPhoto.this).inflate(R.layout.delete_dialog, null);
        final Dialog dialog = new Dialog(showPhoto.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth());
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setContentView(dialogView);
        RelativeLayout quxiao = (RelativeLayout) dialogView.findViewById(R.id.quxiao);
        RelativeLayout queding = (RelativeLayout) dialogView.findViewById(R.id.queding);
        TextView dialog_title = (TextView) dialogView.findViewById(R.id.dialog_title);
        TextView delete_text = (TextView) dialogView.findViewById(R.id.shanchutext);
        if (view_flag == huanyuan_flag) {
            dialog_title.setText("确认还原");
            delete_text.setText("还原后相册夹里的照片将会回到原位置");
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
                delete_type = 0;
                if (flag == Type_zhijiedelete) {
                    Log.i("path", "直接删除");
                    new deletefile_task().execute();
                } else {
                    new huanyuanfile_task().execute();
                    EventBus.getDefault().post(new FirstEventType(1));
                }
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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case 1:
//                if (resultCode == 1) {
//                    filepathlist = fileUtils.getExistFileList(Environment.getExternalStorageDirectory().getPath() + myApplication.move_file_path);
//                    adapter = new showphoto_listviewAdapter(showPhoto.this, filepathlist, 0);
//                    showphoto_listview.setAdapter(adapter);
//                }
//                if (resultCode == 2) {
//                    filepathlist = fileUtils.getExistFileList(Environment.getExternalStorageDirectory().getPath() + myApplication.move_file_path);
//                    adapter = new showphoto_listviewAdapter(showPhoto.this, filepathlist, 0);
//                    showphoto_listview.setAdapter(adapter);
//                    gaibian_flag = 2;
//                }
//                break;
//        }
//    }

    /**
     * EventBus的事件处理函数
     *
     * @param event
     */
    public void onEventMainThread(ShowPhotoDetailType event) {
        switch (event.getmFlag()) {
            case ReturnDelete:
                mShowPhotoPresenter.InitListview(0);
                break;

            case ReturnRestore:
                mShowPhotoPresenter.InitListview(0);
                EventBus.getDefault().post(new FirstEventType(1));
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 弹出重命名的对话框
     */
    public void Create_rename_dialog(String pathname) {
        LayoutInflater inflaterDl = LayoutInflater.from(this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(
                R.layout.dialog_main_info, null);
        final Dialog dialog = new Dialog(showPhoto.this, R.style.Dialog_FS);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        final EditText path_edittext = (EditText) layout
                .findViewById(R.id.path);
        Button dialog_ok = (Button) layout.findViewById(R.id.dialog_ok);
        dialog_ok.setText("确定修改");
        path_edittext.setCursorVisible(true);
        path_edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                path_edittext.setTextColor(Color.parseColor("#B7B7B7"));
            }
        });
        final String yuanlai_filename = pathname.substring(pathname.lastIndexOf("/") + 1, pathname.length());
        path_edittext.setText(yuanlai_filename);
        setEditTextCursorLocation(path_edittext);
        //此段代码可以用来直接出现输入法界面
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(path_edittext, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth());
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setContentView(layout);
        Button btnOK = (Button) layout.findViewById(R.id.dialog_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!path_edittext.getText().toString().equals("")) {
                    //修改文件名
                    renameToNewFile(yuanlai_filename, path_edittext.getText().toString());
                    filepathlist = fileUtils.getExistFileList(Environment.getExternalStorageDirectory().getPath() + myApplication.move_file_path);
                    adapter = new showphoto_listviewAdapter(showPhoto.this, filepathlist, 0);
                    showphoto_listview.setAdapter(adapter);
                    adapter.setMenuListener(new showphoto_listviewAdapter.MenuListener() {
                        @Override
                        public void delete(int position) {
                            needMoveFile.addNeeddeletefile(filepathlist.get(position));
                            flag = Type_zhijiedelete;
                            Createdialog(delete_flag);
                        }

                        @Override
                        public void huanyuan(int position) {
                            needMoveFile.addNeeddeletefile(filepathlist.get(position));
                            flag = Type_huanyuan;
                            Createdialog(huanyuan_flag);
                        }

                        @Override
                        public void chongmingming(int position) {
                            Create_rename_dialog(filepathlist.get(position));
                        }

                        @Override
                        public void beifen(int position) {

                        }
                    });
                    dialog.dismiss();
                }
            }
        });

        ImageView wenjianjia_photo = (ImageView) layout.findViewById(R.id.wenjianjia_photo);
        File[] files = (new File(pathname)).listFiles();
        if (files != null) {
            imageLoader.displayImage("file:///" + files[0].getAbsolutePath(), wenjianjia_photo,
                    options);
        }
        ImageView cancle = (ImageView) layout.findViewById(R.id.cancle);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    /**
     * 将光标定位到edittext最后
     *
     * @param editText
     */
    public void setEditTextCursorLocation(EditText editText) {
        CharSequence text = editText.getText();
        if (text instanceof Spannable) {
            Spannable spanText = (Spannable) text;
            Selection.setSelection(spanText, text.length());
        }
    }

    /**
     * 文件夹重命名
     *
     * @param path
     * @return
     */
    private boolean renameToNewFile(String first_title, String path) {
        File srcDir = new File(Environment.getExternalStorageDirectory().getPath() + myApplication.move_file_path + "/" + first_title);  //就文件夹路径
        boolean isOk = srcDir.renameTo(new File(Environment.getExternalStorageDirectory().getPath() + myApplication.move_file_path + "/" + path));  //dest新文件夹路径，通过renameto修改
        System.out.println("renameToNewFile is OK ? :" + isOk);
        return isOk;
    }
}






