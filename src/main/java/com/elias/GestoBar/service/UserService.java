package com.elias.GestoBar.service;

import com.elias.GestoBar.dto.userDTO.UserRequestDTO;
import com.elias.GestoBar.dto.userDTO.UserResponseDTO;
import com.elias.GestoBar.exception.ResourceNotFoundException;
import com.elias.GestoBar.mapper.UserMapper;
import com.elias.GestoBar.model.UserRole;
import com.elias.GestoBar.model.User;
import com.elias.GestoBar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;


    @Transactional
    public UserResponseDTO createUser(UserRequestDTO dto) {
        if (userRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Username already exists: " + dto.getName());
        }

        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(parseRole(dto.getRole()));

        return userMapper.toResponse(userRepository.save(user));
    }


    public UserResponseDTO getUserById(Integer userId) {
        return userMapper.toResponse(findUserOrThrow(userId));
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }


    @Transactional
    public UserResponseDTO updateUser(Integer userId, UserRequestDTO dto) {
        User existing = findUserOrThrow(userId);

        if (!existing.getName().equals(dto.getName())
                && userRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Username already exists: " + dto.getName());
        }

        existing.setName(dto.getName());
        existing.setLastName(dto.getLastName());
        existing.setRole(parseRole(dto.getRole()));
        existing.setIsActive(dto.getIsActive());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return userMapper.toResponse(userRepository.save(existing));
    }


    @Transactional
    public void deleteUser(Integer userId) {
        User user = findUserOrThrow(userId);
        userRepository.delete(user);
    }


    private User findUserOrThrow(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
    }

    private UserRole parseRole(String role) {
        try {
            return UserRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
    }
}
