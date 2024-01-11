package learning.kirana_stores.configuration;

import learning.kirana_stores.services.JwtFilterRequest;
import learning.kirana_stores.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *
 */
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtFilterRequest jwtFilterRequest;

    /*@Autowired
    public SecurityConfiguration securityConfiguration(JwtFilterRequest jwtFilterRequest,UserService userService){
        this.jwtFilterRequest = jwtFilterRequest;
        this.userService = userService;
    }*/

    @Override
    protected void configure (AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService (userService);
    }

    /**
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure (HttpSecurity http) throws Exception {
        http.csrf ()
                .disable ()
                .authorizeRequests ()
                .antMatchers ("/auth", "signup")
                .permitAll ();

        http.addFilterBefore (jwtFilterRequest, UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder () {
        return NoOpPasswordEncoder.getInstance ();
    }

    /**
     * @return
     * @throws Exception
     */
    @Override
    @Bean (name = BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean () throws Exception {
        return super.authenticationManagerBean ();
    }

}
