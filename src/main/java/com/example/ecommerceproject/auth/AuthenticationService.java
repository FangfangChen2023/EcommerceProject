package com.example.ecommerceproject.auth;

import com.example.ecommerceproject.config.JwtService;
import com.example.ecommerceproject.domain.Role;
import com.example.ecommerceproject.domain.User;
import com.example.ecommerceproject.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public AuthenticationResponse register(RegisterRequest request){

        var user = User.builder()
                .username(request.getUsername())
                .role(Role.CUSTOMER)
                .password(passwordEncoder.encode(request.getPassword())) // Encode the password before putting it into database
                .build();
        userRepository.save(user);  // Save the new user to the database
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();    // Send the new token
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        // AuthenticationManager itself contains methods for authentication
        // If the authentication fails, throw an exception
        authManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));
        // If  the authentication succeed, return a new token
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
