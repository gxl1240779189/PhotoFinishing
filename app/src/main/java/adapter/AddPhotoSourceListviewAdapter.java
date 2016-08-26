package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gxl.photofinishing.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import bean.PhotoSourceBean;
import data.needMoveFile;

/**
 * Created by Administrator on 2016/6/10 0010.
 */
public class AddPhotoSourceListviewAdapter extends BaseAdapter {
    Context context;
    List<String> listfilepath = new ArrayList<String>();

    public AddPhotoSourceListviewAdapter(Context context, List<String> list) {
        this.context = context;
        this.listfilepath = list;
    }


    public int getCount() {
        return listfilepath.size();
    }


    public String getItem(int position) {
        return listfilepath.get(position);
    }


    public long getItemId(int position) {
        return position;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder viewholder = null;
        final String file = listfilepath.get(position);
        File Folder = new File(file);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.guanlimenu_listview_item, null);
            viewholder = new ViewHolder();
            view = convertView;
            viewholder.wenjianjia_title = (TextView) view.findViewById(R.id.wenjianjia_title);
            viewholder.wenjianjia_path = (TextView) view.findViewById(R.id.wenjianjia_path);
            viewholder.check_choose = (ImageView) view.findViewById(R.id.check_choose);
            convertView.setTag(viewholder);
        } else {
            view = convertView;
            viewholder = (ViewHolder) view.getTag();
        }
        viewholder.wenjianjia_title.setText(Folder.getName());
        viewholder.wenjianjia_path.setText(Folder.getAbsolutePath());
        viewholder.check_choose.setId(position);
        if (needMoveFile.IsWillAddPhotoSource(listfilepath.get(position))) {
            viewholder.check_choose.setBackgroundResource(R.drawable.check_choose);
        } else {
            viewholder.check_choose.setBackgroundResource(R.drawable.check_unchoose);
        }
        return view;
    }

    class ViewHolder {
        TextView wenjianjia_title;
        TextView wenjianjia_path;
        ImageView check_choose;
    }
}