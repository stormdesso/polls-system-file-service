package ru.pstu.polls_system.file_service.common.model.user;

import lombok.Data;

import java.util.List;

@Data
public class Role {

    Long id;

    String roleName;

    List<Privilege> privilege;
}
