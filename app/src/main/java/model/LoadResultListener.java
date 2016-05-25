package model;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Administrator on 2016/5/15 0015.
 */
public interface LoadResultListener {
    //加载数据成功
    void LoadSuccess( LinkedHashMap<String, ArrayList<String>> filemap);
    //数据加载失败
    void LoadFailure();
}
