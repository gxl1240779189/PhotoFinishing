package com.example.gxl.photofinishing;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.text.Selection;
import android.text.Spannable;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ikimuhendis.ldrawer.ActionBarDrawerToggle;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.autolayout.AutoLayoutActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import Presenter.PhotoLoadPresenter;
import adapter.ListviewAdapter;
import adapter.movephoto_listviewAdapter;
import application.myApplication;
import customview.DragView;
import customview.MovePhotoGroup;
import customview.myDialog;
import data.needMoveFile;
import data.shezhiSharedprefrence;
import de.greenrobot.event.EventBus;
import eventbustype.FirstEventType;
import eventbustype.TestEventType;
import utils.BitmapUtils;
import utils.LogUtils;
import utils.ScreenUtils;
import utils.fileUtils;
import view.PhotoLoadViewInterface;

/**
 * Created by Administrator on 2016/5/20 0020.
 */
public class firstpageActivity extends AutoLayoutActivity implements PhotoLoadViewInterface, View.OnClickListener {


    /**
     * flag用来表示对话框是移动图片成功时消失，还是返回消失
     */
    int Dialog_flag = 0;
    /**
     * backgroud_flag用来表示当前listview是否为空
     */
    int Backgroud_flag = 0;
    /**
     * 顶部的relationlayout
     */
    RelativeLayout top_area;
    LinearLayout listviewlinearlayout;
    FrameLayout listview_framelayout;

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
     * 创建侧滑菜单需要使用的参数
     */
    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerArrowDrawable drawerArrow;
    private boolean drawerArrowColor;
    private ImageView control_menu;
    private ImageView chakan_file;

    ListView mListview;
    PhotoLoadPresenter mPhotoLoadPresenter;
    FrameLayout layout;

    ImageView mjianliImageview;

    //Dragview是否出现，0不出现，1出现
    int Dragview_flag = 0;

    /*
      分析中对话框
      正在移动中对话框
      分析完成对话框
     */
    Dialog LoadingDialog;
    Dialog MovingDialog;
    Dialog FinishDialog;

    ListviewAdapter listviewadapter;

    MovePhotoGroup group;
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

    int Pos[] = {-1, -1, -1, -1};
    int chakanPos[] = {-1, -1, -1, -1};

    DragView view;

    //用来获取到已经移动好的文件夹
    private ArrayList<String> filepathlist;
    private movephoto_listviewAdapter showphoto_adapter;

    private shezhiSharedprefrence shezhiSP;

    private final int ReturnChanged = 1;            //返回时相册数据更新

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//用来取消状态栏
        setContentView(R.layout.activity_sample);
        EventBus.getDefault().register(this);
        InitView();//初始化View和设置监听，资源
        init_LDrawer();//初始化菜单
        mPhotoLoadPresenter = new PhotoLoadPresenter(this, firstpageActivity.this);
        mPhotoLoadPresenter.InitListview();
    }

    //初始化侧滑菜单
    private void init_LDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (LinearLayout) findViewById(R.id.navdrawer);
        RelativeLayout guanli = (RelativeLayout) mDrawerList.findViewById(R.id.guanli);
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

    //初始化View和设置监听，资源
    @Override
    public void InitView() {
        //初始化menu点击事件
        control_menu = (ImageView) findViewById(R.id.menu);
        control_menu.setOnClickListener(this);
        //初始化查看文件夹按钮点击事件
        chakan_file = (ImageView) findViewById(R.id.chakan);
        chakan_file.setOnClickListener(this);

        layout = (FrameLayout) findViewById(R.id.myFrameLayout);
        mjianliImageview = (ImageView) findViewById(R.id.jianli);
        jiantou = (ImageView) findViewById(R.id.jiantou);
        mListview = (ListView) findViewById(R.id.mylistview);
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

        LoadingDialog = myDialog.createLoadingDialog(firstpageActivity.this, "正在分析照片");
        MovingDialog = myDialog.createLoadingDialog(firstpageActivity.this, "正在移动中");
        FinishDialog = myDialog.createLoadingfinishDialog(firstpageActivity.this, "已完成");
    }

    /**
     * 正在加载listview时出现的动画效果，即出现“正在分析中”的画面
     */
    @Override
    public void LoadingData() {
        LoadingDialog.show();
    }

    /**
     * 数据分析完成，即出现“分析完成”的画面
     */
    @Override
    public void LoadDataFinish() {
        LoadingDialog.dismiss();
        MovingDialog.dismiss();
        FinishDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FinishDialog.dismiss();
            }
        }, 500);
    }

    /**
     * 移动相片中。即出现“正在移动中”的画面
     */
    @Override
    public void MovingData() {
        MovingDialog.show();
    }

    /**
     * 将分析好的数据装载到listview中去，通过设配器
     *
     * @param filemap
     */

    @Override
    public void LoadListviewSuccess(LinkedHashMap<String, ArrayList<String>> filemap) {
        listviewadapter = new ListviewAdapter(firstpageActivity.this, filemap, new ListviewAdapter.show_choose_detail_Listener() {
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
        mListview.setAdapter(listviewadapter);
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
                mPhotoLoadPresenter.CreateMoveGroup(x, y, path);
            }
        });
        mjianliImageview.getLocationOnScreen(Pos);
    }

    /**
     * 如果分析好的数据为空，做出相应的处理，显示当前无数据的页面
     */
    @Override
    public void LoadlistviewFail() {
        relativeLayout.setBackgroundResource(R.drawable.yujiazai);
        mListview.setVisibility(View.GONE);
    }

    /**
     * 改变侧边菜单的开闭状态
     */
    @Override
    public void ChangeMenuState() {
        if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            mDrawerLayout.openDrawer(mDrawerList);
            mDrawerList.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }
    }

    /**
     * 将页面变成普通状态
     */
    @Override
    public void ChangeToNormal() {
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
        mListview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }


    /**
     * 创建一个delete对话框
     */
    @Override
    public void CreateDeleteDialog() {
        final View dialogView = LayoutInflater.from(firstpageActivity.this).inflate(R.layout.delete_main_dialog, null);
        final Dialog dialog = new Dialog(firstpageActivity.this);
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
                mPhotoLoadPresenter.DeleteFileTask();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 用来弹出创建新的文件夹的对话框
     */
    @Override
    public void CreateNewFileDialog(String filename) {
        Dialog_flag = 0;
        Backgroud_flag = 0;
        LayoutInflater inflaterDl = LayoutInflater.from(this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(
                R.layout.dialog_main_info, null);
        final Dialog dialog = new Dialog(firstpageActivity.this, R.style.Dialog_FS);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ChangeToNormal();
            }
        });
        path_edittext = (EditText) layout
                .findViewById(R.id.path);
        path_edittext.setText(filename);
        path_edittext.setOnClickListener(new View.OnClickListener() {
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
        btnOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!path_edittext.getText().toString().equals("")) {
                    mPhotoLoadPresenter.MovePhotoToNew(path_edittext.getText().toString());
                } else {
                    mPhotoLoadPresenter.MovePhotoToNew("无标题");
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
        cancle.setOnClickListener(new View.OnClickListener() {
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
    @Override
    public void CreateMoveToFileDialog() {
        LayoutInflater inflaterDl = LayoutInflater.from(this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(
                R.layout.move_dialog, null);
        final Dialog dialog = new Dialog(firstpageActivity.this, R.style.Dialog_FS);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ChangeToNormal();
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
        showphoto_adapter = new movephoto_listviewAdapter(firstpageActivity.this, filepathlist);
        listview.setAdapter(showphoto_adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                relativeLayout.setBackgroundResource(0);
                mjianliImageview.setImageResource(R.drawable.jianli);
                mPhotoLoadPresenter.MovePhotoToExistFile(filepathlist.get(position));
                Dialog_flag = 1;
                dialog.dismiss();
            }
        });
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
     * 创建一个可以移动的图片集
     *
     * @param x
     * @param y
     * @param path
     */

    @Override
    public void CreateMoveGroup(int x, int y, String path) {
        Dragview_flag = 1;
        show_move_detail.setVisibility(View.GONE);
        relativeLayout.setBackgroundResource(R.drawable.white_copy);
        mjianliImageview.setImageResource(R.drawable.jianli_green);
        jiantou.setVisibility(View.VISIBLE);
        setFlickerAnimation(jiantou, 1, 0);
        group = new MovePhotoGroup(
                firstpageActivity.this);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ScreenUtils.dip2px(firstpageActivity.this, 108),
                ScreenUtils.dip2px(firstpageActivity.this, 108));
        lp.topMargin = x - 25;
        lp.leftMargin = y - 50;
        int listviewlinearlayout_top = listviewlinearlayout.getTop();
        int listview_top = mListview.getTop();
        int listviewframelayout_top = listview_framelayout.getTop();
        top_area_height = listview_top + listviewlinearlayout_top + listviewframelayout_top;
        view = new DragView(firstpageActivity.this,
                BitmapUtils.fileTobitmap(new File(path), 206, 206), Pos, chakanPos, top_area_height, mListview.getLeft());
        view.setLayoutParams(lp);
        view.setMlistener(new DragView.createFilelistener() {
            @Override
            public void createFile() {
                layout.removeView(group);
                mPhotoLoadPresenter.ShowNewDialog();
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
                mListview.setOnTouchListener(new View.OnTouchListener() {
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
                mPhotoLoadPresenter.ShowMovePhotoToExistFileDialog();
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
        layout.addView(group, lp1);
        group.addView(view, lp);
        mListview.setClickable(false);
        mListview.setFocusable(false);
        final MotionEvent toucheevent;
        mListview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                view.onTouchEvent(event);
                return true;
            }
        });
    }

    /**
     * 将listview中的Gridview中的item变成网格状的格子
     */
    @Override
    public void ChangeGridViewItem() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu:
                ChangeMenuState();
                break;

            case R.id.chakan:
                //跳转显示已经存在的文件夹
                mPhotoLoadPresenter.StartShowPhotoActivity();
                break;

            case R.id.quit:
                needMoveFile.removeall();
                listviewadapter.notifyDataSetChanged();
                show_move_detail.setVisibility(View.GONE);
                break;

            case R.id.delete:
                mPhotoLoadPresenter.ShowDeleteDialog();
                break;

            case R.id.guanli:
                //跳转到管理软件源的界面
                mPhotoLoadPresenter.StartguanlimenuActivity();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * EventBus的事件处理函数
     *
     * @param event
     */
    public void onEventMainThread(FirstEventType event) {
        switch (event.getFlag()) {
            case ReturnChanged:
                mPhotoLoadPresenter.InitListview();
                break;
        }
    }

    /**
     * EventBus的事件处理函数
     *
     * @param event
     */
    public void onEventMainThread(TestEventType event) {
        if (event.getFlag()) {
            mListview.getChildAt(event.getmListViewPosition()).findViewById(R.id.mygridview).findViewById(event.getPosition()).setVisibility(View.VISIBLE);
        } else {
            mListview.getChildAt(event.getmListViewPosition()).findViewById(R.id.mygridview).findViewById(event.getPosition()).setVisibility(View.GONE);
        }
        if (needMoveFile.needmoveFile.size() == 0) {
            show_move_detail.setVisibility(View.GONE);
        } else {
            show_move_detail.setVisibility(View.VISIBLE);
            chose_text.setText("已选" + needMoveFile.needmoveFile.size() + "张");
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

}
