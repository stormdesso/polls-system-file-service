package ru.pstu.polls_system.file_service.web.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import ru.pstu.polls_system.file_service.web.utils.UserDetailsUtil;

import java.util.Optional;

@Component
public class FeignClientInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        Optional<String> token = UserDetailsUtil.getTokenFromContext();
        token.ifPresent(value -> requestTemplate.header(HttpHeaders.AUTHORIZATION, value));
    }
}
