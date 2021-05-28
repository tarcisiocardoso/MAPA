package com.example.securingweb.config;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
    	System.out.println(">>>>>CustomAuthenticationProvider<<<<<<s");
        String name = authentication.getName();
        // You can get the password here
        String password = authentication.getCredentials().toString();

        // Your custom authentication logic here
        if (name.equals("admin") && password.equals("pwd")) {
            Authentication auth = new UsernamePasswordAuthenticationToken(name,
                    password);

            return auth;
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
