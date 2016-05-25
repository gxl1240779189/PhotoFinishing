package Presenter;

import java.util.ArrayList;

import model.ShowPhotoModel;
import view.ShowPhotoInterface;

/**
 * Created by Administrator on 2016/5/25 0025.
 */
public class ShowPhotoPresenter {
    public ShowPhotoPresenter(ShowPhotoInterface mView) {
        this.mView = mView;
    }

    private ShowPhotoInterface mView;
    private ShowPhotoModel mModel = new ShowPhotoModel();

    /**
     * 将已经整理好的文件夹显示到listview中
     */
    public void InitListview(int mflag) {
        ArrayList<String> filemap = mModel.GetPhotoFileList();
        if (filemap.size() != 0) {
            mView.LoadListviewSuccess(filemap,mflag);
        } else {
            mView.LoadlistviewFail();
        }
    }


}
