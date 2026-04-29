package com.elias.GestoBar.repository;

import com.elias.GestoBar.model.TicketDetail;
import com.elias.GestoBar.model.TicketDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketDetailRepository extends JpaRepository<TicketDetail, TicketDetailId> {

    List<TicketDetail> findByTicket_TicketId(Integer ticketId);

    List<TicketDetail> findByProduct_ProductId(Integer productId);

    Optional<TicketDetail> findByTicket_TicketIdAndProduct_ProductId(Integer ticketId, Integer productId);

    void deleteByTicket_TicketIdAndProduct_ProductId(Integer ticketId, Integer productId);
}
