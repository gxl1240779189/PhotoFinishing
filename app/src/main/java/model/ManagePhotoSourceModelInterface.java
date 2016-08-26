package model;

import java.util.ArrayList;

import bean.PhotoSourceBean;

/**
 * Created by Administrator on 2016/6/9 0009.
 */
public interface ManagePhotoSourceModelInterface {
    //系统已经添加的相册源
    ArrayList<PhotoSourceBean> GetPhotoSourceList();
}
