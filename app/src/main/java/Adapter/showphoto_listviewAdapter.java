package adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.gxl.photofinishing.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.File;
import java.util.ArrayList;

import data.needMoveFile;
import utils.BitmapUtils;
import utils.LogUtils;
import utils.ScreenUtils;
import utils.fileUtils;

/**
 * Created by gxl on 2016/4/13.
 */
public class showphoto_listviewAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> listfilepath = new ArrayList<String>();
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    final int TYPE_NO_First = 0;
    final int TYPE_First = 1;
    private int delete_type = 0;
    private MenuListener menuListener;

    public void setMenuListener(MenuListener menuListener) {
        this.menuListener = menuListener;
    }

    /**
     * 菜单操作接口
     */
   public interface MenuListener {
        void delete(int position);

        void huanyuan(int position);

        void chongmingming(int position);

        void beifen(int position);
    }




    public showphoto_listviewAdapter(Context context, ArrayList<String> list, int type) {
        this.context = context;
        this.listfilepath = list;
        delete_type = type;
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.yujiazai)
                .showImageForEmptyUri(R.drawable.yujiazai)
                .showImageOnFail(R.drawable.yujiazai).cacheInMemory()
                .cacheOnDisc().displayer(new RoundedBitmapDisplayer(20))
                .displayer(new FadeInBitmapDisplayer(300)).build();
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


    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder viewholder = null;
        Viewholder_first viewholder_first = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            if (type == TYPE_NO_First) {
                convertView = LayoutInflater.from(context).inflate(R.layout.showphoto_listviewitem, null);
                viewholder = new ViewHolder();
                view = convertView;
                viewholder.wenjianjia_photo = (ImageView) view.findViewById(R.id.wenjianjia_photo);
                viewholder.wenjianjia_title = (TextView) view.findViewById(R.id.wenjianjia_title);
                viewholder.wenjianjia_number = (TextView) view.findViewById(R.id.wenjianjia_number);
                viewholder.show_detail = (ImageView) view.findViewById(R.id.show_detail);
                viewholder.check_choose = (ImageView) view.findViewById(R.id.check_choose);
                convertView.setTag(viewholder);
            } else {
                convertView = LayoutInflater.from(context).inflate(R.layout.showphoto_listviewitem_first, null);
                viewholder_first = new Viewholder_first();
                view = convertView;
                viewholder_first.wenjianjia_photo = (ImageView) view.findViewById(R.id.wenjianjia_photo);
                viewholder_first.wenjianjia_title = (TextView) view.findViewById(R.id.wenjianjia_title);
                viewholder_first.wenjianjia_number = (TextView) view.findViewById(R.id.wenjianjia_number);
                viewholder_first.show_detail = (ImageView) view.findViewById(R.id.show_detail);
                viewholder_first.check_choose = (ImageView) view.findViewById(R.id.check_choose);
                convertView.setTag(viewholder_first);
            }
        } else {
            if (type == TYPE_NO_First) {
                view = convertView;
                viewholder = (ViewHolder) view.getTag();
                Log.e("convertView !!!!!!= ", "NULL TYPE_NO_First");
            } else {
                view = convertView;
                viewholder_first = (Viewholder_first) view.getTag();
                Log.e("convertView !!!!!!= ", "NULL TYPE_First");
            }
        }
        if (type == TYPE_NO_First) {
            if ((new File(listfilepath.get(position)).listFiles().length != 0)) {
                File file = new File(fileUtils.getExistFileBitmap(listfilepath.get(position)));
                imageLoader.displayImage("file:///" + fileUtils.getExistFileBitmap(listfilepath.get(position)), viewholder.wenjianjia_photo,
                        options);
                viewholder.wenjianjia_number.setText(new File(listfilepath.get(position)).listFiles().length + "");
            } else {
                viewholder.wenjianjia_photo.setBackgroundResource(R.drawable.wenjianjia_background);
                viewholder.wenjianjia_number.setText("0");
            }
            viewholder.check_choose.setId(position);
            viewholder.wenjianjia_title.setText(listfilepath.get(position).substring(listfilepath.get(position).lastIndexOf("/") + 1, listfilepath.get(position).length()));
            if (delete_type == 0) {
                viewholder.check_choose.setVisibility(View.GONE);
                viewholder.show_detail.setVisibility(View.VISIBLE);
                viewholder.show_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopupWindow(v, initPopupWindow(R.layout.popupmenu_layout, position));
                    }
                });
            } else {
                viewholder.check_choose.setVisibility(View.VISIBLE);
                viewholder.show_detail.setVisibility(View.GONE);
                if (delete_type == 1) {
                    if (needMoveFile.isinNeeddeletefile(listfilepath.get(position))) {
                        viewholder.check_choose.setBackgroundResource(R.drawable.check_choose);
                    } else {
                        viewholder.check_choose.setBackgroundResource(R.drawable.check_unchoose);
                    }
                } else {
                    if (needMoveFile.choose_item == position) {
                        viewholder.check_choose.setBackgroundResource(R.drawable.check_choose);
                    } else {
                        viewholder.check_choose.setBackgroundResource(R.drawable.check_unchoose);
                    }
                }
            }
        } else {
            if ((new File(listfilepath.get(position)).listFiles().length != 0)) {
                File file = new File(fileUtils.getExistFileBitmap(listfilepath.get(position)));
                imageLoader.displayImage("file:///" + fileUtils.getExistFileBitmap(listfilepath.get(position)), viewholder_first.wenjianjia_photo,
                        options);
                viewholder_first.wenjianjia_number.setText(new File(listfilepath.get(position)).listFiles().length + "");
            } else {
                viewholder_first.wenjianjia_photo.setBackgroundResource(R.drawable.wenjianjia_background);
                viewholder_first.wenjianjia_number.setText("0");
            }
            viewholder_first.wenjianjia_title.setText(listfilepath.get(position).substring(listfilepath.get(position).lastIndexOf("/") + 1, listfilepath.get(position).length()));
            viewholder_first.check_choose.setId(position);
            if (delete_type == 0) {
                viewholder_first.check_choose.setVisibility(View.GONE);
                viewholder_first.show_detail.setVisibility(View.VISIBLE);
                viewholder_first.show_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopupWindow(v, initPopupWindow(R.layout.popupmenu_layout, position));
                    }
                });
            } else {
                viewholder_first.check_choose.setVisibility(View.VISIBLE);
                viewholder_first.show_detail.setVisibility(View.GONE);
                if (delete_type == 1) {
                    if (needMoveFile.isinNeeddeletefile(listfilepath.get(position))) {
                        viewholder_first.check_choose.setBackgroundResource(R.drawable.check_choose);
                    } else {
                        viewholder_first.check_choose.setBackgroundResource(R.drawable.check_unchoose);
                    }
                } else {
                    if (needMoveFile.choose_item == position) {
                        viewholder_first.check_choose.setBackgroundResource(R.drawable.check_choose);
                    } else {
                        viewholder_first.check_choose.setBackgroundResource(R.drawable.check_unchoose);
                    }
                }
            }
        }
        return view;
    }


    /**
     * 将图片变为文件夹形状
     *
     * @param source
     * @return
     */
    private Bitmap createwenjianjiaImage(Bitmap source) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = BitmapUtils.DrawableTobitmap(R.drawable.wenjianjia_background, 86, 65, context);
        Canvas canvas = new Canvas(target);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

    /**
     * 给图片添加一个圆角
     */

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private Bitmap createCircleImage(Bitmap source) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap mOut = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mOut);
        canvas.drawRoundRect(0, 0, source.getWidth(), source.getHeight(), 10, 10, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        return mOut;
    }

    /**
     * @param resId
     */
    private PopupWindow initPopupWindow(int resId, final int posite) {
        View view;
        final PopupWindow mPopupWindow;
        TextView[] btns;
        view = LayoutInflater.from(context).inflate(resId, null);
        mPopupWindow = new PopupWindow(view, ScreenUtils.dip2px(context, 94), ScreenUtils.dip2px(context, 146));
//      mPopupWindow.setBackgroundDrawable(new BitmapDrawable());//必须设置background才能消失
        mPopupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.baize));
        mPopupWindow.setOutsideTouchable(true);

        //自定义动画
//      mPopupWindow.setAnimationStyle(R.style.PopupAnimation);
        //使用系统动画
        mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        mPopupWindow.update();
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);

        btns = new TextView[4];
        btns[0] = (TextView) view.findViewById(R.id.shanchu);
        btns[1] = (TextView) view.findViewById(R.id.huanyuan);
        btns[2] = (TextView) view.findViewById(R.id.chongmingming);
        btns[3] = (TextView) view.findViewById(R.id.beifen);
        btns[0].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //做删除操作并更新该listview
                mPopupWindow.dismiss();
                menuListener.delete(posite);
            }
        });
        btns[1].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                LogUtils.loggxl("huanyuan" + posite);
                mPopupWindow.dismiss();
                menuListener.huanyuan(posite);
            }
        });
        btns[2].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //重命名操作
                mPopupWindow.dismiss();
                menuListener.chongmingming(posite);
            }
        });

        btns[3].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //备份操作
                mPopupWindow.dismiss();
                menuListener.beifen(posite);
            }
        });
        return mPopupWindow;
    }

    /**
     * 点击出现popupmenu
     *
     * @param view
     * @param mPopupWindow
     */
    private void showPopupWindow(View view, PopupWindow mPopupWindow) {
        if (!mPopupWindow.isShowing()) {
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            mPopupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0] - mPopupWindow.getWidth(), location[1]);
        }
    }


    class ViewHolder {
        ImageView wenjianjia_photo;
        TextView wenjianjia_title;
        TextView wenjianjia_number;
        ImageView show_detail;
        ImageView jiantou;
        ImageView check_choose;
    }

    class Viewholder_first {
        ImageView wenjianjia_photo;
        TextView wenjianjia_title;
        TextView wenjianjia_number;
        ImageView show_detail;
        ImageView check_choose;
    }

}
