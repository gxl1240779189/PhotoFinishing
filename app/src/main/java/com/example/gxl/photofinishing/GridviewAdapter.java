package com.example.gxl.photofinishing;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

public class GridviewAdapter extends BaseAdapter {

	Context context;

	List<String> listfilepath = new ArrayList<String>();
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;



	public GridviewAdapter(Context context, List<String> listfilepath) {
		this.context = context;
		this.listfilepath = listfilepath;
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory()
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
		View view = null;
		Viewholder viewholder;
		if (convertView == null) {
			view = LayoutInflater.from(context).inflate(R.layout.testgridview,
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
		Log.i("path", listfilepath.get(position));
		imageLoader.displayImage("file:///"+listfilepath.get(position), viewholder.imageview,
				options);
		return view;
	}

	

	class Viewholder {
		ImageView imageview;
	}
}

