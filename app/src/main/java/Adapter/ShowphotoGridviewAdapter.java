package Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gxl.photofinishing.R;
import com.example.gxl.photofinishing.showPhoto;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Data.needMoveFile;
import Utils.fileUtils;

/**
 * Created by gxl on 2016/3/31.
 */
public class ShowphotoGridviewAdapter extends BaseAdapter  implements  DragGridBaseAdapter {

    Context context;
    ArrayList<String> listfilepath = new ArrayList<String>();
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private int mHidePosition = -1;



    public ShowphotoGridviewAdapter(Context context, ArrayList<String> list) {
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


    public View getView(int position, View convertView, ViewGroup parent) {
        final int mPosition=position;
        View view = null;
        view = LayoutInflater.from(context).inflate(R.layout.showphotogridviewitem,
                    null);
        if(listfilepath.get(position)=="创建")
        {
            ((ImageView)view.findViewById(R.id.show_image)).setImageResource(R.drawable.yujiazai);
            ((TextView)view.findViewById(R.id.show_text)).setText("创建");
        }else {
            String imageviewpath = fileUtils.getExistFileBitmap(listfilepath.get(position));
            imageLoader.displayImage("file:///" + imageviewpath,(ImageView)view.findViewById(R.id.show_image),
                    options);
            ((TextView)view.findViewById(R.id.show_text)).setText(listfilepath.get(position).substring(listfilepath.get(position).lastIndexOf("/") + 1, listfilepath.get(position).length()));
        }
        if(position == mHidePosition){
            view.setVisibility(View.INVISIBLE);
        }
        return view;
    }


    @Override
    public void reorderItems(int oldPosition, int newPosition) {

    }

    @Override
    public void setHideItem(int hidePosition) {
    }
}

