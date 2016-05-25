package com.example.gxl.photofinishing;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.text.Selection;
import android.text.Spannable;
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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.ikimuhendis.ldrawer.ActionBarDrawerToggle;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zhy.autolayout.AutoLayoutActivity;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import adapter.ListviewAdapter;
import adapter.movephoto_listviewAdapter;
import application.myApplication;
import data.needMoveFile;
import data.shezhiSharedprefrence;
import utils.BitmapUtils;
import utils.LogUtils;
import utils.ScreenUtils;
import utils.fileUtils;
import customview.DragView;
import customview.MovePhotoGroup;
import customview.myDialog;
import customview.myGridview;

/**
 * 主界面
 * @author gxl
 *
 */

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
    private LinearLayout mDrawerList;
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


    /**
     * Dragview移动到不同的局域的变化
     */
    int changeToNormal = 0;//界面返回正常状态
    int changeToCreate = 1;//界面变成创建状态:具体表现，白色箭头开始闪烁
    int changeTobackground = 3;//toparea的背景变颜色
    int changeToDragview = 4;//Dragview开始缩放

    ImageView jiantou;

    FrameLayout FrameLayout_jianli;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    EditText path_edittext;

    String[] information = new String[3];

    DragView view;

    //用来获取到已经移动好的文件夹
    private ArrayList<String> filepathlist;
    private movephoto_listviewAdapter showphoto_adapter;

    private shezhiSharedprefrence shezhiSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//用来取消状态栏
        setContentView(R.layout.activity_sample);
        initView();//初始化布局
        init_LDrawer();//初始化菜单
        initConfig(); //初始化配置信息
        new moveImages().execute(); //启动异步任务将手机上的图片加载到布局中来
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     *  初始化配置信息
     */
    private void initConfig() {
        shezhiSP=new shezhiSharedprefrence(MainActivity.this);
        imageLoader.init(ImageLoaderConfiguration.createDefault(MainActivity.this));
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.yujiazai)
                .showImageForEmptyUri(R.drawable.yujiazai)
                .showImageOnFail(R.drawable.yujiazai).cacheInMemory()
                .cacheOnDisc().displayer(new RoundedBitmapDisplayer(20))
                .displayer(new FadeInBitmapDisplayer(300)).build();
    }

    /**
     * 初始化布局
     */

    private void initView() {
        //初始化menu点击事件
        control_menu = (ImageView) findViewById(R.id.menu);
        control_menu.setOnClickListener(this);
        //初始化查看文件夹按钮点击事件
        chakan_file = (ImageView) findViewById(R.id.chakan);
        chakan_file.setOnClickListener(this);
        layout = (FrameLayout) findViewById(R.id.myFrameLayout);
        mjianliImageview = (ImageView) findViewById(R.id.jianli);
        jiantou = (ImageView) findViewById(R.id.jiantou);
        listview = (ListView) findViewById(R.id.mylistview);
        top_area = (RelativeLayout) findViewById(R.id.top_area);
        listviewlinearlayout = (LinearLayout) findViewById(R.id.listviewlinearlayout);
        show_move_detail = (RelativeLayout) findViewById(R.id.show_move_detail);
        listview_framelayout = (FrameLayout) findViewById(R.id.listview_framelayout);
        chose_text = (TextView) findViewById(R.id.chose_text);
        quit = (TextView) findViewById(R.id.quit);
        delete = (TextView) findViewById(R.id.delete);
        FrameLayout_jianli = (FrameLayout) findViewById(R.id.FrameLayout_jianli);
        delete.setOnClickListener(this);
        relativeLayout = (RelativeLayout) findViewById(R.id.listview_area);
        quit.setOnClickListener(this);
    }

    /**
     * 初始化侧边栏菜单
     */
    void init_LDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (LinearLayout) findViewById(R.id.navdrawer);
        RelativeLayout guanli= (RelativeLayout) mDrawerList.findViewById(R.id.guanli);
        guanli.setOnClickListener(this);
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
                    mDrawerList.setOnTouchListener(new OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return true;
                        }
                    });
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

            case R.id.guanli:
                Intent guanliintent=new Intent(MainActivity.this,guanlimenuActivity.class);
                startActivityForResult(guanliintent, 1);
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

        Dialog MyDialog;
        Dialog finishDialog;

        @Override
        protected void onPreExecute() {
            MyDialog = myDialog.createLoadingDialog(MainActivity.this, "正在分析照片");
            finishDialog = myDialog.createLoadingfinishDialog(MainActivity.this, "已完成");
            MyDialog.show();
            super.onPreExecute();
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            MyDialog.dismiss();
            finishDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finishDialog.dismiss();
                }
            }, 500);
            if (filemap.size() == 0) {
                relativeLayout.setBackgroundResource(R.drawable.yujiazai);
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

                Pos[0] = FrameLayout_jianli.getLeft() + mjianliImageview.getLeft();
                Pos[1] = mjianliImageview.getTop();
                Pos[2] = FrameLayout_jianli.getLeft() + mjianliImageview.getRight();
                Pos[3] = mjianliImageview.getBottom();

                chakanPos[0] = chakan_file.getLeft();
                chakanPos[1] = chakan_file.getTop();
                chakanPos[2] = chakan_file.getRight();
                chakanPos[3] = chakan_file.getBottom();

                listviewadapter.setGroup(new ListviewAdapter.movephotoGroup() {

                    @Override
                    public void CreateMoveGroup(int x, int y, String path) {
                        Dragview_flag = 1;
                        show_move_detail.setVisibility(View.GONE);
                        relativeLayout.setBackgroundResource(R.drawable.white_copy);

                        mjianliImageview.setImageResource(R.drawable.jianli_green);
                        jiantou.setVisibility(View.VISIBLE);
                        setFlickerAnimation(jiantou, 1, 0);

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

                        int listviewlinearlayout_top = listviewlinearlayout.getTop();
                        int listview_top = listview.getTop();
                        int listviewframelayout_top = listview_framelayout.getTop();
                        top_area_height = listview_top + listviewlinearlayout_top + listviewframelayout_top;

                        view = new DragView(MainActivity.this,
                                BitmapUtils.fileTobitmap(new File(path), 206, 206), Pos, chakanPos, top_area_height, listview.getLeft());
                        view.setLayoutParams(lp);
                        view.setMlistener(new DragView.createFilelistener() {

                            @Override
                            public void createFile() {
                                layout.removeView(group);
                                Createdialog();
                            }

                            @Override
                            public void betrue_createFile(int flag) {
                                if (flag == 1) {
                                    setScaleAnimation(view, 0.6f);
                                    setFlickerAnimation(mjianliImageview, 1, 0.5f);
                                    setScaleAnimation(mjianliImageview, 1.05f);
                                } else {
                                    mjianliImageview.clearAnimation();
                                    setScaleAnimation(view, 1f);
                                    setScaleAnimation(mjianliImageview, 1f);
                                }
                            }

                            @Override
                            public void remove_view() {
                                mjianliImageview.setImageResource(R.drawable.jianli);
                                jiantou.clearAnimation();
                                jiantou.setVisibility(View.GONE);
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
//                                Intent intent = new Intent(MainActivity.this, showPhoto.class);
//                                intent.putExtra("flag", 1);
//                                startActivityForResult(intent, 1);
                                Movedialog();
                            }

                            @Override
                            public void change_imageview(int flag) {
                                if (flag == changeTobackground) {
                                    top_area.setBackgroundColor(Color.parseColor("#C1F3B4"));
                                } else if (flag == changeToCreate) {
                                    top_area.setBackgroundColor(Color.parseColor("#E8E8E8"));
                                    //上面的图标一闪一闪
                                } else if (flag == changeToNormal) {
                                    top_area.setBackgroundColor(Color.parseColor("#E8E8E8"));
                                    //一闪一闪的动画取消
                                } else if (flag == changeToDragview) {
                                    //Dragview缩放到50%
                                    //建立文件夹图标增大到120%
                                }


//                                PropertyValuesHolder pvh1=PropertyValuesHolder.ofFloat("scaleX",0.5f);
//                                PropertyValuesHolder pvh2=PropertyValuesHolder.ofFloat("scaleY",0.5f);
//                                ObjectAnimator.ofPropertyValuesHolder(view, pvh1, pvh2).setDuration(100).start();
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
            try {
                listfile = fileUtils.getSD(MainActivity.this);
                LogUtils.loggxl(listfile.size()+"changdu");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
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
            } else {
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
    class moveNeedTofile_task extends AsyncTask<String, Void, Void> {
        Dialog MyDialog;
        Dialog finishDialog;

        @Override
        protected void onPreExecute() {
            MyDialog = myDialog.createLoadingDialog(MainActivity.this, "正在移动照片");
            finishDialog = myDialog.createLoadingfinishDialog(MainActivity.this, "已存入相册夹");
            MyDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            MoveNeedTofile(params[0]);
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
            change_listview();
        }
    }


    /**
     * 将选好的图片移动到指定的文件夹
     */
    class moveNeedfile_task extends AsyncTask<String, Void, Void> {
        Dialog MyDialog;
        Dialog finishDialog;

        @Override
        protected void onPreExecute() {
            MyDialog = myDialog.createLoadingDialog(MainActivity.this, "正在移动照片");
            finishDialog = myDialog.createLoadingfinishDialog(MainActivity.this, "已存入相册夹");
            MyDialog.show();
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
            finishDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finishDialog.dismiss();
                }
            }, 500);
            change_listview();
        }
    }

    /**
     * 创建新的文件夹时候调用
     *
     * @param path
     */
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
                .getPath() + myApplication.move_file_path + "/" + path);
        if (!movefile.exists()) {
            movefile.mkdir();
        }
        for (File file : needmovelistfile) {
            fileUtils.copyFile(file.getAbsolutePath(), Environment
                    .getExternalStorageDirectory().getPath()
                    + myApplication.move_file_path + "/" + path + "/" + file.getName());
        }
    }


    /**
     * 在移动到已经存在的文件夹中调用
     *
     * @param path
     */
    public void MoveNeedTofile(final String path) {
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
            relativeLayout.setBackgroundResource(R.drawable.yujiazai);
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
                    mjianliImageview.setImageResource(R.drawable.jianli_green);
                    jiantou.setVisibility(View.VISIBLE);
                    setFlickerAnimation(jiantou, 1, 0);
                    group = new MovePhotoGroup(
                            MainActivity.this);
                    LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                            LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            ScreenUtils.dip2px(MainActivity.this, 108),
                            ScreenUtils.dip2px(MainActivity.this, 108));
                    lp.topMargin = x;
                    lp.leftMargin = y - 50;
                    view = new DragView(MainActivity.this,
                            BitmapUtils.fileTobitmap(new File(path), 206, 206), Pos, chakanPos, top_area_height, listview.getLeft());
                    view.setMlistener(new DragView.createFilelistener() {

                        @Override
                        public void createFile() {
                            layout.removeView(group);
                            Createdialog();
                        }

                        @Override
                        public void betrue_createFile(int flag) {
                            if (flag == 1) {
                                setScaleAnimation(view, 0.6f);
                                setFlickerAnimation(mjianliImageview, 1, 0.5f);
                                setScaleAnimation(mjianliImageview, 1.05f);
                            } else {
                                mjianliImageview.clearAnimation();
                                setScaleAnimation(view, 1f);
                                setScaleAnimation(mjianliImageview, 1f);
                            }
                        }

                        @Override
                        public void remove_view() {
                            mjianliImageview.setImageResource(R.drawable.jianli);
                            relativeLayout.setBackgroundResource(0);
                            jiantou.clearAnimation();
                            jiantou.setVisibility(View.GONE);
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
                            Movedialog();
//                            Intent intent = new Intent(MainActivity.this, showPhoto.class);
//                            intent.putExtra("flag", 1);
//                            startActivityForResult(intent, 1);
                        }

                        @Override
                        public void change_imageview(int flag) {
                            if (flag == changeTobackground) {
                                top_area.setBackgroundColor(Color.parseColor("#C1F3B4"));
                            } else if (flag == changeToCreate) {
                                top_area.setBackgroundColor(Color.parseColor("#E8E8E8"));
                                //上面的图标一闪一闪
                            } else if (flag == changeToNormal) {
                                top_area.setBackgroundColor(Color.parseColor("#E8E8E8"));
                                //一闪一闪的动画取消
                            } else if (flag == changeToDragview) {
                                //Dragview缩放到50%
                                //建立文件夹图标增大到120%
                            }
                        }
                    });
                    group.addView(view, lp);
                    layout.addView(group, lp1);
                    listview.setClickable(false);
                    listview.setFocusable(false);
                    listview.setOnTouchListener(new OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            view.onTouchEvent(event);
                            return true;
                        }
                    });
                    group.setOnTouchListener(new OnTouchListener() {

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return true;
                        }
                    });
                }
            });
        }
    }

    /**
     * 用来弹出创建新的文件夹的对话框
     */
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
                changeToNomal();
            }
        });
        information = fileUtils.getNeedmoveFileLocation(needMoveFile.getNeedmoveFile());
        LogUtils.loggxl("weidu " + information[0] + " jindu" + information[1] + "shijian" + information[1]);
        path_edittext = (EditText) layout
                .findViewById(R.id.path);
        new GetCityname_Tack().execute();
        path_edittext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                path_edittext.setCursorVisible(true);
                path_edittext.setTextColor(Color.parseColor("#B7B7B7"));
            }
        });
        setEditTextCursorLocation(path_edittext);
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

        ImageView wenjianjia_photo = (ImageView) layout.findViewById(R.id.wenjianjia_photo);
        imageLoader.displayImage("file:///" + needMoveFile.getNeedmoveFile().get(0), wenjianjia_photo,
                options);
        ImageView cancle = (ImageView) layout.findViewById(R.id.cancle);
        cancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    /**
     * 用来弹出移动到原来文件夹的对话框
     */

    public void Movedialog() {
        LayoutInflater inflaterDl = LayoutInflater.from(this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(
                R.layout.move_dialog, null);
        final Dialog dialog = new Dialog(MainActivity.this, R.style.Dialog_FS);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                changeToNomal();
            }
        });
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth());
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setContentView(layout);
        ListView listview = (ListView) layout.findViewById(R.id.showphoto_listview);
        filepathlist = fileUtils.getExistFileList(Environment.getExternalStorageDirectory().getPath() + myApplication.move_file_path);
        showphoto_adapter = new movephoto_listviewAdapter(MainActivity.this, filepathlist);
        listview.setAdapter(showphoto_adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                relativeLayout.setBackgroundResource(0);
                mjianliImageview.setImageResource(R.drawable.jianli);
                LogUtils.loggxl("zxc" + filepathlist.get(position));
                new moveNeedTofile_task().execute(filepathlist.get(position));
                Dialog_flag = 1;
                dialog.dismiss();
            }
        });
        ImageView cancle = (ImageView) layout.findViewById(R.id.cancle);
        cancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public void Createdeletedialog() {
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
            relativeLayout.setBackgroundResource(R.drawable.yujiazai);
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
                    mjianliImageview.setImageResource(R.drawable.jianli_green);
                    jiantou.setVisibility(View.VISIBLE);
                    setFlickerAnimation(jiantou, 1, 0);
                    group = new MovePhotoGroup(
                            MainActivity.this);
                    LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                            LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            ScreenUtils.dip2px(MainActivity.this, 108),
                            ScreenUtils.dip2px(MainActivity.this, 108));
                    lp.topMargin = x;
                    lp.leftMargin = y - 50;
                    view = new DragView(MainActivity.this,
                            BitmapUtils.fileTobitmap(new File(path), 206, 206), Pos, chakanPos, top_area_height, listview.getLeft());
                    view.setMlistener(new DragView.createFilelistener() {

                        @Override
                        public void createFile() {
                            layout.removeView(group);
                            Createdialog();
                        }

                        @Override
                        public void betrue_createFile(int flag) {
                            if (flag == 1) {
                                setScaleAnimation(view, 0.6f);
                                setFlickerAnimation(mjianliImageview, 1, 0.5f);
                                setScaleAnimation(mjianliImageview, 1.05f);
                            } else {
                                mjianliImageview.clearAnimation();
                                setScaleAnimation(view, 1f);
                                setScaleAnimation(mjianliImageview, 1f);
                            }
                        }

                        @Override
                        public void remove_view() {
                            mjianliImageview.setImageResource(R.drawable.jianli);
                            jiantou.clearAnimation();
                            jiantou.setVisibility(View.GONE);
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
                            Movedialog();
                        }

                        @Override
                        public void change_imageview(int flag) {
                            if (flag == changeTobackground) {
                                top_area.setBackgroundColor(Color.parseColor("#C1F3B4"));
                            } else if (flag == changeToCreate) {
                                top_area.setBackgroundColor(Color.parseColor("#E8E8E8"));
                            } else if (flag == changeToNormal) {
                                top_area.setBackgroundColor(Color.parseColor("#E8E8E8"));
                            } else if (flag == changeToDragview) {
                                //Dragview缩放到50%
                                //建立文件夹图标增大到120%
                            }
                        }
                    });
                    group.addView(view, lp);
                    layout.addView(group, lp1);
                    listview.setClickable(false);
                    listview.setFocusable(false);
                    listview.setOnTouchListener(new OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            view.onTouchEvent(event);
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

    /**
     * 给imageview添加闪烁的效果
     *
     * @param iv_chat_head
     */
    private void setFlickerAnimation(ImageView iv_chat_head, float from, float to) {
        final Animation animation = new AlphaAnimation(from, to); // Change alpha from fully visible to invisible
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); //
        iv_chat_head.setAnimation(animation);
    }

    /**
     * 缩放的大小
     *
     * @param view
     * @param to
     */
    private void setScaleAnimation(View view, float to) {
        PropertyValuesHolder pvh1 = PropertyValuesHolder.ofFloat("scaleX", to);
        PropertyValuesHolder pvh2 = PropertyValuesHolder.ofFloat("scaleY", to);
        ObjectAnimator.ofPropertyValuesHolder(view, pvh1, pvh2).setDuration(100).start();
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
     * 根据经纬度获取城市名
     */
    class GetCityname_Tack extends AsyncTask<Void, Integer, String> {
        String cityname;

        @Override
        protected String doInBackground(Void... params) {
            if ((!information[0].equals("nothing")) && (!information[1].equals("nothing"))) {
                cityname = GetAddr(information[0], information[1]);
            }
            return cityname;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (cityname != null) {
                path_edittext.setText(information[2] + cityname);
            } else {
                path_edittext.setText(information[2]);
            }
        }
    }

    /**
     * 根据经纬度获取城市名称
     *
     * @param latitude
     * @param longitude
     * @return
     */
    public String GetAddr(String latitude, String longitude) {
        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        StringBuilder stringBuilder = null;
        try {
            List<Address> addresses = geocoder.getFromLocation(Float.valueOf(latitude), Float.valueOf(longitude), 1);
            stringBuilder = new StringBuilder();
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                stringBuilder.append(address.getLocality());
                System.out.println(stringBuilder.toString());
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Toast.makeText(this, "报错", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 对话框取消后重新回到主界面，去除之前的效果
     */
    void changeToNomal() {
        mjianliImageview.clearAnimation();
        jiantou.clearAnimation();
        mjianliImageview.clearAnimation();
        setScaleAnimation(mjianliImageview, 1f);
        jiantou.setVisibility(View.GONE);
        top_area.setBackgroundColor(Color.parseColor("#E8E8E8"));
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


}



