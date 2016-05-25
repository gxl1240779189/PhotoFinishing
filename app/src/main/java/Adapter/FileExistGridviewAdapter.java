package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.gxl.photofinishing.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

/**
 * Created by gxl on 2016/4/4.
 */
public class FileExistGridviewAdapter extends BaseAdapter{
    private ArrayList<String> filepath;
    private Context context;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
    private DisplayImageOptions options;

    @Override
    public int getCount() {
        return filepath.size();
    }

    public FileExistGridviewAdapter(ArrayList<String> filepath,Context context) {
        this.filepath=filepath;
        this.context=context;
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.yujiazai)
                .showImageForEmptyUri(R.drawable.yujiazai)
                .showImageOnFail(R.drawable.yujiazai).cacheInMemory()
                .cacheOnDisc().displayer(new RoundedBitmapDisplayer(20))
                .displayer(new FadeInBitmapDisplayer(300)).build();
    }

    @Override
    public Object getItem(int position) {
        return filepath.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        Viewholder viewholder;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.image_list_item,
                    null);
            convertView = view;
            viewholder = new Viewholder();
            viewholder.imageview = (ImageView) view
                    .findViewById(R.id.imageview);
            convertView.setTag(viewholder);
        } else {
            view = convertView;
            viewholder = (Viewholder) view.getTag();
        }
        imageLoader.displayImage("file:///" + filepath.get(position), viewholder.imageview,
                options);
        return view;
    }



    class Viewholder {
        ImageView imageview;
    }
}
