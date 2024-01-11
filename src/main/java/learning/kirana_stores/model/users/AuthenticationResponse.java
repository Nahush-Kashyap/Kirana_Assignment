package learning.kirana_stores.model.users;

import lombok.Data;

@Data
public class AuthenticationResponse {

    private String response;

    public AuthenticationResponse (String response) {
        this.response = response;
    }

}
