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
     * Loads user details by username for authentication.
     *
     * @param username The username to load user details.
     * @return UserDetails for the specified username.
     * @throws UsernameNotFoundException If the username is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find the user in the repository based on the provided username
        UserModel userPresent = userRepository.findByUsername(username);

        // If user not found, throw UsernameNotFoundException
        if (userPresent == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // Return UserDetails with the found user's username, password, and an empty list of authorities
        return new User(userPresent.getUsername(), userPresent.getPassword(), new ArrayList<>());
    }
}
