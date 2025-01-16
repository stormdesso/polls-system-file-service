package ru.pstu.polls_system.file_service.web.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;

@Getter
@AllArgsConstructor
@ToString(of = "id")
public enum SystemObjectConstants {
    POLL("poll", "Опросы"),
    USER_ADMINISTRATION("user_administration", "Администрирование пользователей"),
    RELOCATION("relocation", "Переезд"),
    ;

    private final String id;
    private final String name;
    public static SystemObjectConstants getById(String id) {
        return Arrays.stream(values()).filter(v -> v.id.equals(id)).findAny().orElse(null);
    }
}
