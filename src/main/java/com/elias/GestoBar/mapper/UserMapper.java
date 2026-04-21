package com.elias.GestoBar.mapper;

import com.elias.GestoBar.dto.userDTO.UserRequestDTO;
import com.elias.GestoBar.dto.userDTO.UserResponseDTO;
import com.elias.GestoBar.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequestDTO request);
    UserResponseDTO toResponse(User user);

}
