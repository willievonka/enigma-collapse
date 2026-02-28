package ru.collapse.enigma.ticket.mapper;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.collapse.enigma.ticket.dto.PageResponseDto;

@Component
public class PageMapper {

    public <T> PageResponseDto<T> toDto(Page<T> tickets) {
        return new PageResponseDto<>(
                tickets.getContent(),
                tickets.getNumber(),
                tickets.getSize(),
                tickets.getTotalElements(),
                tickets.getTotalPages());
    }
}
