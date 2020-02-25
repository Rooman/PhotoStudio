package com.photostudio.web.filter;

import com.photostudio.entity.user.UserRole;

import java.util.EnumSet;
import java.util.Set;

public class GuestSecurityFilter extends AbstractSecurityFilter {
    private static final Set<UserRole> ACCEPTED_ROLES = EnumSet.of(UserRole.GUEST);

    @Override
    Set<UserRole> getAcceptedRoles() {
        return ACCEPTED_ROLES;
    }
}
