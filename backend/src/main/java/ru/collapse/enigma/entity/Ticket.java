package ru.collapse.enigma.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.collapse.enigma.entity.enums.TicketStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сущность заявки на поддержку.
 * Представляет собой заявку, созданную пользователем, которая содержит информацию о проблеме и может быть обработана службой поддержки.
 */
@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone", length = 50)
    private String phone;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "subject", nullable = false, length = 500)
    private String subject;

    @Column(name = "raw_email_text", nullable = false)
    private String rawEmailText;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "serial_numbers", columnDefinition = "jsonb")
    private List<String> serialNumbers;

    @Column(name = "device_type")
    private String deviceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private TicketStatus status = TicketStatus.CREATED;

    @Column(name = "sentiment", nullable = false, length = 20)
    private String sentiment;

    @Column(name = "parsed_summary")
    private String parsedSummary;

    @Column(name = "generated_response")
    private String generatedResponse;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "answered_at")
    private LocalDateTime answeredAt;
}
