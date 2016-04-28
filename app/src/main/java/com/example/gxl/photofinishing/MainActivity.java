package com.example.gxl.photofinishing;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.ikimuhendis.ldrawer.ActionBarDrawerToggle;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;
import com.zhy.autolayout.AutoLayoutActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import Adapter.ListviewAdapter;
import Adapter.showphoto_listviewAdapter;
import Data.needMoveFile;
import Utils.BitmapUtils;
import Utils.LogUtils;
import Utils.ScreenUtils;
import Utils.fileUtils;
import Utils.sdUtils;
import myView.DragView;
import myView.MovePhotoGroup;
import myView.myGridview;
import myapplication.myApplication;

public class MainActivity extends AutoLayoutActivity implements OnClickListener {
    List<File> listfile = new ArrayList<File>();
    List<Bitmap> listbitmap = new ArrayList<Bitmap>();
    GridviewAdapter adapter;
    ImageView imageview;
    myGridview gridview;
    ListView listview;
    ListviewAdapter listviewadapter;
    ProgressBar progressbar;
    FrameLayout layout;
    LinearLayout layout1;
    LinkedHashMap<String, ArrayList<String>> filemap;
    float Rawx;
    float Rawy;
    ImageView mjianliImageview;
    int Pos[] = {-1, -1, -1, -1};

    int chakanPos[] = {-1, -1, -1, -1};

    //Dragview是否出现，0不出现，1出现
    int Dragview_flag = 0;

    MovePhotoGroup group;

    /**
     * 创建侧滑菜单需要使用的参数
     */
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerArrowDrawable drawerArrow;
    private boolean drawerArrowColor;
    private ImageView control_menu;
    private ImageView chakan_file;

    /**
     * 显示当前选中的照片数量,控制该布局是否显示
     */
    RelativeLayout show_move_detail;
    TextView chose_text;
    TextView quit;
    TextView delete;

    /**
     * listview显示区域用来添加白色的遮罩
     */

    RelativeLayout relativeLayout;


    /**
     * flag用来表示对话框是移动图片成功时消失，还是返回消失
     */
    int Dialog_flag = 0;
    /**
     * backgroud_flag用来表示当前listview是否为空
     */
    int Backgroud_flag = 0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    //顶部的relationlayout
    RelativeLayout top_area;
    LinearLayout listviewlinearlayout;
    FrameLayout listview_framelayout;

    int top_area_height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//用来取消状态栏
        setContentView(R.layout.activity_sample);
        init_LDrawer();
        control_menu = (ImageView) findViewById(R.id.menu);
        control_menu.setOnClickListener(this);
        chakan_file = (ImageView) findViewById(R.id.chakan);
        chakan_file.setOnClickListener(this);
        layout = (FrameLayout) findViewById(R.id.myFrameLayout);
        mjianliImageview = (ImageView) findViewById(R.id.jianli);
        listview = (ListView) findViewById(R.id.mylistview);
        top_area= (RelativeLayout) findViewById(R.id.top_area);
        listviewlinearlayout= (LinearLayout) findViewById(R.id.listviewlinearlayout);
        show_move_detail = (RelativeLayout) findViewById(R.id.show_move_detail);
        listview_framelayout= (FrameLayout) findViewById(R.id.listview_framelayout);
        chose_text = (TextView) findViewById(R.id.chose_text);
        quit = (TextView) findViewById(R.id.quit);
        delete = (TextView) findViewById(R.id.delete);
        delete.setOnClickListener(this);
        relativeLayout = (RelativeLayout) findViewById(R.id.listview_area);
        quit.setOnClickListener(this);
        new moveImages().execute();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * 初始化侧边栏菜单
     */
    void init_LDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navdrawer);
        drawerArrow = new DrawerArrowDrawable(this) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                drawerArrow, R.string.drawer_open,
                R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    /**
     * 点击事件的响应函数
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu:
                if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                    mDrawerLayout.closeDrawer(mDrawerList);
                } else {
                    mDrawerLayout.openDrawer(mDrawerList);
                }
                break;

            case R.id.chakan:
                //跳转显示已经存在的文件夹
                Intent intent = new Intent(MainActivity.this, showPhoto.class);
                startActivityForResult(intent, 1);
                break;

            case R.id.quit:
                needMoveFile.removeall();
                listviewadapter.notifyDataSetChanged();
                show_move_detail.setVisibility(View.GONE);
                break;

            case R.id.delete:
                Createdeletedialog();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
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
                "Main Page", // TODO: Define a title for the content shown.
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

    /**
     * 异步任务类：
     * 作用：将SD卡上面的照片加载到listview中显示出来
     */
    class moveImages extends AsyncTask<Void, Integer, Void> {

        ProgressDialog MyDialog;

        @Override
        protected void onPreExecute() {
            MyDialog = ProgressDialog.show(MainActivity.this, " ", " 正在分析中", true);
            super.onPreExecute();
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            MyDialog.dismiss();
            if (filemap.size() == 0) {
                relativeLayout.setBackgroundResource(R.drawable.ic_launcher);
                listview.setVisibility(View.GONE);
            } else {
                relativeLayout.setBackgroundResource(0);
                listview.setVisibility(View.VISIBLE);
                Log.i("love", "love2");
                listviewadapter = new ListviewAdapter(MainActivity.this, filemap, new ListviewAdapter.show_choose_detail_Listener() {
                    @Override
                    public void show_choose_detail_linearlayout(int size) {
                        show_move_detail.setVisibility(View.VISIBLE);
                        chose_text.setText("已选" + size + "张");
                    }

                    @Override
                    public void hide_choose_detail_linearlayout() {
                        show_move_detail.setVisibility(View.GONE);
                    }
                });
                listview.setAdapter(listviewadapter);

                Pos[0] = mjianliImageview.getLeft();
                Pos[1] = mjianliImageview.getTop();
                Pos[2] = mjianliImageview.getRight();
                Pos[3] = mjianliImageview.getBottom();

                chakanPos[0] = chakan_file.getLeft();
                chakanPos[1] = chakan_file.getTop();
                chakanPos[2] = chakan_file.getRight();
                chakanPos[3] = chakan_file.getBottom();


                Log.i("path", Pos[0] + "#" + Pos[1] + "#" + Pos[2] + "#" + Pos[3]);

                listviewadapter.setGroup(new ListviewAdapter.movephotoGroup() {

                    @Override
                    public void CreateMoveGroup(int x, int y, String path) {
                        Dragview_flag = 1;
                        Log.i("screen", ScreenUtils.getScreenWidth(MainActivity.this) + "");
                        Log.i("scree", ScreenUtils.getScreenHeight(MainActivity.this) + "");
                        show_move_detail.setVisibility(View.GONE);
                        relativeLayout.setBackgroundResource(R.drawable.white_copy);
                        mjianliImageview.setImageResource(R.drawable.jianli_unin);
                        group = new MovePhotoGroup(
                                MainActivity.this);
                        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                                LayoutParams.MATCH_PARENT,
                                LayoutParams.MATCH_PARENT);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                ScreenUtils.dip2px(MainActivity.this, 108),
                                ScreenUtils.dip2px(MainActivity.this, 108));
                        lp.topMargin = x - 25;
                        lp.leftMargin = y - 50;

                        int listviewlinearlayout_top=listviewlinearlayout.getTop();
                        int listview_top=listview.getTop();
                        int listviewframelayout_top=listview_framelayout.getTop();
                        top_area_height=listview_top+listviewlinearlayout_top+listviewframelayout_top;
//                        LogUtils.loggxl("top_area"+top_area_height+"listviewlinearlayout_top"+listviewlinearlayout_top+"listview_top"+listview_top);

                        final DragView view = new DragView(MainActivity.this,
                                BitmapUtils.fileTobitmap(new File(path), 206, 206), Pos, chakanPos,top_area_height,listview.getLeft());
                        view.setLayoutParams(lp);
                        view.setMlistener(new DragView.createFilelistener() {

                            @Override
                            public void createFile() {
                                layout.removeView(group);
                                Createdialog();
                            }

                            @Override
                            public void betrue_createFile(int flag) {
                                if (flag == 1)
                                    mjianliImageview.setImageResource(R.drawable.jianli_in);
                                else
                                    mjianliImageview.setImageResource(R.drawable.jianli_unin);
                            }

                            @Override
                            public void remove_view() {
                                mjianliImageview.setImageResource(R.drawable.jianli);
                                relativeLayout.setBackgroundResource(0);
                                listview.setOnTouchListener(new OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        return false;
                                    }
                                });
                                show_move_detail.setVisibility(View.VISIBLE);
                                layout.removeView(group);
                            }

                            @Override
                            public void move_exist_file() {
                                layout.removeView(group);
                                Intent intent = new Intent(MainActivity.this, showPhoto.class);
                                intent.putExtra("flag", 1);
                                startActivityForResult(intent, 1);
                            }

                            @Override
                            public void change_imageview() {
                                LogUtils.loggxl("test animation");
                                PropertyValuesHolder pvh1=PropertyValuesHolder.ofFloat("scaleX",0.8f);
                                PropertyValuesHolder pvh2=PropertyValuesHolder.ofFloat("scaleY",0.8f);
                                ObjectAnimator.ofPropertyValuesHolder(view, pvh1, pvh2).setDuration(100).start();
                            }
                        });
                        layout.addView(group, lp1);
                        group.addView(view, lp);
                        listview.setClickable(false);
                        listview.setFocusable(false);
                        final MotionEvent toucheevent;
                        listview.setOnTouchListener(new OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                LogUtils.loggxl("diyici");
//                                toucheevent=event;
                                view.onTouchEvent(event);
                                return true;
                            }
                        });
                    }
                });
                mjianliImageview.getLocationOnScreen(Pos);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }




        @Override
        protected Void doInBackground(Void... params) {
            listfile=fileUtils.getSD();
            filemap = new LinkedHashMap<String, ArrayList<String>>();
            if (listfile.size() != 0) {
                String date = fileUtils.lastModifiedTodate(listfile.get(0));
                filemap.put(date, new ArrayList<String>());
                filemap.get(fileUtils.lastModifiedTodate(listfile.get(0))).add(
                        listfile.get(0).getAbsolutePath());
                for (int i = 1; i < listfile.size(); i++) {
                    if (!filemap.containsKey(fileUtils.lastModifiedTodate(listfile
                            .get(i)))) {
                        filemap.put(fileUtils.lastModifiedTodate(listfile.get(i)),
                                new ArrayList<String>());
                        filemap.get(fileUtils.lastModifiedTodate(listfile.get(i)))
                                .add(listfile.get(i).getAbsolutePath());
                    } else {
                        filemap.get(fileUtils.lastModifiedTodate(listfile.get(i)))
                                .add(listfile.get(i).getAbsolutePath());
                    }
                }
            }else
            {
                Log.i("path", "当前里面没有照片");
            }
            return null;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (Dragview_flag == 0) {
                finish();
            } else {
                Dragview_flag = 0;
                mjianliImageview.setImageResource(R.drawable.jianli);
                relativeLayout.setBackgroundResource(0);
                listview.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });
                show_move_detail.setVisibility(View.VISIBLE);
                layout.removeView(group);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 将选好的图片移动到指定的文件夹
     */
    class moveNeedfile_task extends AsyncTask<String, Void, Void> {
        ProgressDialog MyDialog;

        @Override
        protected void onPreExecute() {
            MyDialog = ProgressDialog.show(MainActivity.this, " ", "正在移动中", true);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            MoveNeedfile(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            MyDialog.dismiss();
            change_listview();
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
        File movefile = new File(Environment.getExternalStorageDirectory()
                .getPath()  + myApplication.move_file_path + "/" + path);
        if (!movefile.exists()) {
            movefile.mkdir();
        }
        for (File file : needmovelistfile) {
            fileUtils.copyFile(file.getAbsolutePath(), Environment
                    .getExternalStorageDirectory().getPath()
                    +  myApplication.move_file_path + "/" + path + "/" + file.getName());
        }

    }

    public void change_listview() {
        List<String> list = needMoveFile.getNeedmoveFile();
        List<String> filelist = new ArrayList<String>();
        for (String key : filemap.keySet()) {
            ArrayList<String> map = filemap.get(key);
            for (String file : list) {
                map.remove(file);
            }
            if (map.size() == 0)
                filelist.add(key);
        }
        for (String string : filelist) {
            Iterator<Entry<String, ArrayList<String>>> it = filemap
                    .entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, ArrayList<String>> entry = it.next();
                String key = entry.getKey();
                if (key.equals(string)) {
                    it.remove();
                }
            }
        }
        needMoveFile.removeall();
        needMoveFile.clearPositemap();
        if (filemap.size() == 0) {
            relativeLayout.setBackgroundResource(R.drawable.ic_launcher);
            listview.setVisibility(View.GONE);
            Backgroud_flag = 1;
        } else {
            listview.setVisibility(View.VISIBLE);
            listviewadapter = new ListviewAdapter(
                    MainActivity.this, filemap, new ListviewAdapter.show_choose_detail_Listener() {
                @Override
                public void show_choose_detail_linearlayout(int size) {
                    show_move_detail.setVisibility(View.VISIBLE);
                    chose_text.setText("已选" + size + "张");
                }

                @Override
                public void hide_choose_detail_linearlayout() {
                    show_move_detail.setVisibility(View.GONE);
                }
            });
            listview.setAdapter(listviewadapter);
            listviewadapter.setGroup(new ListviewAdapter.movephotoGroup() {

                @Override
                public void CreateMoveGroup(int x, int y, String path) {
                    Dragview_flag = 1;
                    show_move_detail.setVisibility(View.GONE);
                    relativeLayout.setBackgroundResource(R.drawable.white_copy);
                    mjianliImageview.setImageResource(R.drawable.jianli_unin);
                    group = new MovePhotoGroup(
                            MainActivity.this);
                    LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                            LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            ScreenUtils.dip2px(MainActivity.this, 108),
                            ScreenUtils.dip2px(MainActivity.this, 108));
                    lp.topMargin = x;
                    lp.leftMargin = y - 50;
                    final DragView view = new DragView(MainActivity.this,
                            BitmapUtils.fileTobitmap(new File(path), 206, 206), Pos, chakanPos,top_area_height,listview.getLeft());
                    view.setMlistener(new DragView.createFilelistener() {

                        @Override
                        public void createFile() {
                            layout.removeView(group);
                            Createdialog();
                        }

                        @Override
                        public void betrue_createFile(int flag) {
                            if (flag == 1)
                                mjianliImageview.setImageResource(R.drawable.jianli_in);
                            else
                                mjianliImageview.setImageResource(R.drawable.jianli_unin);
                        }

                        @Override
                        public void remove_view() {
                            mjianliImageview.setImageResource(R.drawable.jianli);
                            relativeLayout.setBackgroundResource(0);
                            listview.setOnTouchListener(new OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    return false;
                                }
                            });
                            show_move_detail.setVisibility(View.VISIBLE);
                            layout.removeView(group);
                        }

                        @Override
                        public void move_exist_file() {
                            layout.removeView(group);
                            Intent intent = new Intent(MainActivity.this, showPhoto.class);
                            intent.putExtra("flag", 1);
                            startActivityForResult(intent, 1);
                        }

                        @Override
                        public void change_imageview() {
                            PropertyValuesHolder pvh1=PropertyValuesHolder.ofFloat("scaleX",1f,0,1f);
                            PropertyValuesHolder pvh2=PropertyValuesHolder.ofFloat("scaleY",1f,0,1f);
                            ObjectAnimator.ofPropertyValuesHolder(view, pvh1, pvh2).setDuration(100).start();
                        }
                    });
                    group.addView(view, lp);
                    layout.addView(group, lp1);
                    listview.setClickable(false);
                    listview.setFocusable(false);
                    listview.setOnTouchListener(new OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return true;
                        }
                    });
                    group.setOnTouchListener(new OnTouchListener() {

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            // TODO Auto-generated method stub
                            return true;
                        }
                    });
                }
            });
        }
    }


    public void Createdialog() {
        Dialog_flag = 0;
        Backgroud_flag = 0;
        LayoutInflater inflaterDl = LayoutInflater.from(this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(
                R.layout.dialog_main_info, null);
        final Dialog dialog = new Dialog(MainActivity.this, R.style.Dialog_FS);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mjianliImageview.setImageResource(R.drawable.jianli);
                if (Backgroud_flag == 0) {
                    relativeLayout.setBackgroundResource(0);
                }
                if (Dialog_flag == 0) {
                    show_move_detail.setVisibility(View.VISIBLE);
                }
                listview.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });
            }
        });
        final EditText path_edittext = (EditText) layout
                .findViewById(R.id.path);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth());
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setContentView(layout);
        Button btnOK = (Button) layout.findViewById(R.id.dialog_ok);
        btnOK.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!path_edittext.getText().toString().equals("")) {
                    new moveNeedfile_task().execute(path_edittext.getText().toString());
                } else {
                    new moveNeedfile_task().execute("无标题");
                }
                if (Backgroud_flag == 0) {
                    relativeLayout.setBackgroundResource(0);
                }
                Dialog_flag = 1;
                mjianliImageview.setImageResource(R.drawable.jianli);
                dialog.dismiss();
            }
        });
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(path_edittext, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        dialog.show();
    }


    public void Createdeletedialog() {
//        final AlertDialog.Builder customizeDialog = new AlertDialog.Builder(showPhoto.this);
        final View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.delete_main_dialog, null);
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth());
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setContentView(dialogView);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (needMoveFile.needmoveFile.size() != 0) {
                    show_move_detail.setVisibility(View.VISIBLE);
                } else {
                    show_move_detail.setVisibility(View.GONE);
                }
            }
        });
        final RelativeLayout checkbox = (RelativeLayout) dialogView.findViewById(R.id.checkbox);
        RelativeLayout quxiao = (RelativeLayout) dialogView.findViewById(R.id.quxiao);
        RelativeLayout queding = (RelativeLayout) dialogView.findViewById(R.id.queding);
        TextView delete_text = (TextView) dialogView.findViewById(R.id.shanchutext);
        delete_text.setText("确定删除" + needMoveFile.needmoveFile.size() + "个文件?");
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new deletefile_task().execute();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 将选中的文件直接删除
     */
    class deletefile_task extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            fileUtils.deleteFilelist(needMoveFile.getNeedmoveFile());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            changelistview();
        }
    }

    void changelistview() {
        List<String> list = needMoveFile.getNeedmoveFile();
        List<File> needmovelistfile = new ArrayList<File>();
        for (String string : list) {
            needmovelistfile.add(new File(string));
        }
        List<String> filelist = new ArrayList<String>();
        for (String key : filemap.keySet()) {
            ArrayList<String> map = filemap.get(key);
            for (String file : list) {
                map.remove(file);
            }
            if (map.size() == 0)
                filelist.add(key);
        }
        for (String string : filelist) {
            Iterator<Entry<String, ArrayList<String>>> it = filemap
                    .entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, ArrayList<String>> entry = it.next();
                String key = entry.getKey();
                if (key.equals(string)) {
                    it.remove();
                }
            }
        }
        if (filemap.size() == 0) {
            relativeLayout.setBackgroundResource(R.drawable.ic_launcher);
            listview.setVisibility(View.GONE);
            Backgroud_flag = 1;
        } else {
            listview.setVisibility(View.VISIBLE);
            listviewadapter = new ListviewAdapter(
                    MainActivity.this, filemap, new ListviewAdapter.show_choose_detail_Listener() {
                @Override
                public void show_choose_detail_linearlayout(int size) {
                    show_move_detail.setVisibility(View.VISIBLE);
                    chose_text.setText("已选" + size + "张");
                }

                @Override
                public void hide_choose_detail_linearlayout() {
                    show_move_detail.setVisibility(View.GONE);
                }
            });
            needMoveFile.removeall();
            needMoveFile.clearPositemap();
            listview.setAdapter(listviewadapter);
            listviewadapter.setGroup(new ListviewAdapter.movephotoGroup() {

                @Override
                public void CreateMoveGroup(int x, int y, String path) {
                    Dragview_flag = 1;
                    show_move_detail.setVisibility(View.GONE);
                    relativeLayout.setBackgroundResource(R.drawable.white_copy);
                    mjianliImageview.setImageResource(R.drawable.jianli_unin);
                    group = new MovePhotoGroup(
                            MainActivity.this);
                    LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                            LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            ScreenUtils.dip2px(MainActivity.this, 108),
                            ScreenUtils.dip2px(MainActivity.this, 108));
                    lp.topMargin = x;
                    lp.leftMargin = y - 50;
                    final DragView view = new DragView(MainActivity.this,
                            BitmapUtils.fileTobitmap(new File(path), 206, 206), Pos, chakanPos,top_area_height,listview.getLeft());
                    view.setMlistener(new DragView.createFilelistener() {

                        @Override
                        public void createFile() {
                            layout.removeView(group);
                            Createdialog();
                        }

                        @Override
                        public void betrue_createFile(int flag) {
                            if (flag == 1)
                                mjianliImageview.setImageResource(R.drawable.jianli_in);
                            else
                                mjianliImageview.setImageResource(R.drawable.jianli_unin);
                        }

                        @Override
                        public void remove_view() {
                            mjianliImageview.setImageResource(R.drawable.jianli);
                            relativeLayout.setBackgroundResource(0);
                            listview.setOnTouchListener(new OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    return false;
                                }
                            });
                            show_move_detail.setVisibility(View.VISIBLE);
                            layout.removeView(group);
                        }

                        @Override
                        public void move_exist_file() {
                            layout.removeView(group);
                            Intent intent = new Intent(MainActivity.this, showPhoto.class);
                            intent.putExtra("flag", 1);
                            startActivityForResult(intent, 1);
                        }

                        @Override
                        public void change_imageview() {

                            PropertyValuesHolder pvh1=PropertyValuesHolder.ofFloat("scaleX",1f,0,1f);
                            PropertyValuesHolder pvh2=PropertyValuesHolder.ofFloat("scaleY",1f,0,1f);
                            ObjectAnimator.ofPropertyValuesHolder(view, pvh1, pvh2).setDuration(100).start();
                        }
                    });
                    group.addView(view, lp);
                    layout.addView(group, lp1);
                    listview.setClickable(false);
                    listview.setFocusable(false);
                    listview.setOnTouchListener(new OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return true;
                        }
                    });
                    group.setOnTouchListener(new OnTouchListener() {

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            // TODO Auto-generated method stub
                            return true;
                        }
                    });
                }
            });
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mjianliImageview.setImageResource(R.drawable.jianli);
        if (Backgroud_flag == 0) {
            relativeLayout.setBackgroundResource(0);
        }
        if (needMoveFile.getNeedmoveFile().size() > 0) {
            show_move_detail.setVisibility(View.VISIBLE);
        } else {
            show_move_detail.setVisibility(View.GONE);
        }
        listview.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == 1) {
                    Log.i("tag", "onActivityResult:gxl ");
                    mjianliImageview.setImageResource(R.drawable.jianli);
                    relativeLayout.setBackgroundResource(0);
                    show_move_detail.setVisibility(View.VISIBLE);
                    listview.setOnTouchListener(new OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return false;
                        }
                    });
                } else if (resultCode == 2) {
                    Log.i("test", "重新");
                    new moveImages().execute();
                }
                break;
        }
    }

}
