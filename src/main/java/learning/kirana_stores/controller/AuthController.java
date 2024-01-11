package learning.kirana_stores.controller;

import learning.kirana_stores.entities.UserModel;
import learning.kirana_stores.model.users.AuthenticationRequest;
import learning.kirana_stores.model.users.AuthenticationResponse;
import learning.kirana_stores.repository.UserRepository;
import learning.kirana_stores.services.UserService;
import learning.kirana_stores.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    /**
     * @param userRepository
     * @param authenticationManager
     * @param userService
     * @param jwtUtils
     */
    @Autowired
    public AuthController (UserRepository userRepository, AuthenticationManager authenticationManager,
                           UserService userService,
                           JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping ("/signup")
    private ResponseEntity<?> SignupClient (@RequestBody AuthenticationRequest authenticationRequest) {
        String username = authenticationRequest.getUsername ();
        String password = authenticationRequest.getPassword ();
        UserModel user = userRepository.findByUsername (username);
        if (user != null) {
            return ResponseEntity.badRequest ().body ("User already exists");
        }

        UserModel userModel = new UserModel ();
        userModel.setUsername (username);
        userModel.setPassword (password);
        try {
            userRepository.save (userModel);
            return ResponseEntity.ok (new AuthenticationResponse (username + " successfully signed up!!"));
        } catch (Exception e) {
            return ResponseEntity.ok (new AuthenticationResponse ("Unable to complete Signup"));
        }

    }

    @PostMapping ("/auth")
    private ResponseEntity<?> authenticateClient (@RequestBody AuthenticationRequest authenticationRequest) {
        String username = authenticationRequest.getUsername ();
        String password = authenticationRequest.getPassword ();
        try {
            authenticationManager.authenticate (new UsernamePasswordAuthenticationToken (username, password));

        } catch (Exception e) {
            return ResponseEntity.ok (new AuthenticationResponse ("Error during Authentication"));
        }

        UserDetails loadedUser = userService.loadUserByUsername (username);
        String generatedToken = jwtUtils.generateToken (loadedUser);
        return ResponseEntity.ok (new AuthenticationResponse (generatedToken));
    }
}
