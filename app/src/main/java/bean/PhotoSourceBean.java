package bean;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Administrator on 2016/6/9 0009.
 */
@Table(name = "PhotoSourceBeans")
public class PhotoSourceBean extends Model {
    @Column(name = "SourcePath")
    String SourcePath;

    public Boolean getChooseState() {
        return ChooseState;
    }

    public void setChooseState(Boolean chooseState) {
        ChooseState = chooseState;
    }

    public String getSourcePath() {
        return SourcePath;
    }

    public void setSourcePath(String sourcePath) {
        SourcePath = sourcePath;
    }

    @Column(name = "ChooseState")

    Boolean ChooseState;
}
