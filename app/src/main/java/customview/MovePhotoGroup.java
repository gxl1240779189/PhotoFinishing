package customview;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.gxl.photofinishing.R;

/**
 * 用来实现一个从图片从四周汇聚过来的动画效果而创建的一个ViewGroup
 */
public class MovePhotoGroup extends LinearLayout {

	Context context;
	int mCentrex;
	int mCentrey;
	int i = 0;

	public MovePhotoGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public MovePhotoGroup(Context context) {
		this(context, null);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		final View view = getChildAt(0);
		final View view1 = getChildAt(1);
		view1.layout(mCentrex, mCentrey - 100,
				mCentrex + view1.getMeasuredWidth(),
				mCentrey - 100 + view1.getMeasuredHeight());

		final View view2 = getChildAt(2);
		view2.layout(mCentrex, mCentrey + 100,
				mCentrex + view2.getMeasuredWidth(),
				mCentrey + 100 + view2.getMeasuredHeight());

		final View view3 = getChildAt(3);
		view3.layout(mCentrex - 250, mCentrey,
				mCentrex - 250 + view1.getMeasuredWidth(),
				mCentrey + view1.getMeasuredHeight());

		final View view4 = getChildAt(4);
		view4.layout(mCentrex + 250, mCentrey,
				mCentrex + 250 + view1.getMeasuredWidth(),
				mCentrey + view1.getMeasuredHeight());
		i++;
		if (i == 1) {
			TranslateAnimation animation = new TranslateAnimation(0, 0, 0, 100);
			animation.setDuration(200);
			view1.startAnimation(animation);

			TranslateAnimation animation1 = new TranslateAnimation(0, 0, 0,
					-100);
			animation1.setDuration(200);
			view2.startAnimation(animation1);

			TranslateAnimation animation2 = new TranslateAnimation(0, 250, 0, 0);
			animation2.setDuration(200);
			view3.startAnimation(animation2);

			TranslateAnimation animation3 = new TranslateAnimation(0, -250, 0,
					0);
			animation3.setDuration(200);
			view4.startAnimation(animation3);

			animation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					view1.setVisibility(View.GONE);
					view2.setVisibility(View.GONE);
					view3.setVisibility(View.GONE);
					view4.setVisibility(View.GONE);
					view.layout(mCentrex, mCentrey,
							mCentrex + view.getMeasuredWidth(), mCentrey
									+ view.getMeasuredHeight());
				}
			});
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		View view = getChildAt(0);

		LayoutParams lp_centre = (LayoutParams) view
				.getLayoutParams();
		mCentrex = lp_centre.topMargin;
		mCentrey = lp_centre.leftMargin;

		Log.i("mCentrex", mCentrex + "");

		LayoutParams lp = new LayoutParams(300, 300);

		ImageView view1 = new ImageView(context);
		view1.setBackgroundResource(R.drawable.yujiazai);
		addView(view1, lp);
		ImageView view2 = new ImageView(context);
		view2.setBackgroundResource(R.drawable.yujiazai);
		addView(view2, lp);
		ImageView view3 = new ImageView(context);
		view3.setBackgroundResource(R.drawable.yujiazai);
		addView(view3, lp);
		ImageView view4 = new ImageView(context);
		view4.setBackgroundResource(R.drawable.yujiazai);
		addView(view4, lp);

		measureChild(view1, widthMeasureSpec, heightMeasureSpec);
		measureChild(view2, widthMeasureSpec, heightMeasureSpec);
		measureChild(view3, widthMeasureSpec, heightMeasureSpec);
		measureChild(view4, widthMeasureSpec, heightMeasureSpec);
		measureChild(view, widthMeasureSpec, heightMeasureSpec);
	}

}
