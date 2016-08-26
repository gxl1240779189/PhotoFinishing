package model;

import android.os.Environment;

import java.util.ArrayList;

import application.MyApplication;
import utils.fileUtils;

/**
 * Created by Administrator on 2016/5/24 0024.
 */
public class ShowArrangeFolderModel implements ShowArrangeFolderModelInterface {

    //获取当前已经整理好的照片目录
    @Override
    public ArrayList<String> GetPhotoFileList() {
        return fileUtils.getExistFileList(Environment.getExternalStorageDirectory().getPath()+ MyApplication.move_file_path);
    }

}
