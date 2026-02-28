package ru.collapse.enigma.listener.process;

import ru.collapse.enigma.ticket.entity.Ticket;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailPhoneProcessor {

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "(?:\\+7|8)[\\s\\-]?\\(?\\d{3}\\)?[\\s\\-]?\\d{3}[\\s\\-]?\\d{2}[\\s\\-]?\\d{2}"
    );

    public void parsePhoneNumber(Ticket ticket) {
        String text = ticket.getRawEmailText();

        String phoneNumber = null;
        if (text != null) {
            Matcher matcher = PHONE_PATTERN.matcher(text);
            if (matcher.find()) {
                phoneNumber = matcher.group().replaceAll("[\\s\\-]", "");
            }
        }

        ticket.setPhone(phoneNumber);
    }
}
