package com.rafaelgude.customersfavproducts.security;

import com.rafaelgude.customersfavproducts.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/refresh_token")
    public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
        var usuario = userService.getJWTUser();
        response.addHeader("Authorization", "Bearer " + jwtUtil.generateToken(usuario.getUsername()));

        return ResponseEntity.noContent().build();
    }

}
