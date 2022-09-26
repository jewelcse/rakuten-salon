package com.bjit.salon.auth.service.controller;


import com.bjit.salon.auth.service.dto.request.UserLoginDto;
import com.bjit.salon.auth.service.dto.response.LoginResponseDto;
import com.bjit.salon.auth.service.security.jwt.JwtUtil;
import com.bjit.salon.auth.service.service.UserService;
import com.bjit.salon.auth.service.serviceImpl.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.bjit.salon.auth.service.util.ConstraintsUtil.APPLICATION_BASE_URL;
@AllArgsConstructor
@RestController
@RequestMapping(APPLICATION_BASE_URL)
public class UserAuthenticationController {


    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody UserLoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return ResponseEntity.ok( LoginResponseDto.builder()
                        .token(jwt)
                        .username(userDetails.getUsername())
                        .email(userDetails.getEmail())
                        .roles(roles)
                .build());
    }
}
