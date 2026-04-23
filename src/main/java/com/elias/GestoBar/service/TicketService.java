package com.elias.GestoBar.service;

import com.elias.GestoBar.model.*;
import com.elias.GestoBar.repository.RestaurantTableRepository;
import com.elias.GestoBar.repository.TicketDetailRepository;
import com.elias.GestoBar.repository.TicketRepository;
import com.elias.GestoBar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketDetailRepository ticketDetailRepository;
    private final RestaurantTableRepository tableRepository;
    private final UserRepository userRepository;


    public Ticket createTicket(Integer tableId, Integer userId) {
        ticketRepository.findByTable_TableIdAndStatus(tableId, TicketStatus.OPEN)
                .ifPresent(t -> { throw new RuntimeException("Table already has an open ticket"); });

        RestaurantTable table = tableRepository.findById(tableId)
                .orElseThrow(() -> new RuntimeException("Table not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Ticket ticket = Ticket.builder()
                .table(table)
                .user(user)
                .status(TicketStatus.OPEN)
                .total(BigDecimal.ZERO)
                .createdAt(LocalDateTime.now())
                .build();

        return ticketRepository.save(ticket);
    }

    public Ticket getTicketById(Integer ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    public Ticket closeTicket(Integer ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if ("closed".equals(ticket.getStatus())) {
            throw new RuntimeException("Ticket is already closed");
        }

        ticket.setStatus(TicketStatus.CLOSED);
        ticket.setClosedAt(LocalDateTime.now());

        return ticketRepository.save(ticket);
    }

    public void recalculateTotal(Integer ticketId) {
        List<TicketDetail> details = ticketDetailRepository.findByTicket_TicketId(ticketId);

        BigDecimal total = details.stream()
                .map(d -> d.getUnitPrice().multiply(BigDecimal.valueOf(d.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        ticket.setTotal(total);
        ticketRepository.save(ticket);
    }
}
