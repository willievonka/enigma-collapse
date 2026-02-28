package ru.collapse.enigma.ticket;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private  final TicketService ticketService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<TicketResponseDto> getTickets(
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "10") int size
    ) {
        return ticketService.getAllTickets(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TicketResponseDto getTicket(@PathVariable Long id) {
        return ticketService.getTicket(id);
    }

    @PostMapping("/{id}/reply")
    @ResponseStatus(HttpStatus.OK)
    public TicketResponseDto replyTicket(@PathVariable Long id) {
        return null;
    }
}
