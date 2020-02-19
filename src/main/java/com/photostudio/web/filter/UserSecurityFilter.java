package com.photostudio.web.filter;

import com.photostudio.entity.user.UserRole;

import java.util.EnumSet;
import java.util.Set;

public class UserSecurityFilter extends AbstractSecurityFilter {
    private static final Set<UserRole> acceptedRoles = EnumSet.of(UserRole.USER);

    @Override
    Set<UserRole> getAcceptedRoles() {
        return acceptedRoles;
    }
}
