package com.elias.GestoBar.dto.userDTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Integer userId;
    private String name;
    private String lastName;
    private String role;
    private Boolean isActive;
}
