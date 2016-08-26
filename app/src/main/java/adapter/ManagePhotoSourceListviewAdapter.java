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
import data.shezhiSharedprefrence;


/**
 * Created by Administrator on 2016/5/4 0004.
 */
public class ManagePhotoSourceListviewAdapter extends BaseAdapter {
    Context context;
    List<PhotoSourceBean> listfilepath = new ArrayList<PhotoSourceBean>();

    public ManagePhotoSourceListviewAdapter(Context context, List<PhotoSourceBean> list) {
        this.context = context;
        this.listfilepath = list;
    }


    public int getCount() {
        return listfilepath.size();
    }


    public PhotoSourceBean getItem(int position) {
        return listfilepath.get(position);
    }


    public long getItemId(int position) {
        return position;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder viewholder = null;
        final PhotoSourceBean file=listfilepath.get(position);
        File Folder=new File(file.getSourcePath());
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.guanlimenu_listview_item, null);
            viewholder = new ViewHolder();
            view = convertView;
            viewholder.wenjianjia_title= (TextView) view.findViewById(R.id.wenjianjia_title);
            viewholder.wenjianjia_path= (TextView) view.findViewById(R.id.wenjianjia_path);
            viewholder.check_choose= (ImageView) view.findViewById(R.id.check_choose);
            convertView.setTag(viewholder);
        } else {
            view = convertView;
            viewholder = (ViewHolder) view.getTag();
        }
        viewholder.wenjianjia_title.setText(Folder.getName());
        viewholder.wenjianjia_path.setText(Folder.getAbsolutePath());
        if(file.getChooseState())
        {
            viewholder.check_choose.setBackgroundResource(R.drawable.check_choose);
        }else
        {
            viewholder.check_choose.setBackgroundResource(R.drawable.check_unchoose);
        }
        viewholder.check_choose.setId(position);
        return view;
    }

    class ViewHolder {
        TextView wenjianjia_title;
        TextView wenjianjia_path;
        ImageView check_choose;
    }
}
