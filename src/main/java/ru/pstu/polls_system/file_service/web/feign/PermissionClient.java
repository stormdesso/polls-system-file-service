package ru.pstu.polls_system.file_service.web.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "permissionClient", url = "${feign.client.permissionClient}")
public interface PermissionClient {
    @PostMapping
    void hasPermission(@RequestBody List<Long> ids);
}
