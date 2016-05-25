package data;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import utils.LogUtils;

/**
 * Created by Administrator on 2016/5/5 0005.
 */
public class shezhiSharedprefrence {
    Context context;
    SharedPreferences shezhiHistory;


    public shezhiSharedprefrence(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
        shezhiHistory = context.getSharedPreferences("shezhi_photoyuan", 0);
    }


    /**
     * 判断是否是第一次设置SharedPreferences
     * @return
     */
    public Boolean isFirstin()
    {
        if(shezhiHistory.getBoolean("firstin",true))
        {
            SharedPreferences.Editor myeditor = shezhiHistory.edit();
            myeditor.putBoolean("firstin",false);
            myeditor.commit();
            return true;
        }else
        {
            return false;
        }
    }


    /**
     * 添加一个新的地址到设置里面去
     *
     * @param p
     */
    public void save(String p) {
        String old_text = shezhiHistory.getString("shezhi_photoyuan", "");
        LogUtils.loggxl("photoyuan"+old_text);
        StringBuilder builder = new StringBuilder(old_text);
        builder.append(p + ",");
        if (!old_text.contains(p + ",")) {
            SharedPreferences.Editor myeditor = shezhiHistory.edit();
            myeditor.putString("shezhi_photoyuan", builder.toString());
            myeditor.commit();
        } else {
        }
    }

    /**
     * 返回之前设置的地址信息
     *
     * @return
     */
    public List<String> returnhistroydata() {
        List<String> histroy_list = new ArrayList<String>();
        String history = shezhiHistory.getString("shezhi_photoyuan", "");
        if (history !=null) {
            String[] history_arr = history.split(",");
            if (history_arr.length != 0) {
                for (int i = 0; i < history_arr.length; i++) {
                    histroy_list.add(history_arr[i]);
                }
            }
        }
        return histroy_list;
    }


    public List<File> returnhistroyFile() {
        List<File> File_List = new ArrayList<File>();
        List<String> histroy_list = new ArrayList<String>();
        String history = shezhiHistory.getString("shezhi_photoyuan", "");
        if (!(history == "")) {
            String[] history_arr = history.split(",");
            if (history_arr.length != 0) {
                for (int i = 0; i < history_arr.length; i++) {
                    histroy_list.add(history_arr[i]);
                    File_List.add(new File(history_arr[i]));
                }
            }
        }
        return File_List;
    }

    /**
     * 在设置里面删除一个地址源
     *
     * @param deleteneirong
     */
    public void delete(String deleteneirong) {
        List<String> histroy_list = new ArrayList<String>();
        StringBuilder builder = new StringBuilder();
        histroy_list = returnhistroydata();
        LogUtils.loggxl(histroy_list.toString());
        if (histroy_list.contains(deleteneirong)) {
            histroy_list.remove(deleteneirong);
            for (int i = 0; i < histroy_list.size(); i++) {
                builder.append(histroy_list.get(i) + ",");
            }
            LogUtils.loggxl(builder.toString());
            if(builder!=null) {
                SharedPreferences.Editor myeditor = shezhiHistory.edit();
                myeditor.putString("shezhi_photoyuan", builder.toString());
                myeditor.commit();
            }else
            {
                SharedPreferences.Editor myeditor = shezhiHistory.edit();
                myeditor.putString("shezhi_photoyuan","");
                myeditor.commit();
            }
        }
    }

    /**
     * 判断当前的path是否在路径中
     *
     * @param path
     * @return
     */
    public Boolean isExist(String path) {
        List<String> histroy_list = returnhistroydata();
        if (histroy_list.contains(path)) {
            return true;
        } else {
            return false;
        }
    }

}

