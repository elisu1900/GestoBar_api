package com.elias.GestoBar.service;

import com.elias.GestoBar.model.Product;
import com.elias.GestoBar.model.TicketDetail;
import com.elias.GestoBar.model.TicketDetailId;
import com.elias.GestoBar.repository.ProductRepository;
import com.elias.GestoBar.repository.TicketDetailRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketDetailService {

    private final TicketDetailRepository ticketDetailRepository;
    private final ProductRepository productRepository;
    private final TicketService ticketService;

    public TicketDetail addDetail(Integer ticketId, Integer productId, int quantity) {
        Optional<TicketDetail> existing =
                ticketDetailRepository.findByTicket_TicketIdAndProduct_ProductId(ticketId, productId);

        TicketDetail detail;

        if (existing.isPresent()) {
            detail = existing.get();
            detail.setQuantity(detail.getQuantity() + quantity);
        } else {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            detail = new TicketDetail();
            TicketDetailId id = new TicketDetailId();
            id.setTicketId(ticketId);
            id.setProductId(productId);
            detail.setId(id);
            detail.setQuantity(quantity);
            detail.setUnitPrice(product.getPrice()); // precio fijo en el momento
        }

        TicketDetail saved = ticketDetailRepository.save(detail);
        ticketService.recalculateTotal(ticketId);
        return saved;
    }

    public TicketDetail updateQuantity(Integer ticketId, Integer productId, int quantity) {
        TicketDetail detail = ticketDetailRepository
                .findByTicket_TicketIdAndProduct_ProductId(ticketId, productId)
                .orElseThrow(() -> new RuntimeException("Detail not found"));

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
                .orElseThrow(() -> new RuntimeException("Detail not found"));

        ticketDetailRepository.deleteByTicket_TicketIdAndProduct_ProductId(ticketId, productId);
        ticketService.recalculateTotal(ticketId);
    }
}