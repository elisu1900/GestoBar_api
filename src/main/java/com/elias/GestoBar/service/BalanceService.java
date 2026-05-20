package com.elias.GestoBar.service;

import com.elias.GestoBar.dto.balanceDTO.DailyBalanceResponseDTO;
import com.elias.GestoBar.dto.balanceDTO.ProductBalanceDTO;
import com.elias.GestoBar.model.Ticket;
import com.elias.GestoBar.model.TicketDetail;
import com.elias.GestoBar.model.TicketStatus;
import com.elias.GestoBar.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final TicketRepository ticketRepository;

    public DailyBalanceResponseDTO getDailyBalance() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);

        List<Ticket> tickets = ticketRepository
                .findByStatusAndArchivedFalseAndClosedAtBetween(
                        TicketStatus.CLOSED, start, end
                );

        Map<String, ProductBalanceDTO> breakdown = new LinkedHashMap<>();

        for (Ticket ticket : tickets) {
            for (TicketDetail detail : ticket.getDetails()) {
                String productName = detail.getProduct().getName();
                BigDecimal revenue = detail.getUnitPrice()
                        .multiply(BigDecimal.valueOf(detail.getQuantity()));
                BigDecimal cost = detail.getProduct().getCostPrice()
                        .multiply(BigDecimal.valueOf(detail.getQuantity()));
                BigDecimal profit = revenue.subtract(cost);

                breakdown.merge(productName,
                        ProductBalanceDTO.builder()
                                .productName(productName)
                                .qtySold(detail.getQuantity())
                                .revenue(revenue)
                                .cost(cost)
                                .profit(profit)
                                .build(),
                        (existing, newEntry) -> ProductBalanceDTO.builder()
                                .productName(productName)
                                .qtySold(existing.getQtySold() + newEntry.getQtySold())
                                .revenue(existing.getRevenue().add(newEntry.getRevenue()))
                                .cost(existing.getCost().add(newEntry.getCost()))
                                .profit(existing.getProfit().add(newEntry.getProfit()))
                                .build()
                );
            }
        }

        BigDecimal totalRevenue = breakdown.values().stream()
                .map(ProductBalanceDTO::getRevenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCosts = breakdown.values().stream()
                .map(ProductBalanceDTO::getCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return DailyBalanceResponseDTO.builder()
                .totalRevenue(totalRevenue)
                .totalCosts(totalCosts)
                .realProfit(totalRevenue.subtract(totalCosts))
                .breakdown(new ArrayList<>(breakdown.values()))
                .build();
    }

    @Transactional
    public void resetDay() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);

        List<Ticket> tickets = ticketRepository
                .findByStatusAndArchivedFalseAndClosedAtBetween(
                        TicketStatus.CLOSED, start, end
                );

        tickets.forEach(t -> t.setArchived(true));
        ticketRepository.saveAll(tickets);
    }
}