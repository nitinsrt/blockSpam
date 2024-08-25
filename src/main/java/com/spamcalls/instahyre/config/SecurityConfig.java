package com.spamcalls.instahyre.config;

import com.spamcalls.instahyre.entities.Contact;
import com.spamcalls.instahyre.entities.User;
import com.spamcalls.instahyre.filters.JWTAuthenticationFilter;
import com.spamcalls.instahyre.filters.JWTAuthorizationFilter;
import com.spamcalls.instahyre.repository.ContactRepository;
import com.spamcalls.instahyre.repository.UserRepository;
import com.spamcalls.instahyre.utils.JwtUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public JwtUtils jwtUtils(){
        return new JwtUtils();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter(authenticationManager);
        JWTAuthorizationFilter jwtAuthorizationFilter = new JWTAuthorizationFilter(authenticationManager);


        http
                .csrf(csrf->csrf.disable()) // Disable CSRF if not needed
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/register", "/api/login").permitAll() // Allow registration and login endpoints
                        .anyRequest().authenticated() // Protect all other endpoints
                )
                .addFilter(jwtAuthenticationFilter) // Register JWT Authentication Filter
                .addFilter(jwtAuthorizationFilter); // Register JWT Authorization Filter

        return http.build();
    }

    @Bean
    public CommandLineRunner populateDatabase(UserRepository userRepository, ContactRepository contactRepository, BCryptPasswordEncoder passwordEncoder) {
        return args -> {
            // Create test users
            User alice = new User();
            alice.setName("Alice Smith");
            alice.setPhoneNumber("1234567890");
            alice.setEmail("alice@example.com");
            alice.setPassword(passwordEncoder.encode("password1"));
            userRepository.save(alice);

            User bob = new User();
            bob.setName("Bob Johnson");
            bob.setPhoneNumber("2345678901");
            bob.setEmail("bob@example.com");
            bob.setPassword(passwordEncoder.encode("password2"));
            userRepository.save(bob);

            User carol = new User();
            carol.setName("Carol Williams");
            carol.setPhoneNumber("3456789012");
            carol.setEmail("carol@example.com");
            carol.setPassword(passwordEncoder.encode("password3"));
            userRepository.save(carol);

            // Create contacts for Alice
            Contact dave = new Contact();
            dave.setName("Dave Brown");
            dave.setPhoneNumber("4567890123");
            dave.setUser(alice);
            contactRepository.save(dave);

            Contact eve = new Contact();
            eve.setName("Eve Davis");
            eve.setPhoneNumber("5678901234");
            eve.setUser(alice);
            contactRepository.save(eve);

            // Create contact for Bob
            Contact frank = new Contact();
            frank.setName("Frank Miller");
            frank.setPhoneNumber("6789012345");
            frank.setUser(bob);
            contactRepository.save(frank);
        };
    }

}
