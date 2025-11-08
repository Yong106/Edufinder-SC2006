package com.sc2006.g5.edufinder.unit.setup;

import com.sc2006.g5.edufinder.model.user.CustomUserDetails;
import com.sc2006.g5.edufinder.model.user.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        User user = User.builder()
            .id(annotation.id())
            .username(annotation.username())
            .role(annotation.role())
            .build();

        CustomUserDetails principal = new CustomUserDetails(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(
            principal,
            principal.getPassword(),
            principal.getAuthorities()
        );

        context.setAuthentication(auth);
        return context;
    }
}

