package eventbustype;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/10 0010.
 */
public class AddPhotoSourceEventType {
    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public List<String> list=new ArrayList<String>();

    public AddPhotoSourceEventType(List<String> list) {
        this.list = list;
    }
}
