package eventbustype;

/**
 * Created by Administrator on 2016/5/25 0025.
 */

/**
 * 查看单张图片Activity向查看照片文件夹发送消息
 */
public class ShowPhotoDetailType {
    public ShowPhotoDetailType(int mFlag) {
        this.mFlag = mFlag;
    }
    public int getmFlag() {
        return mFlag;
    }
    private int mFlag;

}
