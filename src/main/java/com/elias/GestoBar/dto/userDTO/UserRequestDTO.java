package com.elias.GestoBar.dto.userDTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class UserRequestDTO {
    private String name;
    private String lastName;
    private String password;
    private String role;
    private Boolean isActive;
}
