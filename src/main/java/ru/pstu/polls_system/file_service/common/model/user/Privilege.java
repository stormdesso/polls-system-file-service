package ru.pstu.polls_system.file_service.common.model.user;

import lombok.Data;

@Data
public class Privilege {

    Long id;

    String systemObjectName;

    String actionName;
}
