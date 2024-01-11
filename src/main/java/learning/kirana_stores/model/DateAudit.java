package learning.kirana_stores.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class DateAudit implements Serializable {
    private Date createdAt;
    private Date updatedAt;
}
