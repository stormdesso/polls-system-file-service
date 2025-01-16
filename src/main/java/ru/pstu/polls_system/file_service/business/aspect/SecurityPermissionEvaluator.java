package ru.pstu.polls_system.file_service.business.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.pstu.polls_system.file_service.common.model.SecurityUser;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@RequiredArgsConstructor
public class SecurityPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication,Object resource,Object action) {
        var securityUser = (SecurityUser) authentication.getPrincipal();
        log.trace("Проверка привилегий пользователя {} на доступ к ресурсу {} на операцию {}",
                securityUser.getUsername(), resource, action);
        boolean result = hasPermission(resource, action);
        log.trace("Результат проверки  доступа пользователя {} на доступ к ресурсу {} на операцию {} - {}",
                securityUser.getUsername(), resource, action, result ? "Доступ разрешен" : "Доступ запрещен");
        return result;
    }

    @Override
    public boolean hasPermission(Authentication authentication,Serializable targetId,String targetType,Object permission) {
        return false;
    }
    public boolean hasPermission(Object resource, Object action) {

        AtomicReference<Boolean> result = new AtomicReference<>(false);
        var roles = ((SecurityUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).getUserData().getRole();

        roles.forEach( role -> role.getPrivilege().forEach(privilege -> {
                if( privilege.getSystemObjectName().equals((String)resource) && privilege.getActionName().equals((String)action) )
                    result.set(true);
            }));
        return result.get();
    }
}
