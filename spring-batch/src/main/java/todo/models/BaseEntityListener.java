package todo.models;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

public class BaseEntityListener {

    @PrePersist
    public void onPrePersist(BaseModel baseModel) {
        baseModel.setCreatedDate(new Date());
        baseModel.setUpdateDate(new Date());
    }

    @PreUpdate
    public void onPreUpdate(BaseModel baseModel) {
        baseModel.setUpdateDate(new Date());
    }
}
