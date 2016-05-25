package eventbustype;

/**
 * ShowPhoto向MainActivity发送事件消息
 */
public class FirstEventType {
    public FirstEventType(int flag) {
        this.flag = flag;
    }
    public int getFlag() {
        return flag;
    }
    public int flag;
}
