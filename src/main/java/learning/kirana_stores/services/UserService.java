package learning.kirana_stores.services;

import learning.kirana_stores.entities.UserModel;
import learning.kirana_stores.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * @param s
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername (String s) throws UsernameNotFoundException {
        UserModel userPresent = userRepository.findByUsername (s);
        if (userPresent == null) {
            return null;
        }

        return new User (userPresent.getUsername (), userPresent.getPassword (), new ArrayList<> ());
    }
}
