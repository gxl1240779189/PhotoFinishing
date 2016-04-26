package Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by gxl on 2016/3/31.
 */
public class myImageLoader {

    /**
     * 需要实现的功能：
     * 1.从当前内存中加载bitmap(Lrucache)
     * 2.从内存卡中加载图片
     * 3.线程池的实现
     */

    private LruCache<String, Bitmap> mMemoryCache;
    private Context context;

    myImageLoader(Context context) {
        this.context = context;
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cachsize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cachsize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    public  void addBitmapToMemory(String key, Bitmap bitmap) {
        if (getBitmapFromMemory(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemory(String key) {
        return mMemoryCache.get(key);
    }

    public void RemoveBitmapMemory(String key) {
        mMemoryCache.remove(key);
    }

}
