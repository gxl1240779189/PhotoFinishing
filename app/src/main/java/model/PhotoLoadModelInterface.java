package model;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Administrator on 2016/5/15 0015.
 */
public interface PhotoLoadModelInterface {
   void LoadPhotoPathList(LoadResultListener loadResultListener);
   ArrayList<String> GetNeedMoveFile();
   LinkedHashMap<String, ArrayList<String>> GetDataChangedFile();
}
