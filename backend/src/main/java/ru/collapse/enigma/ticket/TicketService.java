package ru.collapse.enigma.ticket;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    @Transactional(readOnly = true)
    public Page<TicketResponseDto> getAllTickets(Pageable pageable) {
        return ticketRepository
                .findAll(pageable)
                .map(ticketMapper::toDto);
    }

    @Transactional(readOnly = true)
    public TicketResponseDto getTicket(Long id) {
        return ticketRepository.findById(id)
                .map(ticketMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));
    }
}
