package ru.collapse.enigma.ticket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.collapse.enigma.ticket.service.TicketService;
import ru.collapse.enigma.ticket.dto.PageResponseDto;
import ru.collapse.enigma.ticket.dto.TicketReplyRequestDto;
import ru.collapse.enigma.ticket.dto.TicketResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private  final TicketService ticketService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponseDto<TicketResponseDto> getTickets(
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void replyTicket(
            @PathVariable Long id,
            @RequestBody TicketReplyRequestDto request) {
        ticketService.replyTicket(id, request.message());
    }
}
