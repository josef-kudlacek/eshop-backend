package cz.jkdabing.backend.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.jkdabing.backend.constants.ResponseConstants;
import cz.jkdabing.backend.dto.response.MessageResponse;
import cz.jkdabing.backend.service.MessageService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component("customAuthenticationEntryPoint")
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final MessageService messageService;

    public CustomAuthenticationEntryPoint(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setCharacterEncoding(ResponseConstants.CHARACTER_ENCODING_UTF_8);
        response.setContentType(ResponseConstants.CONTENT_TYPE_APPLICATION_JSON);

        MessageResponse messageResponse = MessageResponse.builder()
                .message(messageService.getMessage(ResponseConstants.ERROR_MESSAGE_ACCESS_DENIED))
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(messageResponse);
        response.getWriter().println(jsonResponse);
    }
}
