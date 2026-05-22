package com.elias.GestoBar.controller;

import com.elias.GestoBar.dto.ticketDTO.TicketRequestDTO;
import com.elias.GestoBar.dto.ticketDTO.TicketResponseDTO;
import com.elias.GestoBar.dto.ticketDetailDTO.TicketDetailRequestDTO;
import com.elias.GestoBar.dto.ticketDetailDTO.TicketDetailResponseDTO;
import com.elias.GestoBar.mapper.TicketDetailMapper;
import com.elias.GestoBar.mapper.TicketMapper;
import com.elias.GestoBar.model.Ticket;
import com.elias.GestoBar.model.TicketDetail;
import com.elias.GestoBar.model.User;
import com.elias.GestoBar.repository.UserRepository;
import com.elias.GestoBar.service.TicketDetailService;
import com.elias.GestoBar.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final TicketDetailService ticketDetailService;
    private final TicketMapper ticketMapper;
    private final TicketDetailMapper ticketDetailMapper;
    private final UserRepository userRepository;

    //POST /api/tickets
    @PostMapping
    public ResponseEntity<TicketResponseDTO> createTicket(
            @RequestBody @Valid TicketRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByName(userDetails.getUsername()).orElseThrow();
        Ticket ticket = ticketService.createTicket(request.getTableId(), user.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketMapper.toResponse(ticket));
    }

    //GET /api/tickets/{ticketId}
    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketResponseDTO> getTicket(@PathVariable Integer ticketId) {
        return ResponseEntity.ok(ticketMapper.toResponse(ticketService.getTicketById(ticketId)));
    }

    //PATCH /api/tickets/{ticketId}/close
    @PatchMapping("/{ticketId}/close")
    public ResponseEntity<TicketResponseDTO> closeTicket(@PathVariable Integer ticketId) {
        return ResponseEntity.ok(ticketMapper.toResponse(ticketService.closeTicket(ticketId)));
    }

    //PATCH /api/tickets/{ticketId}/cancel
    @PatchMapping("/{ticketId}/cancel")
    public ResponseEntity<TicketResponseDTO> cancelTicket(@PathVariable Integer ticketId) {
        return ResponseEntity.ok(ticketMapper.toResponse(ticketService.cancelTicket(ticketId)));
    }

    //POST /api/tickets/{ticketId}/details
    @PostMapping("/{ticketId}/details")
    public ResponseEntity<TicketDetailResponseDTO> addDetail(
            @PathVariable Integer ticketId,
            @RequestBody @Valid TicketDetailRequestDTO request) {
        TicketDetail detail = ticketDetailService.addDetail(ticketId, request.getProductId(), request.getQuantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketDetailMapper.toResponse(detail));
    }

    //PATCH /api/tickets/{ticketId}/details/{productId}
    @PatchMapping("/{ticketId}/details/{productId}")
    public ResponseEntity<TicketDetailResponseDTO> updateDetail(
            @PathVariable Integer ticketId,
            @PathVariable Integer productId,
            @RequestBody @Valid TicketDetailRequestDTO request) {
        TicketDetail detail = ticketDetailService.updateQuantity(ticketId, productId, request.getQuantity());
        if (detail == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ticketDetailMapper.toResponse(detail));
    }

    // GET /api/tickets/table/{tableId}/open
    @GetMapping("/table/{tableId}/open")
    public ResponseEntity<TicketResponseDTO> getOpenTicketByTable(@PathVariable Integer tableId) {
        return ticketService.findOpenTicketByTable(tableId)
                .map(ticket -> ResponseEntity.ok(ticketMapper.toResponse(ticket)))
                .orElse(ResponseEntity.notFound().build());
    }

    // PATCH /api/tickets/{ticketId}/move?targetTableId={id}
    @PatchMapping("/{ticketId}/move")
    public ResponseEntity<TicketResponseDTO> moveTicket(
            @PathVariable Integer ticketId,
            @RequestParam Integer targetTableId) {
        return ResponseEntity.ok(ticketMapper.toResponse(ticketService.moveTicket(ticketId, targetTableId)));
    }

    //DELETE /api/tickes/{ticketId}/details/{productId}
    @DeleteMapping("/{ticketId}/details/{productId}")
    public ResponseEntity<Void> deleteDetail(
            @PathVariable Integer ticketId,
            @PathVariable Integer productId) {
        ticketDetailService.deleteDetail(ticketId, productId);
        return ResponseEntity.noContent().build();
    }
}
