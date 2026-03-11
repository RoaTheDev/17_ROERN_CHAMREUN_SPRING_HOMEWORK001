package io.roa.ticket_sys.Controller;

import io.roa.ticket_sys.DTO.Request.BulkUpdateTicketRequest;
import io.roa.ticket_sys.DTO.Request.TicketRequest;
import io.roa.ticket_sys.DTO.Response.ApiResponse;
import io.roa.ticket_sys.Model.Ticket;
import io.roa.ticket_sys.Model.Value.TicketStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("api/v1/tickets")
public class TicketController {

    private final List<Ticket> TICKETS;

    public TicketController() {
        TICKETS = new ArrayList<>();
        TICKETS.add(
                Ticket.builder()
                        .passengerName("Roa")
                        .seatNumber("RR05")
                        .paymentStatus(true)
                        .sourceDestination("PP")
                        .destinationStation("Bay Area")
                        .travelDate(LocalDate.of(2027, 5, 15))
                        .ticketStatus(TicketStatus.BOOKED)
                        .price(new BigDecimal("1500"))
                        .build());
        TICKETS.add(
                Ticket.builder()
                        .passengerName("Rem")
                        .seatNumber("RE15")
                        .paymentStatus(true)
                        .sourceDestination("Berlin")
                        .destinationStation("Bay Area")
                        .travelDate(LocalDate.of(2030, 2, 5))
                        .ticketStatus(TicketStatus.BOOKED)
                        .price(new BigDecimal("800"))
                        .build());
        TICKETS.add(
                Ticket.builder()
                        .passengerName("Alice")
                        .seatNumber("AL02")
                        .paymentStatus(true)
                        .sourceDestination("England")
                        .destinationStation("Bay Area")
                        .travelDate(LocalDate.of(2029, 10, 19))
                        .ticketStatus(TicketStatus.BOOKED)
                        .price(new BigDecimal("1200"))
                        .build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Ticket>> createTicket(@RequestBody TicketRequest req) {
        Ticket ticket = map(req);
        TICKETS.add(ticket);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(ticket, "Ticket created successfully", HttpStatus.CREATED.toString()));
    }

    @GetMapping("{ticket-id}")
    public ResponseEntity<ApiResponse<Ticket>> getTicketById(@PathVariable("ticket-id") Long tId) {
        Optional<Ticket> ticket = TICKETS.stream()
                .filter(t -> t.getTicketId().equals(tId))
                .findFirst();

        return ticket.isPresent() ?
                ResponseEntity.ok(
                        ApiResponse.success(ticket.get(), "Ticket fetched successfully", HttpStatus.OK.toString()))
                : ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.fail("No tickets found with the given ID.", HttpStatus.NOT_FOUND.toString()));
    }

    @GetMapping("filter")
    public ResponseEntity<ApiResponse<List<Ticket>>> filterTicket(@RequestParam TicketStatus status, @RequestParam LocalDate date) {
        var res = TICKETS.stream()
                .filter(ticket -> ticket.getTicketStatus().equals(status) && ticket.getTravelDate().equals(date))
                .toList();
        return ResponseEntity.ok(ApiResponse.success(res
                , !res.isEmpty() ? "Ticket fetched successfully" : "No tickets found with given filters", HttpStatus.OK.toString()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Ticket>>> getPaginatedTicket(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {

        page = page != null && page > 0 ? page : 1;
        size = size != null && size > 0 ? size : 10;

        return ResponseEntity.ok(
                ApiResponse.success(TICKETS.stream()
                                .skip((long) (page - 1) * size)
                                .limit(size)
                                .toList(),
                        "Ticket retrieved successfully",
                        HttpStatus.OK.toString()));
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<Ticket>> updateTicketById(@PathVariable Long id, @RequestBody TicketRequest req) {

        for (int i = 0; i < TICKETS.size(); i++) {
            if (TICKETS.get(i).getTicketId().equals(id)) {
                Ticket updatedTicket = map(req);
                TICKETS.set(i, updatedTicket);
                return ResponseEntity.ok(
                        ApiResponse.success(TICKETS.get(i), "Ticket updated successfully", HttpStatus.OK.toString()));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.fail("No tickets found with the given ID.", HttpStatus.NOT_FOUND.toString()));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Ticket>> deleteTicketById(@PathVariable Long id) {
        for (int i = 0; i < TICKETS.size(); i++) {
            if (TICKETS.get(i).getTicketId().equals(id)) {
                TICKETS.remove(i);
                return ResponseEntity.ok(
                        ApiResponse.success(TICKETS.get(i), "Ticket updated successfully", HttpStatus.OK.toString()));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.fail("Ticket not found", HttpStatus.NOT_FOUND.toString()));
    }

    @GetMapping("search")
    public ResponseEntity<ApiResponse<List<Ticket>>> searchByName(@RequestParam String passengerName) {

        var res = TICKETS.stream()
                .filter(t -> t.getPassengerName().equals(passengerName))
                .toList();
        return ResponseEntity.ok(ApiResponse.success(
                res,
                !res.isEmpty() ? "Ticket fetched successfully" : "No tickets found with given filters",
                HttpStatus.OK.toString()
        ));
    }

    @PostMapping("bulk")
    public ResponseEntity<ApiResponse<List<Ticket>>> bulkCreate(@RequestBody List<TicketRequest> req) {

        List<Ticket> tickets = new ArrayList<>();

        for (TicketRequest ticketReq : req) {
            tickets.add(map(ticketReq));
        }

        TICKETS.addAll(tickets);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(tickets, "Ticket created successfully", HttpStatus.CREATED.toString()));
    }

    @PutMapping("bulk")
    public ResponseEntity<ApiResponse<List<Ticket>>> bulkUpdateStatus(@RequestBody BulkUpdateTicketRequest req) {
        List<Ticket> resTicket = new ArrayList<>();

        for (int i = 0; i < TICKETS.size(); i++) {
            for (Long id : req.ticketIds()) {
                if (TICKETS.get(i).getTicketId().equals(id)) {
                    Ticket ticket = TICKETS.get(i);
                    ticket.setPaymentStatus(req.paymentStatus());
                    resTicket.add(ticket);
                    TICKETS.set(i, ticket);
                }
            }
        }

        return !resTicket.isEmpty() ? ResponseEntity.ok(ApiResponse
                .success(resTicket, "Ticket updated successfully", HttpStatus.OK.toString()))
                : ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.fail("No tickets were updated", HttpStatus.NOT_FOUND.toString()));

    }


    private Ticket map(TicketRequest req) {
        return Ticket.builder()
                .passengerName(req.passengerName())
                .ticketStatus(req.ticketStatus())
                .paymentStatus(req.paymentStatus())
                .sourceDestination(req.sourceDestination())
                .destinationStation(req.destinationStation())
                .travelDate(req.travelDate())
                .price(req.price())
                .build();
    }

}

