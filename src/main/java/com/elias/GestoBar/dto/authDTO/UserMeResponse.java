package com.elias.GestoBar.dto.authDTO;


public record UserMeResponse(
        Integer userId,
        String name,
        String lastName,
        String role,
        Boolean isActive
) {}
