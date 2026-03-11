package io.roa.ticket_sys.DTO.Request;

import io.roa.ticket_sys.Model.Value.TicketStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TicketRequest(
        String passengerName,
        LocalDate travelDate,
        String sourceDestination,
        String destinationStation,
        BigDecimal price,
        Boolean paymentStatus,
        TicketStatus ticketStatus,
        Short seatNumber) {

}
