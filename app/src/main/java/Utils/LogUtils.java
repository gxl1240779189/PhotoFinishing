package Utils;

import android.util.Log;

/**
 * log管理工具类
 * Created by Administrator on 2016/4/26 0026.
 */
public class LogUtils {
    public static boolean isShow = true;//开发模式
//    public static boolean isShow = false;//上线模式
    //gxl工程师打出来的log
    public static void loggxl(String msg) {
        if (isShow) {
            Log.i("gxl", msg);
        }
    }
}
