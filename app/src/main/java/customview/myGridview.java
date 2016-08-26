package customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class MyGridview extends GridView{

	public MyGridview(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public MyGridview(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}


	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec=MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);	
	}

}
