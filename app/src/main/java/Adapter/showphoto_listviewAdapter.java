package Adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.gxl.photofinishing.R;
import com.example.gxl.photofinishing.showPhotoDetail;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.File;
import java.util.ArrayList;

import Data.needMoveFile;
import Utils.BitmapUtils;
import Utils.fileUtils;
import myView.myGridview;

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
    private int delete_type=0;



    public showphoto_listviewAdapter(Context context, ArrayList<String> list,int type) {
        this.context = context;
        this.listfilepath = list;
        delete_type=type;
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
                viewholder.check_choose= (ImageView) view.findViewById(R.id.check_choose);
                convertView.setTag(viewholder);
            } else {
                convertView = LayoutInflater.from(context).inflate(R.layout.showphoto_listviewitem_first, null);
                viewholder_first = new Viewholder_first();
                view = convertView;
                viewholder_first.wenjianjia_photo = (ImageView) view.findViewById(R.id.wenjianjia_photo);
                viewholder_first.wenjianjia_title = (TextView) view.findViewById(R.id.wenjianjia_title);
                viewholder_first.wenjianjia_number = (TextView) view.findViewById(R.id.wenjianjia_number);
                viewholder_first.show_detail = (ImageView) view.findViewById(R.id.show_detail);
                viewholder_first.check_choose= (ImageView) view.findViewById(R.id.check_choose);
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
        if(type==TYPE_NO_First) {
            if((new File(listfilepath.get(position)).listFiles().length!=0))
            {
                File file = new File(fileUtils.getExistFileBitmap(listfilepath.get(position)));
                imageLoader.displayImage("file:///"+fileUtils.getExistFileBitmap(listfilepath.get(position)), viewholder.wenjianjia_photo,
                        options);
                viewholder.wenjianjia_number.setText(new File(listfilepath.get(position)).listFiles().length + "");
            }else
            {
                viewholder.wenjianjia_photo.setBackgroundResource(R.drawable.wenjianjia_background);
                viewholder.wenjianjia_number.setText("0");
            }
            viewholder.check_choose.setId(position);
            viewholder.wenjianjia_title.setText(listfilepath.get(position).substring(listfilepath.get(position).lastIndexOf("/") + 1, listfilepath.get(position).length()));
            if(delete_type==0)
            {
                viewholder.check_choose.setVisibility(View.GONE);
                viewholder.show_detail.setVisibility(View.VISIBLE);
            }else
            {
                viewholder.check_choose.setVisibility(View.VISIBLE);
                viewholder.show_detail.setVisibility(View.GONE);
                if(delete_type==1) {
                    if (needMoveFile.isinNeeddeletefile(listfilepath.get(position))) {
                        viewholder.check_choose.setBackgroundResource(R.drawable.check_choose);
                    } else {
                        viewholder.check_choose.setBackgroundResource(R.drawable.check_unchoose);
                    }
                }                                                                                                                                                                                                                                                                                    else
                {
                    if (needMoveFile.choose_item==position) {
                        viewholder.check_choose.setBackgroundResource(R.drawable.check_choose);
                    } else {
                        viewholder.check_choose.setBackgroundResource(R.drawable.check_unchoose);
                    }
                }
            }
        }else
        {
            if((new File(listfilepath.get(position)).listFiles().length!=0))
            {
                File file = new File(fileUtils.getExistFileBitmap(listfilepath.get(position)));
                imageLoader.displayImage("file:///"+fileUtils.getExistFileBitmap(listfilepath.get(position)), viewholder_first.wenjianjia_photo,
                        options);
                viewholder_first.wenjianjia_number.setText(new File(listfilepath.get(position)).listFiles().length + "");
            }else
            {
                viewholder_first.wenjianjia_photo.setBackgroundResource(R.drawable.wenjianjia_background);
                viewholder_first.wenjianjia_number.setText("0");
            }
            viewholder_first.wenjianjia_title.setText(listfilepath.get(position).substring(listfilepath.get(position).lastIndexOf("/") + 1, listfilepath.get(position).length()));
            viewholder_first.check_choose.setId(position);
            if(delete_type==0)
            {
                viewholder_first.check_choose.setVisibility(View.GONE);
                viewholder_first.show_detail.setVisibility(View.VISIBLE);
            }else
            {
                viewholder_first.check_choose.setVisibility(View.VISIBLE);
                viewholder_first.show_detail.setVisibility(View.GONE);
                if(delete_type==1) {
                    if (needMoveFile.isinNeeddeletefile(listfilepath.get(position))) {
                        viewholder_first.check_choose.setBackgroundResource(R.drawable.check_choose);
                    } else {
                        viewholder_first.check_choose.setBackgroundResource(R.drawable.check_unchoose);
                    }
                }else
                {
                    if (needMoveFile.choose_item==position) {
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
     * @param source
     * @return
     */
    private Bitmap createwenjianjiaImage(Bitmap source) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = BitmapUtils.DrawableTobitmap(R.drawable.wenjianjia_background,86,65,context);
        Canvas canvas= new Canvas(target);
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
        Bitmap mOut=Bitmap.createBitmap(source.getWidth(),source.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(mOut);
        canvas.drawRoundRect(0, 0, source.getWidth(), source.getHeight(), 10, 10, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source,0,0,paint);
        return mOut;
    }


    class ViewHolder {
        ImageView wenjianjia_photo;
        TextView wenjianjia_title;
        TextView wenjianjia_number;
        ImageView show_detail;
        ImageView jiantou;
        ImageView  check_choose;
    }

    class Viewholder_first {
        ImageView wenjianjia_photo;
        TextView wenjianjia_title;
        TextView wenjianjia_number;
        ImageView show_detail;
        ImageView  check_choose;
    }

}
