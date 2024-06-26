package fr.epita.eventure.service;

import fr.epita.eventure.conversion.EventMapper;
import fr.epita.eventure.datamodel.Event;
import fr.epita.eventure.dto.EventDTO;
import fr.epita.eventure.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Autowired
    public EventService(EventRepository eventRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    public List<EventDTO> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    public EventDTO getEventById(String id) {
        Event event = eventRepository.findById(id).orElse(null);
        return event != null ? eventMapper.toDto(event) : null;
    }

    public EventDTO createEvent(EventDTO eventDTO) {
        Event event = eventMapper.toEntity(eventDTO);
        Event savedEvent = eventRepository.save(event);
        return eventMapper.toDto(savedEvent);
    }

    public EventDTO updateEvent(String id, EventDTO eventDTO) {
        Event existingEvent = eventRepository.findById(id).orElse(null);
        if (existingEvent != null) {
            Event updatedEvent = eventMapper.updateFromDto(eventDTO, existingEvent);
            Event savedEvent = eventRepository.save(updatedEvent);
            return eventMapper.toDto(savedEvent);
        }
        return null;
    }

    public boolean deleteEvent(String id) {
        Event event = eventRepository.findById(id).orElse(null);
        if (event != null) {
            eventRepository.delete(event);
            return true;
        }
        return false;
    }
}
