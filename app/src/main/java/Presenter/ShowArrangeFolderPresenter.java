package Presenter;

import java.util.ArrayList;

import model.ShowArrangeFolderModel;
import view.ShowArrangeFolderInterface;

/**
 * Created by Administrator on 2016/5/25 0025.
 */
public class ShowArrangeFolderPresenter {
    public ShowArrangeFolderPresenter(ShowArrangeFolderInterface mView) {
        this.mView = mView;
    }

    private ShowArrangeFolderInterface mView;
    private ShowArrangeFolderModel mModel = new ShowArrangeFolderModel();

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
