package myView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class myGridview extends GridView{

	public myGridview(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public myGridview(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}


	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec=MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);	
	}

}
