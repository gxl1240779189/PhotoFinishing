package Adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by gxl on 2016/4/3.
 */
public class GuideViewpaper extends PagerAdapter {
    private List<View> views;

    public int getCount() {
        return views.size();
    }

    public GuideViewpaper(List<View> views) {
        this.views = views;
    }

    public boolean isViewFromObject(View view, Object object) {
        return (view==object);
    }

    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position));
        return views.get(position);
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
       container.removeView(views.get(position));
    }
}
