package com.example.gxl.photofinishing;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Window;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Adapter.FileExistGridviewAdapter;
import Utils.fileUtils;

/**
 * Created by gxl on 2016/3/30.
 */
public class FileExistActivity extends Activity {

    private String filepath;
    private GridView gridView;
    ArrayList<String> filepathlist;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        filepath=getIntent().getStringExtra("filepath");
        setContentView(R.layout.fileexist_layout);
        Log.i("tag", filepath);
        gridView= (GridView) findViewById(R.id.gridview);
        filepathlist= fileUtils.getExistImageList(filepath);
        for (String path:
             filepathlist) {
            Log.i("path",path);
        }
        FileExistGridviewAdapter adpter=new FileExistGridviewAdapter(filepathlist,FileExistActivity.this);
       gridView.setAdapter(adpter);
    }
}
