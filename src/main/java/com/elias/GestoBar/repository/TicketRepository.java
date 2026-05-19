package com.elias.GestoBar.repository;

import com.elias.GestoBar.model.Ticket;
import com.elias.GestoBar.model.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findByUser_name(String username);

    List<Ticket> findByUser_UserId(Integer userId);

    List<Ticket> findByTable_TableId(Integer tableId);

    Optional<Ticket> findByTicketId(Integer id);

    List<Ticket> findByTable_TableIdAndStatus(Integer tableId, TicketStatus status);

    List<Ticket> findByStatusAndArchivedFalseAndClosedAtBetween(TicketStatus status, LocalDateTime start, LocalDateTime end);
}
