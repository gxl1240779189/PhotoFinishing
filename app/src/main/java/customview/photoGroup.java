package customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class photoGroup extends RelativeLayout {

	public  List<String> needmoveFile = new ArrayList<String>();
	public  List<Bitmap> needmoveBitmap = new ArrayList<Bitmap>();
	public  List<ImageView> needmoveView = new ArrayList<ImageView>();
	public Context context;

	public photoGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		View child = getChildAt(0);
		Log.i("width", child.getMeasuredWidth()+"");
		l=150;
		t=150;
		child.layout(l, t, l+500, t+500);
	}

	public photoGroup(Context context, List<Bitmap> needmoveBitmap) {
		super(context);
		this.context = context;
		this.needmoveBitmap = needmoveBitmap;
	
		for (int i = 0; i < needmoveBitmap.size(); i++) {
			ImageView imageview = new ImageView(context);
			imageview.setImageBitmap(needmoveBitmap.get(i));
			needmoveView.add(imageview);
		}
		ViewPager view = new ViewPager(context);
		Log.i("needmoveView.size()", needmoveView.size()+"");
		view.setAdapter(new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				// TODO Auto-generated method stub
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return needmoveView.size();
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView(needmoveView.get(position));
			}

			public int getItemPosition(Object object) {
				return super.getItemPosition(object);
			}

			public Object instantiateItem(ViewGroup container, int position) {
				container.addView(needmoveView.get(position));
				return needmoveView.get(position);
			}

		});
		measureView(view);
		addView(view);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		View child = getChildAt(0);
//		LayoutParams lp=(LayoutParams) child.getLayoutParams();
//		lp.width=50;
//		lp.height=50;
//		child.setLayoutParams(lp);	
//		setMeasuredDimension(300, 300);
//		setBackgroundColor(getResources().getColor(R.color.blue));
//		ImageView image=new ImageView(context);
//		image.setBackgroundResource(R.drawable.ic_launcher);
//		RelativeLayout.LayoutParams imageparams=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
//		imageparams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
//		measureView(image);
//		addView(image);
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

	}

	private void measureView(View child) {
		int childWidthSpec;
		int childHeightSpec;
		childHeightSpec = MeasureSpec.makeMeasureSpec(100,
				MeasureSpec.EXACTLY);
		childWidthSpec = MeasureSpec.makeMeasureSpec(100,
						MeasureSpec.EXACTLY);
		child.measure(childWidthSpec, childHeightSpec);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.i("aaa", "aaa");
			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}
	
}
