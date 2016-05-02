package Adapter;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import com.example.gxl.photofinishing.R;
import com.example.gxl.photofinishing.showPhoto;
import com.example.gxl.photofinishing.test;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import Data.needMoveFile;
import myView.myImageview;

public class GridviewAdapter extends BaseAdapter {

    Activity context;
    ArrayList<String> listfilepath = new ArrayList<String>();
    List<Boolean> listimageviewtip = new ArrayList<Boolean>();
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;


    public GridviewAdapter(Context context, ArrayList<String> list) {
        this.context = (Activity) context;
        this.listfilepath = list;
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .cacheOnDisc(true)
                .cacheInMemory(false)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showStubImage(R.drawable.yujiazai)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .showImageForEmptyUri(R.drawable.yujiazai)
                .showImageOnFail(R.drawable.yujiazai).cacheInMemory()
                .cacheOnDisc().displayer(new RoundedBitmapDisplayer(20))
                .displayer(new FadeInBitmapDisplayer(300)).build();
    }

    @Override
    public int getCount() {

        return listfilepath.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return listfilepath.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int mPosition = position;
        View view = null;
        Viewholder viewholder;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.gridview_item,
                    null);
            convertView = view;
            viewholder = new Viewholder();
            viewholder.imageview = (myImageview) view
                    .findViewById(R.id.imageview);
            viewholder.imageviewtip = (ImageView) view
                    .findViewById(R.id.ImageView_tip);
            viewholder.chakanphoto = (ImageView) view.findViewById(R.id.chakan_photo);
            convertView.setTag(viewholder);
        } else {
            view = convertView;
            viewholder = (Viewholder) view.getTag();
        }
        imageLoader.displayImage("file:///" + listfilepath.get(position), viewholder.imageview,
                options);
        if (needMoveFile.isinNeedmovefile(listfilepath.get(position))) {
            viewholder.imageviewtip.setVisibility(View.VISIBLE);
        } else {
            viewholder.imageviewtip.setVisibility(View.GONE);
        }
		viewholder.imageviewtip.setId(position);
        viewholder.chakanphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, test.class);
                intent.putStringArrayListExtra("listfilepath", listfilepath);
                intent.putExtra("position", mPosition);
                context.startActivityForResult(intent, 1);
            }
        });
        return view;
    }


    public List<String> getListfilename() {
        return listfilepath;
    }

    class Viewholder {
        myImageview imageview;
        ImageView imageviewtip;
        ImageView chakanphoto;
    }
}
