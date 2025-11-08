package com.sc2006.g5.edufinder.unit.setup;

import com.sc2006.g5.edufinder.model.user.Role;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
    long id();
    String username() default "test_user";
    Role role() default Role.USER;
}