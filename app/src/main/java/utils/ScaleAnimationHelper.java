package utils;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

/**
 * Created by Administrator on 2016/6/8 0008.
 */
public class ScaleAnimationHelper {
   private static ScaleAnimation myAnimation_Scale;

    //缩小的类
    public static void ScaleInAnimation(View view,float from,float to) {
        myAnimation_Scale = new ScaleAnimation(from, to, from, to,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f);
        myAnimation_Scale.setInterpolator(new AccelerateInterpolator());
        AnimationSet aa = new AnimationSet(true);
        aa.addAnimation(myAnimation_Scale);
        aa.setDuration(500);
        view.startAnimation(aa);
    }

}
