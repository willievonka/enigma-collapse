package ru.collapse.enigma.ticket.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.collapse.enigma.ticket.entity.Ticket;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Page<Ticket> findAll(Pageable pageable);

    @Query(value = "SELECT sentiment, COUNT(*) FROM tickets WHERE sentiment IS NOT NULL GROUP BY sentiment", nativeQuery = true)
    List<Object[]> countBySentiment();

    @Query(value = "SELECT category, COUNT(*) FROM tickets WHERE category IS NOT NULL GROUP BY category", nativeQuery = true)
    List<Object[]> countByCategory();

    @Query(value = """
            SELECT sn, COUNT(*) AS cnt
            FROM tickets, jsonb_array_elements_text(serial_numbers) AS sn
            GROUP BY sn
            ORDER BY cnt DESC
            LIMIT 10
            """, nativeQuery = true)
    List<Object[]> findTopSerialNumbers();
}
