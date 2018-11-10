package com.lukhol.test.config;

import com.lukhol.dna.exercise.model.User;
import com.lukhol.dna.exercise.security.UserPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser withMockCustomUser) {
        var context = SecurityContextHolder.createEmptyContext();
        var user = User.builder().id(1L).login("login").password("password").build();
        var userPrincipal = new UserPrincipal(user);
        var token = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
        context.setAuthentication(token);
        return context;
    }
}
