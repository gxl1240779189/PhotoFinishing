package com.example.gxl.photofinishing;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.v4.view.PagerAdapter;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zhy.autolayout.AutoLayoutActivity;

import java.io.File;
import java.util.ArrayList;

import Adapter.showphoto_listviewAdapter;
import Data.needMoveFile;
import Utils.BitmapUtils;
import Utils.CacheUtils;
import Utils.fileUtils;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by gxl on 2016/4/1.
 */
public class test extends AutoLayoutActivity implements View.OnClickListener {
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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    other.ImageLoader mImageLoader;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//用来取消状态栏
        setContentView(R.layout.showphoto_layout);
        show_move_detail = (RelativeLayout) findViewById(R.id.show_move_detail);
        chose_text = (TextView) findViewById(R.id.chose_text);
        quit = (TextView) findViewById(R.id.quit);
        delete = (TextView) findViewById(R.id.delete);
        quit.setOnClickListener(this);
        delete.setOnClickListener(this);
        houtui = (ImageView) findViewById(R.id.houtui);
        show_detail = (TextView) findViewById(R.id.show_detail);
        chose_detail = (ImageView) findViewById(R.id.chose_detail);
        init();
        chose_detail.setOnClickListener(this);
        houtui.setOnClickListener(this);
        mImageLoader = other.ImageLoader.build(test.this);
        filepathlist = getIntent().getStringArrayListExtra("listfilepath");
        position = getIntent().getIntExtra("position", 0);
        viewpaper = (ViewPager) findViewById(R.id.viewPager);
        adapter=new viewpaperAdapter(filepathlist);
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
        show_detail.setText(index + "/" + filepathlist.size());
        if (needMoveFile.isinNeedmovefile(filepathlist.get(position))) {
            chose_detail.setImageResource(R.drawable.check_choose);
        } else {
            chose_detail.setImageResource(R.drawable.check_unchoose);
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    class viewpaperAdapter extends PagerAdapter {
        ArrayList<View> list = new ArrayList<View>();
        public viewpaperAdapter(
                ArrayList<String> filepathlist) {
            for (int i = 0; i < filepathlist.size(); i++) {
                PhotoView imageview = new PhotoView(test.this);
                ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, ViewGroup.MarginLayoutParams.MATCH_PARENT);
                imageview.setLayoutParams(lp);
                list.add(imageview);
            }
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        public int getCount() {
            return filepathlist.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView imageView = (PhotoView) list.get(position);
            mImageLoader.bindBitmap(filepathlist.get(position), imageView, 400, 400);
            if (position < list.size() - 3) {
                PhotoView imageView_later = (PhotoView) list.get(position + 1);
                mImageLoader.bindBitmap(filepathlist.get(position + 1), imageView_later, 400, 400);
            }
            if (position > 0) {
                PhotoView imageView_pre = (PhotoView) list.get(position - 1);
                mImageLoader.bindBitmap(filepathlist.get(position - 1), imageView_pre, 400, 400);
            }
            container.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return list.get(position);
        }
    }

    /**
     * 用来判断是否显示底部detail
     */
    void init() {
        if (needMoveFile.needmoveFile.size() > 0) {
            show_move_detail.setVisibility(View.VISIBLE);
            chose_text.setText("已选" + needMoveFile.needmoveFile.size() + "张");

        } else {
            show_move_detail.setVisibility(View.GONE);
        }
    }

    /**
     * 改变删除之后的viewpaper的显示
     */
    void change_viewpaper() {

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
                } else {
                    chose_detail.setImageResource(R.drawable.check_unchoose);
                    needMoveFile.removefile(filepathlist.get(dangqian_index));
                }
                init();
                break;

            case R.id.houtui:
                Intent intent=new Intent();
                setResult(2,intent);
                finish();
                break;

            case R.id.quit:
                needMoveFile.removeall();
                chose_detail.setImageResource(R.drawable.check_unchoose);
                show_move_detail.setVisibility(View.GONE);
                break;

            case R.id.delete:
                Createdeletedialog();
                break;
        }
    }

    public void Createdeletedialog() {
        final View dialogView = LayoutInflater.from(test.this).inflate(R.layout.delete_main_dialog, null);
        dialog = new Dialog(test.this);
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
            }
        });
        dialog.show();
    }

    /**
     * 将选中的文件直接删除
     */
    class deletefile_task extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... params) {
            fileUtils.deleteFilelist(needMoveFile.getNeedmoveFile());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            for(int i=0;i<needMoveFile.getNeedmoveFile().size();i++)
            {
                filepathlist.remove(needMoveFile.getNeedmoveFile().get(i));
            }
            needMoveFile.removeall();
            adapter=new viewpaperAdapter(filepathlist);
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
            chose_detail.setImageResource(R.drawable.check_unchoose);
            show_move_detail.setVisibility(View.GONE);
            dialog.dismiss();
            super.onPostExecute(aVoid);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent=new Intent();
            setResult(2,intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
