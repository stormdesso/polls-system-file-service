package ru.pstu.polls_system.file_service.web.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;

@Getter
@AllArgsConstructor
@ToString(of = "id")
public enum ActionConstants {
    READ("read", "Чтение"),
    WRITE("write", "Редактирование"),
    CREATE("create", "Создание"),
    DELETE("delete", "Удаление"),
    ;

    private final String id;
    private final String name;

    public static ActionConstants getById(String id) {
        return Arrays.stream(values()).filter(v -> v.id.equals(id)).findAny().orElse(null);
    }
}
