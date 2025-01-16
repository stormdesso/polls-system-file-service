package ru.pstu.polls_system.file_service.common.model.user;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class User {

    Long id;

    String fullName;

    Date birthdate;

    String login;

    String password;

    String phoneNumber;

    String email;

    boolean isBlocked;

    Long ownershipId;

    List<Role> role;
}
