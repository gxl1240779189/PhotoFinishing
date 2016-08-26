package Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.example.gxl.photofinishing.AboutUsActivity;
import com.example.gxl.photofinishing.ManagePhotoSourceActivity;
import com.example.gxl.photofinishing.ShowArrangeFolderActivity;
import com.example.gxl.photofinishing.SyncBackupActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import application.MyApplication;
import data.needMoveFile;
import model.LoadResultListener;
import model.LoadPhotoToViewModel;
import model.LoadPhotoToViewModelInterface;
import utils.fileUtils;
import view.LoadPhotoToViewInterface;

/**
 * Created by Administrator on 2016/5/20 0020.
 */
public class LoadPhotoToViewPresenter {
    Context mContext;
    LoadPhotoToViewInterface mPhotoLoadView;
    LoadPhotoToViewModelInterface mPhotoLoadModel = new LoadPhotoToViewModel();

    public LoadPhotoToViewPresenter(LoadPhotoToViewInterface mPhotoLoadView, Context context) {
        this.mPhotoLoadView = mPhotoLoadView;
        mContext = context;
    }

    /**
     * 用来初始化listview
     * 步骤：
     * 通过mPhotoLoadModel获取照片的地址信息集合
     * 通过mPhotoLoadView将获取的地址信息集合装载到listview中去
     */
    public void InitListview() {
        mPhotoLoadView.LoadingData();
        mPhotoLoadModel.LoadPhotoPathList(new LoadResultListener() {
            @Override
            public void LoadSuccess(LinkedHashMap<String, ArrayList<String>> filemap) {
                mPhotoLoadView.LoadDataFinish();
                //在这里将数据集合装载到adapter中去
                mPhotoLoadView.LoadListviewSuccess(filemap);
            }

            @Override
            public void LoadFailure() {
                //数据加载失败，显示无照片页面
                mPhotoLoadView.LoadlistviewFail();
            }
        });
    }

    /**
     * 跳转到查看照片文件夹页面中
     */
    public void StartShowPhotoActivity() {
        Intent intent = new Intent(mContext, ShowArrangeFolderActivity.class);
        ((Activity) mContext).startActivityForResult(intent, 1);
    }

    /**
     * 跳转到管理相册源页面中
     */
    public void StartguanlimenuActivity() {
        Intent guanliintent = new Intent(mContext, ManagePhotoSourceActivity.class);
        ((Activity) mContext).startActivityForResult(guanliintent, 1);
    }

    /**
     * 跳转到同步备份页面中
     */
    public void StartsyncbackupActivity() {
        Intent syncbackup = new Intent(mContext, SyncBackupActivity.class);
        ((Activity) mContext).startActivity(syncbackup);
    }

    /**
     * 跳转到关于我们页面中
     */
    public void StartAboutUsActivity() {
        Intent intent = new Intent(mContext, AboutUsActivity.class);
        ((Activity) mContext).startActivity(intent);
    }


    /**
     * 删除选择图片的任务
     */
    public void DeleteFileTask() {
        new DeleteFileTask().execute();
    }


    /**
     * 弹出删除的对话框
     */
    public void ShowDeleteDialog() {
        mPhotoLoadView.CreateDeleteDialog();
    }

    /**
     * 弹出新建文件夹的对话框
     */
    public void ShowNewDialog() {
        new GetCityname_Tack().execute();
    }

    /**
     * 移动照片到新的文件夹
     */
    public void MovePhotoToNew(String FileName) {
        new MoveNeedFileTask().execute(FileName);
    }

    /**
     * 将现在选择的照片移动到已经存在的文件夹中
     *
     * @param FileName
     */
    public void MovePhotoToExistFile(String FileName) {
        new MovePhotoToExistFileTask().execute(FileName);
    }

    /**
     * 移动照片到之前存在的文件夹中
     */
    public void ShowMovePhotoToExistFileDialog() {
        mPhotoLoadView.CreateMoveToFileDialog();
    }

    /**
     * 创建一个可以移动的照片集
     */

    public void CreateMoveGroup(int x, int y, String path) {
        mPhotoLoadView.CreateMoveGroup(x, y, path);
    }


    /**
     * 获取地址时间信息
     */
    /**
     * 根据经纬度获取城市名
     */
    class GetCityname_Tack extends AsyncTask<Void, Integer, String> {
        String shijian;
        String cityname;
        String[] information = new String[3];

        @Override
        protected String doInBackground(Void... params) {

            information = fileUtils.getNeedmoveFileLocation(needMoveFile.getNeedmoveFile());
            if ((!information[0].equals("nothing")) && (!information[1].equals("nothing"))) {
                cityname = GetAddr(information[0], information[1]);
            }
            return cityname;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (cityname != null) {
                mPhotoLoadView.CreateNewFileDialog(information[2] + cityname);
            } else {
                mPhotoLoadView.CreateNewFileDialog(information[2]);
            }
        }
    }

    /**
     * 根据经纬度获取城市名称
     *
     * @param latitude
     * @param longitude
     * @return
     */
    public String GetAddr(String latitude, String longitude) {
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        StringBuilder stringBuilder = null;
        try {
            List<Address> addresses = geocoder.getFromLocation(Float.valueOf(latitude), Float.valueOf(longitude), 1);
            stringBuilder = new StringBuilder();
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                stringBuilder.append(address.getLocality());
                System.out.println(stringBuilder.toString());
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Toast.makeText(mContext, "报错", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


    /**
     * 将选中的文件直接删除
     */
    class DeleteFileTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            fileUtils.deleteFilelist(mPhotoLoadModel.GetNeedMoveFile());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            LinkedHashMap<String, ArrayList<String>> filemap = mPhotoLoadModel.GetDataChangedFile();
            mPhotoLoadView.LoadListviewSuccess(filemap);
        }
    }

    /**
     * 将选好的图片移动到指定的文件夹 TASK
     */
    class MoveNeedFileTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            mPhotoLoadView.MovingData();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            MoveNeedfile(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mPhotoLoadView.LoadDataFinish();
            LinkedHashMap<String, ArrayList<String>> filemap = mPhotoLoadModel.GetDataChangedFile();
            mPhotoLoadView.LoadListviewSuccess(filemap);
        }
    }

    /**
     * 创建新的文件夹时候调用
     *
     * @param path
     */
    public void MoveNeedfile(final String path) {
        List<String> list = needMoveFile.getNeedmoveFile();
        final List<File> needmovelistfile = new ArrayList<File>();
        for (String string : list) {
            needmovelistfile.add(new File(string));
        }
        File Folder = new File(Environment
                .getExternalStorageDirectory().getPath()
                + MyApplication.move_file_path + "/" + path);
        if (!Folder.exists()) {
            Folder.mkdir();
        }
        for (File file : needmovelistfile) {
            fileUtils.copyFile(file.getAbsolutePath(), Environment
                    .getExternalStorageDirectory().getPath()
                    + MyApplication.move_file_path + "/" + path + "/" + file.getName());
        }
    }


    /**
     * 将选好的图片移动到已经存在的文件夹中
     */
    class MovePhotoToExistFileTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            mPhotoLoadView.MovingData();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            MoveNeedToFile(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mPhotoLoadView.LoadDataFinish();
            LinkedHashMap<String, ArrayList<String>> filemap = mPhotoLoadModel.GetDataChangedFile();
            mPhotoLoadView.LoadListviewSuccess(filemap);
        }
    }

    /**
     * 在移动到已经存在的文件夹中调用
     *
     * @param path
     */
    public void MoveNeedToFile(final String path) {
        List<String> list = needMoveFile.getNeedmoveFile();
        final List<File> needmovelistfile = new ArrayList<File>();
        for (String string : list) {
            needmovelistfile.add(new File(string));
        }
        File app_dic = new File(Environment.getExternalStorageDirectory()
                .getPath() + "/" + MyApplication.move_file_path);
        if (!app_dic.exists()) {
            app_dic.mkdir();
        }
        File movefile = new File(path);
        if (!movefile.exists()) {
            movefile.mkdir();
        }
        for (File file : needmovelistfile) {
            fileUtils.copyFile(file.getAbsolutePath(), path + "/" + file.getName());
        }
    }


}
