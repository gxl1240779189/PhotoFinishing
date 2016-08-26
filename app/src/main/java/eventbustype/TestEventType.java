package eventbustype;

import java.util.ArrayList;
import java.util.List;

/**
 * 查看单张图片的Test向MainActivity发送消息
 */
public class TestEventType {

    public TestEventType(ArrayList<String> mListViewPosition, int position) {
        this.mListViewPosition = mListViewPosition;
        this.position = position;
    }

    public ArrayList<String> getmListViewPosition() {
        return mListViewPosition;
    }

    private ArrayList<String> mListViewPosition;
    private int position;

    public int getPosition() {
        return position;
    }

}
