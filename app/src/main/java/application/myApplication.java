package application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;

import com.activeandroid.ActiveAndroid;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import utils.LogUtils;

/**
 * Created by gxl on 2016/3/30.
 */
public class MyApplication extends Application {

    public static String move_file_path = "/0FunPic";

    public static Context getContext() {
        return context;
    }

    static Context context;

    private static String[] SourceList = {
            "/storage/emulated/0/Pictures",
            "/storage/emulated/0/Snapseed"
    };

    /**
     * 获取当前手机手机中的默认相册源
     *
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static ArrayList<File> getPhoto_yuan() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        StorageManager mStorageManager;
        Method mMethodGetPaths;
        ArrayList<File> photo_yuan = new ArrayList<File>();
        mStorageManager = (StorageManager) context
                .getSystemService(Activity.STORAGE_SERVICE);
        mMethodGetPaths = mStorageManager.getClass()
                .getMethod("getVolumePaths");
        String[] paths = null;
        paths = (String[]) mMethodGetPaths.invoke(mStorageManager);
        for (int i = 0; i < paths.length; i++) {
            if (new File(paths[i] + "/DCIM/Camera").exists()) {
                photo_yuan.add(new File(paths[i] + "/DCIM/Camera"));
            } else {
                continue;
            }
        }
        if (getPath2() != null && (new File(getPath2() + "/DCIM/Camera").exists())) {
            photo_yuan.add(new File(getPath2() + "/DCIM/Camera"));
        }
        for (String item :
                SourceList) {
            File file = new File(item);
            if (file.exists()) {
                photo_yuan.add(file);
            }
        }
        return photo_yuan;
    }

    //获取外置存储卡的根路径，如果没有外置存储卡，则返回null
    public static String getPath2() {
        String sdcard_path = null;
        String sd_default = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        if (sd_default.endsWith("/")) {
            sd_default = sd_default.substring(0, sd_default.length() - 1);
        }
        // 得到路径
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            String line;
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                if (line.contains("secure"))
                    continue;
                if (line.contains("asec"))
                    continue;
                if (line.contains("fat") && line.contains("/mnt/")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        if (sd_default.trim().equals(columns[1].trim())) {
                            continue;
                        }
                        sdcard_path = columns[1];
                    }
                } else if (line.contains("fuse") && line.contains("/mnt/")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        if (sd_default.trim().equals(columns[1].trim())) {
                            continue;
                        }
                        sdcard_path = columns[1];
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sdcard_path;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "imageloader/Cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getApplicationContext())
                .memoryCacheExtraOptions(800, 800) // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(5)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new WeakMemoryCache()) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCacheFileCount(100) //缓存的文件数量
                .discCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(getApplicationContext(), 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();//开始构建
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);

        File app_dic = new File(Environment.getExternalStorageDirectory()
                .getPath() + MyApplication.move_file_path);
        if (!app_dic.exists()) {
            app_dic.mkdir();
        }
        ActiveAndroid.initialize(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }
}
