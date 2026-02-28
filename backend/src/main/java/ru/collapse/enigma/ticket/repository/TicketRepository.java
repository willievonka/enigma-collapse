package ru.collapse.enigma.ticket.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.collapse.enigma.ticket.entity.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Page<Ticket> findAll(Pageable pageable);
}
