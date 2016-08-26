package com.example.gxl.photofinishing;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.zhy.autolayout.AutoLayoutActivity;

import java.io.File;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import eventbustype.ChaKanPhotoType;
import eventbustype.ShowPhotoDetailType;
import fragment.ImageDetailFragment;
import utils.fileUtils;

/**
 * 整理好的照片查看器
 */
public class ArrangePhotoViewerActivity extends AutoLayoutActivity implements View.OnClickListener {
    ViewPager viewpaper;
    ArrayList<View> list;
    ArrayList<String> filepathlist;
    int current_position;
    ImageView houtui;

    RelativeLayout show_move_detail;
    TextView huanyuan_textview;
    TextView shanchu_textview;

    int delete_flag = 4;
    int huanyuan_flag = 5;

    viewpaperAdapter adapter;
    Dialog dialog;

    int gaibian_flag = 0;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    other.ImageLoader mImageLoader;

    private static final String STATE_POSITION = "STATE_POSITION";
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_URLS = "image_urls";

    private final int ReturnDelete = 1;
    private final int ReturnRestore = 2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//用来取消状态栏
        setContentView(R.layout.chakanphoto_layout);
        show_move_detail = (RelativeLayout) findViewById(R.id.show_move_detail);
        huanyuan_textview = (TextView) findViewById(R.id.huanyuan);
        shanchu_textview = (TextView) findViewById(R.id.delete);
        houtui = (ImageView) findViewById(R.id.houtui);

        houtui.setOnClickListener(this);
        huanyuan_textview.setOnClickListener(this);
        shanchu_textview.setOnClickListener(this);

        mImageLoader = other.ImageLoader.build(ArrangePhotoViewerActivity.this);

        filepathlist = getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS);
        current_position = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
        viewpaper = (ViewPager) findViewById(R.id.viewPager);
        adapter = new viewpaperAdapter(getSupportFragmentManager(), filepathlist);
        viewpaper.setAdapter(adapter);
        viewpaper.setCurrentItem(current_position);
        viewpaper.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                current_position = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    class viewpaperAdapter extends FragmentStatePagerAdapter {

        public ArrayList<String> fileList;

        public viewpaperAdapter(FragmentManager fm, ArrayList<String> fileList) {
            super(fm);
            this.fileList = fileList;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }


        @Override
        public int getCount() {
            return fileList == null ? 0 : fileList.size();
        }

        @Override
        public Fragment getItem(int position) {
            String url = fileList.get(position);
            Log.i("zxcv", url);
            return ImageDetailFragment.newInstance(url);
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
                "test Page", // TODO: Define a title for the content shown.
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
                "test Page", // TODO: Define a title for the content shown.
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.houtui:
                Intent intent = new Intent();
                setResult(gaibian_flag, intent);
                finish();
                break;

            case R.id.huanyuan:
                Createdialog(huanyuan_flag);
                break;

            case R.id.delete:
                Createdialog(delete_flag);
                break;
        }
    }

    /**
     * 弹出提示对话框
     */
    public void Createdialog(final int view_flag) {
        final View dialogView = LayoutInflater.from(ArrangePhotoViewerActivity.this).inflate(R.layout.delete_dialog, null);
        dialog = new Dialog(ArrangePhotoViewerActivity.this);
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
        dialog_title.setText("确认删除");
        delete_text.setText("删除的照片将不能再次还原");
        if (view_flag == huanyuan_flag) {
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
                if (view_flag == delete_flag) {
                    Log.i("path", "直接删除");
                    new deletefile_task().execute();
                    EventBus.getDefault().post(new ChaKanPhotoType(ReturnDelete));
                    EventBus.getDefault().post(new ShowPhotoDetailType(ReturnDelete));
                } else {
                    Log.i("path", "返回到远处");
                    new huanyuanfile_task().execute();
                    EventBus.getDefault().post(new ChaKanPhotoType(ReturnRestore));
                    EventBus.getDefault().post(new ShowPhotoDetailType(ReturnRestore));
                }
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
            fileUtils.deleteFile(new File(filepathlist.get(current_position)));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            filepathlist.remove(filepathlist.get(current_position));
            adapter = new viewpaperAdapter(getSupportFragmentManager(), filepathlist);
            viewpaper.setAdapter(adapter);
            dialog.dismiss();
            gaibian_flag = 2;
            super.onPostExecute(aVoid);
        }
    }

    /**
     * 将选中的文件还原到原来的位置
     */
    class huanyuanfile_task extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            fileUtils.movePhoto(filepathlist.get(current_position));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            filepathlist.remove(filepathlist.get(current_position));
            adapter = new viewpaperAdapter(getSupportFragmentManager(), filepathlist);
            viewpaper.setAdapter(adapter);
            dialog.dismiss();
            gaibian_flag = 1;
            super.onPostExecute(aVoid);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            setResult(gaibian_flag, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
