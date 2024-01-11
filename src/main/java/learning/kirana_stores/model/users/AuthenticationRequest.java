package learning.kirana_stores.model.users;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String username;
    private String password;

    public AuthenticationRequest () {
    }

}
