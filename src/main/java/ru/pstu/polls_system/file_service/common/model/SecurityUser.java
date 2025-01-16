package ru.pstu.polls_system.file_service.common.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class SecurityUser extends User {

    private final ru.pstu.polls_system.file_service.common.model.user.User userData;

    public SecurityUser(ru.pstu.polls_system.file_service.common.model.user.User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getLogin(), user.getPassword(), true, true, true, !user.isBlocked(), authorities);
        this.userData = user;
    }

}
