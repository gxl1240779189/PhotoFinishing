package utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.gxl.photofinishing.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BitmapUtils {

	/**
	 * 将图片文件列表转化成Bitmap
	 * @param list
	 * @param reqWidth
	 * @param reqheight
	 * @return
	 */
	public static List<Bitmap> fileTobitmap(List<File> list, int reqWidth,
			int reqheight) {
		List<Bitmap> listBitmap = new ArrayList<Bitmap>();
		Bitmap bitmap;
		for (int i = 0; i < list.size(); i++) {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(list.get(i).getAbsolutePath(), opts);
			// opts.inSampleSize = calculateInSampleSize(opts, reqWidth,
			// reqheight);
			opts.inSampleSize = 2;
			opts.inJustDecodeBounds = false;
			Bitmap bmp = BitmapFactory.decodeFile(
					list.get(i).getAbsolutePath(), opts);
			Bitmap bmp2 = ImageCrop(bmp,reqWidth,reqheight);
			listBitmap.add(bmp2);
		}
		return listBitmap;
	}

	/**
	 * 将单个的图片文件转化成固定大小的bitmap
	 * @param file
	 * @param reqWidth   宽
	 * @param reqheight  高
	 * @return
	 */
	public static Bitmap fileTobitmap(File file, int reqWidth, int reqheight) {
		List<Bitmap> listBitmap = new ArrayList<Bitmap>();
		Bitmap bitmap;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
		opts.inSampleSize = calculateInSampleSize(opts, reqWidth, reqheight);
		opts.inJustDecodeBounds = false;
		Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
		if((opts.outHeight<reqheight) || (opts.outWidth < reqWidth))
		{
			return bmp;
		}
		Bitmap bmp2 = ImageCrop(bmp, reqWidth, reqheight);
		return bmp2;
	}

	/**
	 * 不转化成固定大小
	 * @param file
	 * @param reqWidth
	 * @param reqheight
	 * @return
	 */

	public static Bitmap fileTobitmap2(File file, int reqWidth, int reqheight) {
		List<Bitmap> listBitmap = new ArrayList<Bitmap>();
		Bitmap bitmap;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
		opts.inSampleSize = calculateInSampleSize(opts, reqWidth, reqheight);
		opts.inJustDecodeBounds = false;
		Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
		return bmp;
	}

	/**
	 * 将res中的文件转化成固定大小的bitmap
	 * @param resid
	 * @param reqWidth
	 * @param reqheight
	 * @return
	 */
	public static Bitmap DrawableTobitmap(int resid, int reqWidth, int reqheight,Context context) {
		List<Bitmap> listBitmap = new ArrayList<Bitmap>();
		Bitmap bitmap;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(context.getResources(), R.drawable.wenjianjia_background,opts);
		opts.inSampleSize = calculateInSampleSize(opts, reqWidth, reqheight);
		opts.inJustDecodeBounds = false;
		Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.wenjianjia_background, opts);
		if((opts.outHeight<reqheight)||(opts.outWidth<reqWidth))
		{
			return bmp;
		}
		Bitmap bmp2 = ImageCrop(bmp,reqWidth,reqheight);
		return bmp2;
	}

	/**
	 * 计算需要缩放的比例
	 * @param options
	 * @param reqWidth
	 * @param reqheight
	 * @return
	 */

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqheight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int samplesize = 1;
		if (height > reqheight || width > reqWidth) {
			final int halfheight = height / 2;
			final int halfwight = width / 2;
			while ((halfheight / samplesize) >= reqheight
					&& (halfwight / samplesize) >= reqWidth) {
				samplesize *= 2;
			}
		}
		return samplesize;
	}

	/**
	 * 将bitmap变成固定宽高的大小
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap ImageCrop(Bitmap bitmap,int width,int height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		int wh = w > h ? h : w;
		int retX = w > h ? (w - h) / 2 : 0;
		int retY = w > h ? 0 : (h - w) / 2;
		return Bitmap.createBitmap(bitmap, retX, retY, width, height, null, false);
	}
}
