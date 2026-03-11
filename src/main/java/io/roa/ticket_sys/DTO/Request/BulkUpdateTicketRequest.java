package io.roa.ticket_sys.DTO.Request;

import java.util.List;

public record BulkUpdateTicketRequest(
        List<Long> ticketIds,
        Boolean paymentStatus
) {

}
