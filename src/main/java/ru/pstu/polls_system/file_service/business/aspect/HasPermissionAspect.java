package ru.pstu.polls_system.file_service.business.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.pstu.polls_system.file_service.web.utils.UserDetailsUtil;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class HasPermissionAspect {
    private final PermissionEvaluator permissionEvaluator;

    @Around("@annotation(hasPermission)")
    public Object aroundHasPermission(ProceedingJoinPoint joinPoint,HasPermission hasPermission) throws Throwable {
        log.debug("Проверка прав пользователя: {}", UserDetailsUtil.getAuthenticatedUser().getLogin());
        if (!hasPermission(hasPermission)) {
            throw new AccessDeniedException(String.format("Недостаточно прав для выполнения операции '%s' над объектом системы '%s'",
                    hasPermission.action().getName(), hasPermission.resource().getName()));
        }

        return joinPoint.proceed();
    }

    private boolean hasPermission(HasPermission hasPermission) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return permissionEvaluator.hasPermission(authentication, hasPermission.resource().getId(), hasPermission.action().getId());
    }

}
