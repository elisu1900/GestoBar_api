package com.elias.GestoBar.service;

import com.elias.GestoBar.exception.ResourceNotFoundException;
import com.elias.GestoBar.model.Product;
import com.elias.GestoBar.model.Ticket;
import com.elias.GestoBar.model.TicketDetail;
import com.elias.GestoBar.model.TicketDetailId;
import com.elias.GestoBar.repository.ProductRepository;
import com.elias.GestoBar.repository.TicketDetailRepository;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.table.DefaultTableCellRenderer;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketDetailService {

    private final TicketDetailRepository ticketDetailRepository;
    private final ProductRepository productRepository;
    private final TicketService ticketService;

    public TicketDetail addDetail(Integer ticketId, Integer productId, @NotNull @Min(1) Integer quantity) {
        Optional<TicketDetail> existing =
                ticketDetailRepository.findByTicket_TicketIdAndProduct_ProductId(ticketId, productId);

        TicketDetail detail;

        if (existing.isPresent()) {
            detail = existing.get();
            detail.setQuantity(detail.getQuantity() + 1);
        } else {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("product not found with id: " + productId));

            Ticket ticket = ticketService.getTicketById(ticketId);

            TicketDetailId id = new TicketDetailId();
            id.setTicketId(ticketId);
            id.setProductId(productId);

            detail = new TicketDetail();
            detail.setId(id);
            detail.setTicket(ticket);
            detail.setProduct(product);
            detail.setQuantity(1);
            detail.setUnitPrice(product.getSellPrice());
        }

        TicketDetail saved = ticketDetailRepository.save(detail);
        ticketService.recalculateTotal(ticketId);
        return saved;
    }

    public TicketDetail updateQuantity(Integer ticketId, Integer productId, int quantity) {
        TicketDetail detail = ticketDetailRepository
                .findByTicket_TicketIdAndProduct_ProductId(ticketId, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Detail not found with id: " + ticketId));

        if (quantity <= 0) {
            ticketDetailRepository.deleteByTicket_TicketIdAndProduct_ProductId(ticketId, productId);
            ticketService.recalculateTotal(ticketId);
            return null;
        }

        detail.setQuantity(quantity);
        TicketDetail saved = ticketDetailRepository.save(detail);
        ticketService.recalculateTotal(ticketId);
        return saved;
    }

    @Transactional
    public void deleteDetail(Integer ticketId, Integer productId) {
        ticketDetailRepository.findByTicket_TicketIdAndProduct_ProductId(ticketId, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Detail not found with id: " + ticketId));

        ticketDetailRepository.deleteByTicket_TicketIdAndProduct_ProductId(ticketId, productId);
        ticketService.recalculateTotal(ticketId);
    }
}