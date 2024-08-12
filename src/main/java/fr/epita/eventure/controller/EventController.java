package fr.epita.eventure.controller;

import fr.epita.eventure.dto.EventDTO;
import fr.epita.eventure.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO eventDTO, @RequestHeader("userId") String userId) {
        eventDTO.setUserId(userId); // Ensure the event creator is the authenticated user
        EventDTO createdEvent = eventService.createEvent(eventDTO);
        return ResponseEntity.ok(createdEvent);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable String eventId, @RequestBody EventDTO eventDTO, @RequestHeader("userId") String userId) {
        EventDTO updatedEvent = eventService.updateEvent(eventId, eventDTO, userId);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String eventId, @RequestHeader("userId") String userId) {
        eventService.deleteEvent(eventId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        List<EventDTO> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable String eventId) {
        EventDTO event = eventService.getEventById(eventId);
        return ResponseEntity.ok(event);
    }
}
