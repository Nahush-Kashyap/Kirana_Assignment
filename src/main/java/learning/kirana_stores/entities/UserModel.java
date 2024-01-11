package learning.kirana_stores.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document (collection = "Security ")
public class UserModel {

    @Id
    private String id;

    @Indexed (unique = true)
    private String username;
    private String password;

}
