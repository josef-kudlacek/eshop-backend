package cz.jkdabing.backend.controller;

import cz.jkdabing.backend.dto.response.MessageResponse;
import cz.jkdabing.backend.entity.UserEntity;
import cz.jkdabing.backend.service.MessageService;
import cz.jkdabing.backend.service.SecurityService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController extends AbstractBaseController {

    private final SecurityService securityService;

    public HelloController(MessageService messageService, SecurityService securityService) {
        super(messageService);
        this.securityService = securityService;
    }

    @GetMapping("/")
    public MessageResponse hello() {
        return new MessageResponse(getLocalizedMessage("greeting.message"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public MessageResponse admin() {
        UserEntity currentUser = securityService.getCurrentUser();

        return new MessageResponse(getLocalizedMessage("admin.greeting.message", currentUser.getUsername()));
    }
}
