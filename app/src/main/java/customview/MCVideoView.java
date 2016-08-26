package customview;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.VideoView;

public class MCVideoView extends VideoView{

	public MCVideoView(Context context) {
		this(context,null);
	}

	public MCVideoView(Context context, AttributeSet attrs) {
		super(context, attrs,0);
	}

	public MCVideoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
	}

	public void playVideo(Context context,Uri uri){
		if(uri==null){
			throw new IllegalArgumentException("Uri can not be null");
		}
		//设置播放路径
		setVideoURI(uri);
		//开始播放
		start();
		setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				//设置循环播放
				mp.setLooping(true);	
			}
		});
		setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				return true;
			}
		});
	}
}
