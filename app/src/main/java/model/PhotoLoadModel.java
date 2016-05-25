package model;

import android.os.AsyncTask;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import data.needMoveFile;
import utils.fileUtils;

/**
 * Created by Administrator on 2016/5/15 0015.
 */
public class PhotoLoadModel implements PhotoLoadModelInterface {
    LoadResultListener mLoadResultListener;
    LinkedHashMap<String, ArrayList<String>> mFilemap;

    @Override
    public void LoadPhotoPathList(LoadResultListener loadResultListener) {
        mLoadResultListener = loadResultListener;
        new GetPhotosPath().execute();
    }

    @Override
    public ArrayList<String> GetNeedMoveFile() {
        return needMoveFile.getNeedmoveFile();
    }

    /**
     * 获取listview删除item过后的数据地址
     * @return
     */
    public LinkedHashMap<String, ArrayList<String>>  GetDataChangedFile()
    {
        List<String> list = needMoveFile.getNeedmoveFile();
        List<String> filelist = new ArrayList<String>();
        for (String key : mFilemap.keySet()) {
            ArrayList<String> map = mFilemap.get(key);
            for (String file : list) {
                map.remove(file);
            }
            if (map.size() == 0)
                filelist.add(key);
        }
        for (String string : filelist) {
            Iterator<Map.Entry<String, ArrayList<String>>> it = mFilemap
                    .entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, ArrayList<String>> entry = it.next();
                String key = entry.getKey();
                if (key.equals(string)) {
                    it.remove();
                }
            }
        }
        needMoveFile.removeall();
        needMoveFile.clearPositemap();
        return mFilemap;
    }


    /**
     * 获取当前手机中按时间排序的相册地址集
     */
    class GetPhotosPath extends AsyncTask<Void, Integer, Void> {
        List<File> listfile = new ArrayList<File>();
        LinkedHashMap<String, ArrayList<String>> filemap;
        @Override
        protected Void doInBackground(Void... params) {
            try {
                listfile = fileUtils.getSD(application.myApplication.getContext());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            filemap = new LinkedHashMap<String, ArrayList<String>>();
            if (listfile.size() != 0) {
                String date = fileUtils.lastModifiedTodate(listfile.get(0));
                filemap.put(date, new ArrayList<String>());
                filemap.get(fileUtils.lastModifiedTodate(listfile.get(0))).add(
                        listfile.get(0).getAbsolutePath());
                for (int i = 1; i < listfile.size(); i++) {
                    if (!filemap.containsKey(fileUtils.lastModifiedTodate(listfile
                            .get(i)))) {
                        filemap.put(fileUtils.lastModifiedTodate(listfile.get(i)),
                                new ArrayList<String>());
                        filemap.get(fileUtils.lastModifiedTodate(listfile.get(i)))
                                .add(listfile.get(i).getAbsolutePath());
                    } else {
                        filemap.get(fileUtils.lastModifiedTodate(listfile.get(i)))
                                .add(listfile.get(i).getAbsolutePath());
                    }
                }
            }
            mFilemap=filemap;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mLoadResultListener.LoadSuccess(filemap);
            super.onPostExecute(aVoid);
        }
    }
}
