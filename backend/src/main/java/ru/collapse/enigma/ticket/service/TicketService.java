package ru.collapse.enigma.ticket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.collapse.enigma.mail.send.MailClient;
import ru.collapse.enigma.ticket.mapper.TicketMapper;
import ru.collapse.enigma.ticket.dto.PageResponseDto;
import ru.collapse.enigma.ticket.dto.TicketResponseDto;
import ru.collapse.enigma.ticket.entity.Ticket;
import ru.collapse.enigma.ticket.repository.TicketRepository;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final MailClient mailClient;

    @Transactional(readOnly = true)
    public PageResponseDto<TicketResponseDto> getAllTickets(Pageable pageable) {
        Page<TicketResponseDto> tickets =  ticketRepository
                .findAll(pageable)
                .map(ticketMapper::toDto);

        return new PageResponseDto<>(
                tickets.getContent(),
                tickets.getNumber(),
                tickets.getSize(),
                tickets.getTotalElements(),
                tickets.getTotalPages());
    }

    @Transactional(readOnly = true)
    public TicketResponseDto getTicket(Long id) {
        Ticket ticket = getById(id);

        return ticketMapper.toDto(ticket);
    }

    @Transactional(readOnly = true)
    public void replyTicket(Long id, String message) {
        Ticket ticket = getById(id);

        mailClient.reply(
                ticket.getEmail(),
                ticket.getSubject(),
                message,
                ticket.getMailId()
        );
    }

    private Ticket getById(Long id) {
        return ticketRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));
    }
}
