package ru.pstu.polls_system.file_service.web.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.pstu.polls_system.file_service.common.model.user.User;

@FeignClient(name = "authClient", url = "${feign.client.authClient}")
public interface AuthClient {
    @GetMapping(path = "/authenticate/{token}")
    User authenticate(@PathVariable String token);
}
