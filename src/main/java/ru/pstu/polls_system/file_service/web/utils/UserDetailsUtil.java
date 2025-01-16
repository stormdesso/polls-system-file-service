package ru.pstu.polls_system.file_service.web.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.pstu.polls_system.file_service.common.model.SecurityUser;
import ru.pstu.polls_system.file_service.common.model.user.User;

import java.util.Optional;

public class UserDetailsUtil {
    public static Optional<String> getTokenFromContext() {
        var authentication = getAuthentication();
        return authentication.map(value -> value.getDetails().toString());
    }

    public static User getAuthenticatedUser() {
        return ((SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserData();
    }

    public static Optional<Authentication> getAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
    }
}
