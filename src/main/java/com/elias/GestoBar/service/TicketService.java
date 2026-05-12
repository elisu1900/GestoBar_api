package com.elias.GestoBar.service;

import com.elias.GestoBar.exception.ResourceNotFoundException;
import com.elias.GestoBar.model.*;
import com.elias.GestoBar.repository.RestaurantTableRepository;
import com.elias.GestoBar.repository.TicketDetailRepository;
import com.elias.GestoBar.repository.TicketRepository;
import com.elias.GestoBar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketDetailRepository ticketDetailRepository;
    private final RestaurantTableRepository tableRepository;
    private final UserRepository userRepository;

    //CREATE

    @Transactional
    public Ticket createTicket(Integer tableId, Integer userId) {
        ticketRepository.findByTable_TableIdAndStatus(tableId, TicketStatus.OPEN)
                .ifPresent(t -> {
                    throw new IllegalStateException("Table already has an open ticket");
                });

        RestaurantTable table = tableRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + tableId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Ticket ticket = Ticket.builder()
                .table(table)
                .user(user)
                .status(TicketStatus.OPEN)
                .total(BigDecimal.ZERO)
                .createdAt(LocalDateTime.now())
                .build();

        return ticketRepository.save(ticket);
    }

    //READ

    public Ticket getTicketById(Integer ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + ticketId));
    }

    //CLOSE

    @Transactional
    public Ticket closeTicket(Integer ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + ticketId));

        if (TicketStatus.CLOSED.equals(ticket.getStatus())) {
            throw new IllegalStateException("Ticket is already closed");
        }

        ticket.setStatus(TicketStatus.CLOSED);
        ticket.setClosedAt(LocalDateTime.now());

        return ticketRepository.save(ticket);
    }


    public Optional<Ticket> findOpenTicketByTable(Integer tableId) {
        return ticketRepository.findByTable_TableIdAndStatus(tableId, TicketStatus.OPEN);
    }

    // RECALCULATE

    @Transactional
    public void recalculateTotal(Integer ticketId) {
        List<TicketDetail> details = ticketDetailRepository.findByTicket_TicketId(ticketId);

        BigDecimal total = details.stream()
                .map(d -> d.getUnitPrice().multiply(BigDecimal.valueOf(d.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + ticketId));

        ticket.setTotal(total);
        ticketRepository.save(ticket);
    }
}
