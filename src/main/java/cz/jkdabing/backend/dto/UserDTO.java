package cz.jkdabing.backend.dto;

import cz.jkdabing.backend.entity.AddressEntity;
import cz.jkdabing.backend.enums.UserRole;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDTO {

    private Long userId;

    private UserRole userRole;

    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private List<AddressEntity> addressEntities = new ArrayList<>();

    private String userName;

    private String password;

    private boolean isActive;
}
