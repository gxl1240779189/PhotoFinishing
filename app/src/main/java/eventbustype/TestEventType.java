package eventbustype;

import java.util.List;

/**
 * 查看单张图片的Test向MainActivity发送消息
 */
public class TestEventType {
    public int getmListViewPosition() {
        return mListViewPosition;
    }

    private int mListViewPosition;
    private int position;
    private Boolean flag;

    public int getPosition() {
        return position;
    }

    public Boolean getFlag() {
        return flag;
    }

    public TestEventType(int mListViewPosition, int position, Boolean flag) {
        this.mListViewPosition = mListViewPosition;
        this.position = position;
        this.flag = flag;
    }
}
