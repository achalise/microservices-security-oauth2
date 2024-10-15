package example.authserver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.Collection;
import java.util.List;
import java.util.Map;

//@Configuration
public class Config {
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user1 = User.withUsername("user2")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .authorities("profile", "openid")
                .build();

        UserDetails admin = User.withUsername("testuser")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .authorities("profile", "openid", "message.read", "message.write")
                .build();

        return new InMemoryUserDetailsManager(user1, admin);
    }

    // Define the password encoder (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
