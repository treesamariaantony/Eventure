package fr.epita.eventure.conversion;


import fr.epita.eventure.datamodel.Event;
import fr.epita.eventure.dto.EventDTO;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public EventDTO toDto(Event event) {
        EventDTO dto = new EventDTO();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setStartDate(event.getStartDate());
        dto.setEndDate(event.getEndDate());
        dto.setDescription(event.getDescription());

        return dto;
    }

    public Event toEntity(EventDTO dto) {
        Event event = new Event();
        event.setId(dto.getId());
        event.setTitle(dto.getTitle());
        event.setStartDate(dto.getStartDate());
        event.setEndDate(dto.getEndDate());
        event.setDescription(dto.getDescription());
        return event;
    }

    public Event updateFromDto(EventDTO dto, Event event) {
        event.setTitle(dto.getTitle());
        event.setStartDate(dto.getStartDate());
        event.setEndDate(dto.getEndDate());
        event.setDescription(dto.getDescription());
        return event;
    }
}
