package ru.collapse.enigma.export.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.collapse.enigma.export.service.TicketExportService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tickets/export")
public class TicketExportController {

    private final TicketExportService ticketExportService;

    @GetMapping("/csv")
    @ResponseStatus(HttpStatus.OK)
    public void exportCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=tickets.csv");
        response.setCharacterEncoding("UTF-8");
        ticketExportService.exportCsv(response.getWriter());
    }

    @GetMapping("/xlsx")
    @ResponseStatus(HttpStatus.OK)
    public void exportXlsx(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=tickets.xlsx");
        ticketExportService.exportXlsx(response.getOutputStream());
    }
}

