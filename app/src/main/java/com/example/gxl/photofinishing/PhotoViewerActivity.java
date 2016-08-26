package com.example.gxl.photofinishing;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zhy.autolayout.AutoLayoutActivity;

import java.io.File;
import java.util.ArrayList;


import data.needMoveFile;
import de.greenrobot.event.EventBus;
import eventbustype.TestEventType;
import fragment.ImageDetailFragment;
import utils.fileUtils;

/**
 * Created by gxl on 2016/4/1.
 */
public class PhotoViewerActivity extends AutoLayoutActivity implements View.OnClickListener {
    ViewPager viewpaper;
    ArrayList<View> list;
    ArrayList<String> filepathlist;
    int position;
    TextView show_detail;
    ImageView chose_detail;
    ImageView move;
    int dangqian_index;
    ImageView houtui;

    TextView chose_text;
    TextView quit;
    TextView delete;
    RelativeLayout show_move_detail;
    viewpaperAdapter adapter;
    Dialog dialog;

    int gaibian_flag = 0;

    int mListViewPosition;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    other.ImageLoader mImageLoader;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    private static final String STATE_POSITION = "STATE_POSITION";
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_URLS = "image_urls";

    private static final int FLAG_UNDELETE = 1;
    private static final int FLAF_DELETE = 2;

    private static int mFlag = FLAG_UNDELETE;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//用来取消状态栏
        imageLoader.init(ImageLoaderConfiguration.createDefault(PhotoViewerActivity.this));
        options = new DisplayImageOptions.Builder()
                .cacheOnDisc(true)
                .cacheInMemory(false)
                .showStubImage(R.drawable.yujiazai)
                .showImageForEmptyUri(R.drawable.yujiazai)
                .showImageOnFail(R.drawable.yujiazai).cacheInMemory()
                .cacheOnDisc().displayer(new RoundedBitmapDisplayer(20))
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .displayer(new FadeInBitmapDisplayer(300)).build();
        setContentView(R.layout.showphoto_layout);
        show_move_detail = (RelativeLayout) findViewById(R.id.show_move_detail);
        delete = (TextView) findViewById(R.id.delete);
        delete.setOnClickListener(this);
        houtui = (ImageView) findViewById(R.id.houtui);
        show_detail = (TextView) findViewById(R.id.show_detail);
        chose_detail = (ImageView) findViewById(R.id.chose_detail);
        chose_detail.setOnClickListener(this);
        houtui.setOnClickListener(this);
        mImageLoader = other.ImageLoader.build(PhotoViewerActivity.this);
        filepathlist = getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS);
        position = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
        mListViewPosition = getIntent().getIntExtra(STATE_POSITION, 0);
        viewpaper = (ViewPager) findViewById(R.id.viewPager);
        adapter = new viewpaperAdapter(getSupportFragmentManager(), filepathlist);
        viewpaper.setOffscreenPageLimit(2);
        viewpaper.setAdapter(adapter);
        viewpaper.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                dangqian_index = position;
                if (needMoveFile.isinNeedmovefile(filepathlist.get(position))) {
                    chose_detail.setImageResource(R.drawable.check_choose);
                } else {
                    chose_detail.setImageResource(R.drawable.check_unchoose);
                }
                show_detail.setText((++position) + "/" + filepathlist.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewpaper.setCurrentItem(position);
        int index = position + 1;
        if (needMoveFile.isinNeedmovefile(filepathlist.get(position))) {
            chose_detail.setImageResource(R.drawable.check_choose);
        } else {
            chose_detail.setImageResource(R.drawable.check_unchoose);
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private class viewpaperAdapter extends FragmentStatePagerAdapter {

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
            case R.id.chose_detail:
                if (!needMoveFile.isinNeedmovefile(filepathlist.get(dangqian_index))) {
                    chose_detail.setImageResource(R.drawable.check_choose);
                    needMoveFile.addNeedmovefile(filepathlist.get(dangqian_index));
                    mFlag = FLAF_DELETE;
                } else {
                    chose_detail.setImageResource(R.drawable.check_unchoose);
                    needMoveFile.removefile(filepathlist.get(dangqian_index));
                    mFlag = FLAF_DELETE;
                }
                break;
            case R.id.houtui:
                if (mFlag == FLAF_DELETE) {
                    EventBus.getDefault().post(new TestEventType(filepathlist, mListViewPosition));
                }
                finish();
                break;

            case R.id.delete:
                Createdeletedialog();
                break;
        }
    }

    public void Createdeletedialog() {
        final View dialogView = LayoutInflater.from(PhotoViewerActivity.this).inflate(R.layout.delete_main_dialog, null);
        dialog = new Dialog(PhotoViewerActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth());
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setContentView(dialogView);
        final RelativeLayout checkbox = (RelativeLayout) dialogView.findViewById(R.id.checkbox);
        RelativeLayout quxiao = (RelativeLayout) dialogView.findViewById(R.id.quxiao);
        RelativeLayout queding = (RelativeLayout) dialogView.findViewById(R.id.queding);
        TextView delete_text = (TextView) dialogView.findViewById(R.id.shanchutext);
        delete_text.setText("确定要删除该照片吗?");
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
                gaibian_flag = 2;
                mFlag = FLAF_DELETE;
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
            fileUtils.deleteFile(new File(filepathlist.get(dangqian_index)));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            String path = filepathlist.get(dangqian_index);
            needMoveFile.removefile(filepathlist.get(dangqian_index));
            dangqian_index = 0;
            filepathlist.remove(path);
            adapter = new viewpaperAdapter(getSupportFragmentManager(), filepathlist);
            viewpaper.setAdapter(adapter);
            chose_detail.setImageResource(R.drawable.check_unchoose);
            dialog.dismiss();
            super.onPostExecute(aVoid);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mFlag == FLAF_DELETE) {
                EventBus.getDefault().post(new TestEventType(filepathlist, mListViewPosition));
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
