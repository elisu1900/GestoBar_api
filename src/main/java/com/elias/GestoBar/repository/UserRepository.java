package com.elias.GestoBar.repository;

import com.elias.GestoBar.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByName(String username);
    boolean existsByName(String username);
    List<User> findByIsActiveTrue();


}
