package utils;

import android.graphics.Bitmap;
import android.util.LruCache;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by gxl on 2016/4/1.
 */
 public  class CacheUtils {

    /**
     * 需要实现的功能：
     * 1.从当前内存中加载bitmap(Lrucache)
     * 2.从内存卡中加载图片
     * 3.线程池的实现
     */
    static int  maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    static int cachsize = maxMemory / 8;
    private static LruCache<String, Bitmap> mMemoryCache = new LruCache<String, Bitmap>(cachsize) {
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes() * value.getHeight() / 1024;
        }
    };

    public static void addBitmapToMemory(String key, Bitmap bitmap) {
        if (getBitmapFromMemory(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public static  Bitmap getBitmapFromMemory(String key) {
        return mMemoryCache.get(key);
    }

    public static void RemoveBitmapMemory(String key) {
        mMemoryCache.remove(key);
    }

    public static String hashKeyFormUrl(String url) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(url.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return String.valueOf(url.hashCode());
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
