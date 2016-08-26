package view;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Administrator on 2016/5/24 0024.
 */
public interface ShowArrangeFolderInterface {
    void InitView();      //初始化View和设置监听，资源
    void LoadListviewSuccess(ArrayList<String> filemap,int mflag);//将分析好的数据装载到listview中去，通过设配器
    void LoadlistviewFail(); //如果分析好的数据为空，做出相应的处理，显示当前无数据的页面
    void ChangeToNormal();   //从删除的界面变成正常的界面
    void ChangeToDelete();   //从正常的界面变成删除的界面
}
