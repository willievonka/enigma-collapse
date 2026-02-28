package ru.collapse.enigma.listener.process;

import org.springframework.stereotype.Component;
import ru.collapse.enigma.ticket.entity.Ticket;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EmailPhoneProcessor {

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "(?<!\\d)" +
            "(?:\\+?[78][\\s.\\-]?)?" +                    // код страны: +7 / 7 / +8 / 8 (опционально)
            "\\(?\\d{3}\\)?[\\s.\\-]?" +                   // код оператора (3 цифры, возможно в скобках)
            "\\d{3}[\\s.\\-]?" +                           // первые 3 цифры номера
            "\\d{2}[\\s.\\-]?" +                           // следующие 2 цифры
            "\\d{2}" +                                     // последние 2 цифры
            "(?!\\d)"
    );

    private static final Pattern DIGITS_ONLY = Pattern.compile("\\D");

    public void parsePhoneNumber(Ticket ticket) {
        String text = ticket.getRawEmailText();

        String phoneNumber = null;
        if (text != null) {
            Matcher matcher = PHONE_PATTERN.matcher(text);
            if (matcher.find()) {
                phoneNumber = normalize(matcher.group());
            }
        }

        ticket.setPhone(phoneNumber);
    }

    private String normalize(String raw) {
        String digits = DIGITS_ONLY.matcher(raw).replaceAll("");

        return switch (digits.length()) {
            case 11 -> "+7" + digits.substring(1);
            case 10 -> "+7" + digits;
            default -> null;
        };
    }
}
