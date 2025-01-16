package ru.pstu.polls_system.file_service.web.security;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import ru.pstu.polls_system.file_service.common.model.SecurityUser;
import ru.pstu.polls_system.file_service.common.model.user.User;
import ru.pstu.polls_system.file_service.web.feign.AuthClient;

import java.util.Collections;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationProvider {

    private final AuthClient authClient;

    public UsernamePasswordAuthenticationToken authenticate(String token) {
        log.debug("Пользователь с токеном: {}, пытается авторизоваться", token);
        User user;
        try {
            user = authClient.authenticate(token);
        } catch (FeignException e) {
            throw new AccessDeniedException("Ошибка при попытке авторизации!", e);
        }
        var securityUser = new SecurityUser(user, Collections.emptyList());
        return new UsernamePasswordAuthenticationToken(securityUser, null, Collections.emptyList());
    }
}
