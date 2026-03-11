package io.roa.ticket_sys.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.roa.ticket_sys.Model.Value.TicketStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

@Builder
@Getter
@Setter
public class Ticket {
    private static AtomicLong atomicLong = new AtomicLong(1);

    @Builder.Default
    @JsonProperty(index = 1)
    private Long ticketId = atomicLong.getAndIncrement();

    @JsonProperty(index = 2)
    private String passengerName;

    @JsonProperty(index = 3)
    private LocalDate travelDate;

    @JsonProperty(index = 4)
    private String sourceDestination;

    @JsonProperty(index = 5)
    private String destinationStation;

    @JsonProperty(index = 6)
    private BigDecimal price;

    @JsonProperty(index = 7)
    private Boolean paymentStatus;

    @JsonProperty(index = 8)
    private TicketStatus ticketStatus;

    @JsonProperty(index = 9)
    private String seatNumber;

}
