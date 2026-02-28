package ru.collapse.enigma.ticket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.collapse.enigma.ticket.entity.Sentiment;
import ru.collapse.enigma.ticket.entity.Ticket;
import ru.collapse.enigma.ticket.entity.TicketStatus;
import ru.collapse.enigma.ticket.repository.TicketRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final TicketRepository ticketRepository;

    @GetMapping("/create")
    public void create(){
        Ticket ticket = Ticket.builder()
                .mailId("test-mail-id-001")
                .fullName("Иван Иванов")
                .email("ivan.ivanov@example.com")
                .phone("+7 (999) 123-45-67")
                .companyName("ООО Тест")
                .subject("Не работает устройство")
                .rawEmailText("Здравствуйте! У меня не работает устройство с серийным номером SN-12345. Прошу разобраться.")
                .serialNumbers(List.of("SN-12345", "SN-67890"))
                .deviceType("Принтер")
                .sentiment(Sentiment.NEGATIVE)
                .status(TicketStatus.CREATED)
                .parsedSummary("Пользователь сообщает о неисправности принтера.")
                .build();
        ticketRepository.save(ticket);
    }
}
