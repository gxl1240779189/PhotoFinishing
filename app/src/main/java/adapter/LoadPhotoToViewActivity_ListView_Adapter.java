package adapter;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gxl.photofinishing.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import data.needMoveFile;
import customview.MyGridview;

public class LoadPhotoToViewActivity_ListView_Adapter extends BaseAdapter {

    Map<String, ArrayList<String>> filepathlist;
    Context context;
    List<String> datelist = new ArrayList<String>();
    int flag = 0;
    show_choose_detail_Listener mListener;
    final int TYPE_NO_First = 0;
    final int TYPE_First = 1;


    public interface show_choose_detail_Listener {
        void show_choose_detail_linearlayout(int size);

        void hide_choose_detail_linearlayout();
    }


    public void setGroup(movephotoGroup group) {
        this.group = group;
    }

    movephotoGroup group;

    public interface movephotoGroup {
        void CreateMoveGroup(int x, int y, String string);
    }

    public LoadPhotoToViewActivity_ListView_Adapter(Context context,
                                                    Map<String, ArrayList<String>> filepathlist, show_choose_detail_Listener listener) {
        this.filepathlist = filepathlist;
        this.context = context;
        this.mListener = listener;
        if (filepathlist.size() > 0) {
            for (String key : filepathlist.keySet()) {
                datelist.add(key);
            }
        }
    }

    @Override
    public int getCount() {
        return datelist.size();
    }

    @Override
    public Object getItem(int position) {

        return datelist.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }


    public int getViewTypeCount() {
        return 2;
    }


    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        if (position == 0)
            return TYPE_First;
        else
            return TYPE_NO_First;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int listview_position = position;
        View view = null;
        int type = getItemViewType(position);
        Viewholder viewholder = null;
        Viewholder_first viewholder_first = null;
        final int position1 = position;
        if (convertView == null) {
            if (type == TYPE_NO_First) {
                view = LayoutInflater.from(context).inflate(R.layout.listview_item,
                        null);
                convertView = view;
                viewholder = new Viewholder();
                viewholder.gridview = (MyGridview) view
                        .findViewById(R.id.mygridview);
                viewholder.date = (TextView) view.findViewById(R.id.date);
                viewholder.choseall = (ImageView) view.findViewById(R.id.select);
                convertView.setTag(viewholder);
            } else {
                view = LayoutInflater.from(context).inflate(R.layout.listview_firstitem,
                        null);
                convertView = view;
                viewholder_first = new Viewholder_first();
                viewholder_first.gridview = (MyGridview) view
                        .findViewById(R.id.mygridview);
                viewholder_first.date = (TextView) view.findViewById(R.id.date);
                viewholder_first.choseall = (ImageView) view.findViewById(R.id.select);
                convertView.setTag(viewholder_first);
            }
        } else {
            if (type == TYPE_NO_First) {
                view = convertView;
                viewholder = (Viewholder) view.getTag();
                Log.e("convertView !!!!!!= ", "NULL TYPE_NO_First");
            } else {
                view = convertView;
                viewholder_first = (Viewholder_first) view.getTag();
                Log.e("convertView !!!!!!= ", "NULL TYPE_First");
            }
        }
        final String date = datelist.get(position);
        final ArrayList<String> filelist = filepathlist.get(date);
//        final ArrayList<String> filelist = new ArrayList<String>();
        final LoadPhotoToViewActivity_Gridview_Adapter adapter = new LoadPhotoToViewActivity_Gridview_Adapter(context, filelist, position);

        if (type == TYPE_NO_First) {
            final Viewholder viewholder1 = viewholder;
            viewholder1.gridview.setAdapter(adapter);
            viewholder1.gridview.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    if (!needMoveFile.isinNeedmovefile(filelist
                            .get(position))) {
                        view.findViewById(position).setVisibility(
                                View.VISIBLE);
                        needMoveFile.addNeedmovefile(filelist.get(
                                position));
                    } else {
                        view.findViewById(position).setVisibility(
                                View.GONE);
                        needMoveFile.removefile(filelist.get(
                                position));
                        Log.i("tiaoshi", position + "存在" + filelist
                                .get(position));
                    }
                    if (needMoveFile.isExistinList(filelist)) {
                        viewholder1.choseall.setImageResource(R.drawable.check_choose);
                        Integer integer = new Integer(1);
                        needMoveFile.putPositemap(position1, integer);
                    } else {
                        viewholder1.choseall.setImageResource(R.drawable.unchoose);
                        Integer integer = new Integer(0);
                        needMoveFile.putPositemap(position1, integer);
                    }
                    if (needMoveFile.needmoveFile.size() == 0) {
                        mListener.hide_choose_detail_linearlayout();
                    } else {
                        mListener.show_choose_detail_linearlayout(needMoveFile.needmoveFile.size());
                    }
                }
            });
            viewholder1.gridview
                    .setOnItemLongClickListener(new OnItemLongClickListener() {

                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent,
                                                       View view, int position, long id) {
                            if (needMoveFile.isinNeedmovefile(adapter
                                    .getListfilename().get(position))) {
                                int Pos[] = {-1, -1};
                                view.getLocationOnScreen(Pos);
                                Log.i("path", Pos[0] + " " + Pos[1]);
                                group.CreateMoveGroup(Pos[0], Pos[1], adapter.getListfilename().get(position));
                            }
                            return true;
                        }
                    });
            viewholder1.choseall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!needMoveFile.getPositemap(position)) {
                        List<String> listfile = adapter.getListfilename();
                        needMoveFile.addNeedmovefileList(listfile);
                        for (int i = viewholder1.gridview.getFirstVisiblePosition(); i <= viewholder1.gridview.getLastVisiblePosition(); i++) {
                            viewholder1.gridview.findViewById(i).setVisibility(View.VISIBLE);
                        }
                        Integer integer = new Integer(1);
                        needMoveFile.putPositemap(position1, integer);
                        viewholder1.choseall.setImageResource(R.drawable.check_choose);
                        if (needMoveFile.needmoveFile.size() == 0) {
                            mListener.hide_choose_detail_linearlayout();
                        } else {
                            mListener.show_choose_detail_linearlayout(needMoveFile.needmoveFile.size());
                        }
                    } else {
                        Log.i("TAG", "remove: ");
                        List<String> listfile = adapter.getListfilename();
                        needMoveFile.removeNeedmovefileList(listfile);
                        for (int i = viewholder1.gridview.getFirstVisiblePosition(); i <= viewholder1.gridview.getLastVisiblePosition(); i++) {
                            viewholder1.gridview.findViewById(i).setVisibility(View.GONE);
                        }
                        Integer integer = new Integer(0);
                        needMoveFile.putPositemap(position1, integer);
                        viewholder1.choseall.setImageResource(R.drawable.unchoose);
                        if (needMoveFile.needmoveFile.size() == 0) {
                            mListener.hide_choose_detail_linearlayout();
                        } else {
                            mListener.show_choose_detail_linearlayout(needMoveFile.needmoveFile.size());
                        }
                    }
                }
            });
            if (needMoveFile.isExistinList(filelist)) {
                viewholder1.choseall.setImageResource(R.drawable.btnback);
                Integer integer = new Integer(1);
                needMoveFile.putPositemap(position1, integer);
            } else {
                viewholder1.choseall.setImageResource(R.drawable.yujiazai);
                Integer integer = new Integer(0);
                needMoveFile.putPositemap(position1, integer);
            }
            if (needMoveFile.getPositemap(position)) {
                viewholder1.choseall.setImageResource(R.drawable.check_choose);
            } else {
                viewholder1.choseall.setImageResource(R.drawable.unchoose);
            }
            if (needMoveFile.needmoveFile.size() == 0) {
                mListener.hide_choose_detail_linearlayout();
            } else {
                mListener.show_choose_detail_linearlayout(needMoveFile.needmoveFile.size());
            }
            viewholder1.date.setText(date);
        } else {
            final Viewholder_first Viewholder_first1 = viewholder_first;
            Viewholder_first1.gridview.setAdapter(adapter);
            Viewholder_first1.gridview.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    if (!needMoveFile.isinNeedmovefile(filelist
                            .get(position))) {
                        view.findViewById(position).setVisibility(
                                View.VISIBLE);
                        needMoveFile.addNeedmovefile(filelist.get(
                                position));
                        Log.i("tiaoshi", position + "不存在" + filelist
                                .get(position));
                    } else {
                        view.findViewById(position).setVisibility(
                                View.GONE);
                        needMoveFile.removefile(filelist.get(
                                position));
                        ;
                        Log.i("tiaoshi", position + "存在" + filelist
                                .get(position));
                    }
                    if (needMoveFile.isExistinList(filelist)) {
                        Viewholder_first1.choseall.setImageResource(R.drawable.check_choose);
                        Integer integer = new Integer(1);
                        needMoveFile.putPositemap(position1, integer);
                    } else {
                        Viewholder_first1.choseall.setImageResource(R.drawable.unchoose);
                        Integer integer = new Integer(0);
                        needMoveFile.putPositemap(position1, integer);
                    }
                    if (needMoveFile.needmoveFile.size() == 0) {
                        mListener.hide_choose_detail_linearlayout();
                    } else {
                        mListener.show_choose_detail_linearlayout(needMoveFile.needmoveFile.size());
                    }
                }
            });
            Viewholder_first1.gridview
                    .setOnItemLongClickListener(new OnItemLongClickListener() {

                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent,
                                                       View view, int position, long id) {
                            if (needMoveFile.isinNeedmovefile(adapter
                                    .getListfilename().get(position))) {
                                int Pos[] = {-1, -1};
                                view.getLocationOnScreen(Pos);
                                group.CreateMoveGroup(Pos[0], Pos[1], adapter.getListfilename().get(position));
                            }
                            return true;
                        }
                    });
            Viewholder_first1.choseall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!needMoveFile.getPositemap(position)) {
                        List<String> listfile = adapter.getListfilename();
                        needMoveFile.addNeedmovefileList(listfile);
                        for (int i = Viewholder_first1.gridview.getFirstVisiblePosition(); i <= Viewholder_first1.gridview.getLastVisiblePosition(); i++) {
                            Viewholder_first1.gridview.findViewById(i).setVisibility(View.VISIBLE);
                        }
                        Integer integer = new Integer(1);
                        needMoveFile.putPositemap(position1, integer);
                        Viewholder_first1.choseall.setImageResource(R.drawable.check_choose);
                        if (needMoveFile.needmoveFile.size() == 0) {
                            mListener.hide_choose_detail_linearlayout();
                        } else {
                            mListener.show_choose_detail_linearlayout(needMoveFile.needmoveFile.size());
                        }
                    } else {
                        Log.i("TAG", "remove: ");
                        List<String> listfile = adapter.getListfilename();
                        needMoveFile.removeNeedmovefileList(listfile);
                        for (int i = Viewholder_first1.gridview.getFirstVisiblePosition(); i <= Viewholder_first1.gridview.getLastVisiblePosition(); i++) {
                            Viewholder_first1.gridview.findViewById(i).setVisibility(View.GONE);
                        }
                        Integer integer = new Integer(0);
                        needMoveFile.putPositemap(position1, integer);
                        Viewholder_first1.choseall.setImageResource(R.drawable.unchoose);
                        if (needMoveFile.needmoveFile.size() == 0) {
                            mListener.hide_choose_detail_linearlayout();
                        } else {
                            mListener.show_choose_detail_linearlayout(needMoveFile.needmoveFile.size());
                        }
                    }
                }
            });
            if (needMoveFile.isExistinList(filelist)) {
                Viewholder_first1.choseall.setImageResource(R.drawable.btnback);
                Integer integer = new Integer(1);
                needMoveFile.putPositemap(position1, integer);
            } else {
                Viewholder_first1.choseall.setImageResource(R.drawable.yujiazai);
                Integer integer = new Integer(0);
                needMoveFile.putPositemap(position1, integer);
            }
            if (needMoveFile.getPositemap(position)) {
                Viewholder_first1.choseall.setImageResource(R.drawable.check_choose);
            } else {
                Viewholder_first1.choseall.setImageResource(R.drawable.unchoose);
            }
            if (needMoveFile.needmoveFile.size() == 0) {
                mListener.hide_choose_detail_linearlayout();
            } else {
                mListener.show_choose_detail_linearlayout(needMoveFile.needmoveFile.size());
            }
            Viewholder_first1.date.setText(date);
        }
        return view;
    }

    class Viewholder {
        customview.MyGridview gridview;
        TextView date;
        ImageView choseall;
    }

    class Viewholder_first {
        customview.MyGridview gridview;
        TextView date;
        ImageView choseall;
    }

}
