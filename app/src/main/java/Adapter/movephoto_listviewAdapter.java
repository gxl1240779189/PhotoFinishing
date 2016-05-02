package Adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gxl.photofinishing.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.File;
import java.util.ArrayList;

import Data.needMoveFile;
import Utils.BitmapUtils;
import Utils.fileUtils;

/**
 * Created by Administrator on 2016/5/1 0001.
 */
public class movephoto_listviewAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> listfilepath = new ArrayList<String>();
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;


    public movephoto_listviewAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.listfilepath = list;
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.yujiazai)
                .showImageForEmptyUri(R.drawable.yujiazai)
                .showImageOnFail(R.drawable.yujiazai).cacheInMemory()
                .cacheOnDisc().displayer(new RoundedBitmapDisplayer(20))
                .displayer(new FadeInBitmapDisplayer(300)).build();
    }


    public int getCount() {
        return listfilepath.size();
    }


    public String getItem(int position) {
        return listfilepath.get(position);
    }


    public long getItemId(int position) {
        return position;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder viewholder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.movephoto_listviewitem, null);
            viewholder = new ViewHolder();
            view = convertView;
            viewholder.wenjianjia_photo = (ImageView) view.findViewById(R.id.wenjianjia_photo);
            viewholder.wenjianjia_title = (TextView) view.findViewById(R.id.wenjianjia_title);
            viewholder.wenjianjia_number = (TextView) view.findViewById(R.id.wenjianjia_number);
            convertView.setTag(viewholder);
        } else {
            view = convertView;
            viewholder = (ViewHolder) view.getTag();
        }
        if ((new File(listfilepath.get(position)).listFiles().length != 0)) {
            File file = new File(fileUtils.getExistFileBitmap(listfilepath.get(position)));
            imageLoader.displayImage("file:///" + fileUtils.getExistFileBitmap(listfilepath.get(position)), viewholder.wenjianjia_photo,
                    options);
            viewholder.wenjianjia_number.setText(new File(listfilepath.get(position)).listFiles().length + "");
        } else {
            viewholder.wenjianjia_photo.setBackgroundResource(R.drawable.wenjianjia_background);
            viewholder.wenjianjia_number.setText("0");
        }
        viewholder.wenjianjia_title.setText(listfilepath.get(position).substring(listfilepath.get(position).lastIndexOf("/") + 1, listfilepath.get(position).length()));
        return view;
    }

class ViewHolder {
    ImageView wenjianjia_photo;
    TextView wenjianjia_title;
    TextView wenjianjia_number;
}

}
