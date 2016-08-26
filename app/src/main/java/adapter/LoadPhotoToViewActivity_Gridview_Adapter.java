package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.example.gxl.photofinishing.R;
import com.example.gxl.photofinishing.PhotoViewerActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

import data.needMoveFile;
import customview.MyImageview;

public class LoadPhotoToViewActivity_Gridview_Adapter extends BaseAdapter {

    private Activity context;
    private ArrayList<String> listfilepath = new ArrayList<String>();
    private List<Boolean> listimageviewtip = new ArrayList<Boolean>();
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private int mListViewPosition;
    private static final String STATE_POSITION = "STATE_POSITION";
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_URLS = "image_urls";


    public LoadPhotoToViewActivity_Gridview_Adapter(Context context, ArrayList<String> list, int position) {
        this.mListViewPosition=position;
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
            viewholder.imageview = (MyImageview) view
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
                Intent intent = new Intent(context, PhotoViewerActivity.class);
                intent.putStringArrayListExtra(EXTRA_IMAGE_URLS, listfilepath);
                intent.putExtra(EXTRA_IMAGE_INDEX, mPosition);
                intent.putExtra(STATE_POSITION,mListViewPosition);
                context.startActivityForResult(intent, 1);
            }
        });
        return view;
    }


    public List<String> getListfilename() {
        return listfilepath;
    }

    class Viewholder {
        MyImageview imageview;
        ImageView imageviewtip;
        ImageView chakanphoto;
    }
}
