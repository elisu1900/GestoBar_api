package com.elias.GestoBar.dto.userDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    @NotBlank
    @Size(max = 100)
    private String name;

    @Size(max = 100)
    private String lastName;

    @NotBlank
    @Size(min = 6, max = 255)
    private String password;

    @NotBlank
    private String role;

    @NotNull
    private Boolean isActive;
}
