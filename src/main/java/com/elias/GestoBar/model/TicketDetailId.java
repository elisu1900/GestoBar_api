package com.elias.GestoBar.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TicketDetailId implements Serializable {

    @Column(name = "ticket_id", nullable = false)
    private Integer ticketId;

    @Column(name = "product_id", nullable = false)
    private Integer productId;
}
