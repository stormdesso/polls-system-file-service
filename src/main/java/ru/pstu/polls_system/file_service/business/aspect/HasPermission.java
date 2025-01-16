package ru.pstu.polls_system.file_service.business.aspect;

import ru.pstu.polls_system.file_service.web.constant.ActionConstants;
import ru.pstu.polls_system.file_service.web.constant.SystemObjectConstants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface HasPermission {
    SystemObjectConstants resource();
    ActionConstants action();
}
