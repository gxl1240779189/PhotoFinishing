package utils;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import bean.PhotoSourceBean;

/**
 * 数据库的操作方法
 */
public class DbUtils {

    /**
     * 返回当前选择好的相片源
     *
     * @return
     */
    public static ArrayList<File> GetChoosePhotoSourceList() {
        ArrayList<File> FileList = new ArrayList<File>();
        List<PhotoSourceBean> list = new Select()
                .from(PhotoSourceBean.class)
                .execute();
        for (PhotoSourceBean item :
                list) {
            if (item.getChooseState()) {
                FileList.add(new File(item.getSourcePath()));
            }
        }
        return FileList;
    }

    /**
     * 返回总的相片源
     *
     * @return
     */
    public static List<PhotoSourceBean> GetPhotoSourceList() {
        List<PhotoSourceBean> list = new Select()
                .from(PhotoSourceBean.class)
                .execute();
//        for (PhotoSourceBean item :
//                list) {
//            if (!IsFileHasPhoto(item.getSourcePath())) {
//                new Delete().from(PhotoSourceBean.class).where("SourcePath = ?", item.getSourcePath()).execute();
//                list.remove(item);
//            }
//        }
//        try {
//            Iterator<PhotoSourceBean> iterator = list.iterator();
//            while (iterator.hasNext()) {
//                if (!IsFileHasPhoto(iterator.next().getSourcePath())) {
//                new Delete().from(PhotoSourceBean.class).where("SourcePath = ?", iterator.next().getSourcePath()).execute();
//                list.remove(iterator.next());
//            }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return list;
    }


    /**
     * 判断当前添加的目录中是否还有照片
     *
     * @param strPath
     * @return
     */
    public static Boolean IsFileHasPhoto(String strPath) {
        File dir = new File(strPath);
        File[] files = dir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                LogUtils.loggxl(fileName);
                if (files[i].isDirectory()) {
                    continue;
                } else if (getImageFile(files[i].getPath())) {
                    return true;
                } else {
                    continue;
                }
            }
        }
        return false;
    }


    public static boolean getImageFile(String fName) {
        boolean re;

        String end = fName
                .substring(fName.lastIndexOf(".") + 1, fName.length())
                .toLowerCase();


        if (end.equals("jpg") || end.equals("gif") || end.equals("png")
                || end.equals("jpeg") || end.equals("bmp")) {
            re = true;
        } else {
            re = false;
        }
        return re;
    }

    /**
     * 点击更改相片源状态
     *
     * @param flag
     * @param path
     */
    public static void ChangePhotoSourceState(Boolean flag, String path) {
        new Delete().from(PhotoSourceBean.class).where("SourcePath = ?", path).execute();
        PhotoSourceBean item = new PhotoSourceBean();
        item.setSourcePath(path);
        item.setChooseState(flag);
        item.save();
    }

    /**
     * 判断当前地址是否在数据库中
     *
     * @param path
     * @return
     */
    public static Boolean IsExist(String path) {
        PhotoSourceBean item = new Select().from(PhotoSourceBean.class).where("SourcePath = ?", path).executeSingle();
        if (item == null)
            return false;
        else
            return true;
    }
}
