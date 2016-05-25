package utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

public  class sdUtils {
	

	public static boolean ExistSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}

	public static void CreateFile(String name,Context context) {
	
		if (ExistSDCard()) {
			Log.i("tag", "111");
			File sdcardDir = Environment.getExternalStorageDirectory();
			String path = sdcardDir.getPath() + "/"+"test"+"/" + name;
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			} else {
				Toast.makeText(context, "当前文件夹已经存在!", Toast.LENGTH_LONG).show();
			}
		}
	}
}
