package fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.gxl.photofinishing.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 单张图片显示Fragment
 */
public class ImageDetailFragment extends Fragment {
	private static DisplayImageOptions options;
	private String mImageUrl;
	private ImageView mImageView;
	private ProgressBar progressBar;
	private PhotoViewAttacher mAttacher;
	private static ImageLoader imageLoader = ImageLoader.getInstance();

	public static ImageDetailFragment newInstance(String imageUrl) {
		final ImageDetailFragment f = new ImageDetailFragment();
		final Bundle args = new Bundle();
		        options = new DisplayImageOptions.Builder()
                .cacheOnDisc(true)
                .cacheInMemory(false)
                .showStubImage(R.drawable.yujiazai)
                .showImageForEmptyUri(R.drawable.yujiazai)
                .showImageOnFail(R.drawable.yujiazai)
						.displayer(new RoundedBitmapDisplayer(20))
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .displayer(new FadeInBitmapDisplayer(300)).build();
		args.putString("url", imageUrl);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
		mImageView = (ImageView) v.findViewById(R.id.image);
		progressBar = (ProgressBar) v.findViewById(R.id.loading);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ImageLoader.getInstance().clearMemoryCache();
		ImageLoader.getInstance().displayImage("file:///" + mImageUrl, mImageView, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				progressBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				String message = null;
				switch (failReason.getType()) {
					case IO_ERROR:
						message = "下载错误";
						break;
					case DECODING_ERROR:
						message = "图片无法显示";
						break;
					case NETWORK_DENIED:
						message = "网络有问题，无法下载";
						break;
					case OUT_OF_MEMORY:
						message = "图片太大无法显示";
						break;
					case UNKNOWN:
						message = "未知的错误";
						break;
				}
				Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
				progressBar.setVisibility(View.GONE);
			}
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

			}
		});
		 progressBar.setVisibility(View.GONE);
	}
}
