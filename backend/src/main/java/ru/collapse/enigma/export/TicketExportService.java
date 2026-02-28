package ru.collapse.enigma.export;

import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import ru.collapse.enigma.ticket.entity.Ticket;
import ru.collapse.enigma.ticket.repository.TicketRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketExportService {

    private static final String[] HEADERS = {
            "ID", "Mail ID", "Full Name", "Email", "Phone", "Company",
            "Subject", "Device Type", "Serial Numbers", "Sentiment",
            "Status", "Created At", "Answered At"
    };

    private final TicketRepository ticketRepository;

    public void exportCsv(PrintWriter writer) throws IOException {
        List<Ticket> tickets = ticketRepository.findAll();

        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader(HEADERS)
                .build();

        try (CSVPrinter printer = new CSVPrinter(writer, format)) {
            for (Ticket t : tickets) {
                printer.printRecord(
                        t.getId(),
                        t.getMailId(),
                        t.getFullName(),
                        t.getEmail(),
                        t.getPhone(),
                        t.getCompanyName(),
                        t.getSubject(),
                        t.getDeviceType(),
                        t.getSerialNumbers() != null ? String.join(", ", t.getSerialNumbers()) : "",
                        t.getSentiment(),
                        t.getStatus(),
                        t.getCreatedAt(),
                        t.getAnsweredAt()
                );
            }
        }
    }

    public void exportXlsx(OutputStream outputStream) throws IOException {
        List<Ticket> tickets = ticketRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Tickets");

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < HEADERS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(HEADERS[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (Ticket t : tickets) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(t.getId() != null ? t.getId() : 0);
                row.createCell(1).setCellValue(t.getMailId());
                row.createCell(2).setCellValue(t.getFullName());
                row.createCell(3).setCellValue(t.getEmail());
                row.createCell(4).setCellValue(t.getPhone());
                row.createCell(5).setCellValue(t.getCompanyName());
                row.createCell(6).setCellValue(t.getSubject());
                row.createCell(7).setCellValue(t.getDeviceType());
                row.createCell(8).setCellValue(t.getSerialNumbers() != null ? String.join(", ", t.getSerialNumbers()) : "");
                row.createCell(9).setCellValue(t.getSentiment() != null ? t.getSentiment().name() : "");
                row.createCell(10).setCellValue(t.getStatus() != null ? t.getStatus().name() : "");
                row.createCell(12).setCellValue(t.getCreatedAt() != null ? t.getCreatedAt().toString() : "");
                row.createCell(13).setCellValue(t.getAnsweredAt() != null ? t.getAnsweredAt().toString() : "");
            }

            for (int i = 0; i < HEADERS.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
        }
    }
}

