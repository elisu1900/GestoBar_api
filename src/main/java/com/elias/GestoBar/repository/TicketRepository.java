package com.elias.GestoBar.repository;

import com.elias.GestoBar.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findByUser_Username(String username);

    List<Ticket> findByUserId(Long userId);

    List<Ticket> findByTableId(String tableId);

    Optional<Ticket> findById(Long id);

}
