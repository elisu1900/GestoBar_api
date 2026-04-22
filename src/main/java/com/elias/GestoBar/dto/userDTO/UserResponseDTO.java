package com.elias.GestoBar.dto.userDTO;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class UserResponseDTO {
    private Integer id;
    private String name;
    private String lastName;
    private String role;
    private Boolean isActive;
}
