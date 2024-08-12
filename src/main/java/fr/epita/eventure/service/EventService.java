//package fr.epita.eventure.service;
//
//import fr.epita.eventure.conversion.EventMapper;
//import fr.epita.eventure.datamodel.Event;
//import fr.epita.eventure.dto.EventDTO;
//import fr.epita.eventure.repository.EventRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//public class EventService {
//
//    private final EventRepository eventRepository;
//    private final EventMapper eventMapper;
//
//    @Autowired
//    public EventService(EventRepository eventRepository, EventMapper eventMapper) {
//        this.eventRepository = eventRepository;
//        this.eventMapper = eventMapper;
//    }
//
//    public List<EventDTO> getAllEvents() {
//        List<Event> events = eventRepository.findAll();
//        return events.stream()
//                .map(eventMapper::toDto)
//                .collect(Collectors.toList());
//    }
//
//    public EventDTO getEventById(String id) {
//        Event event = eventRepository.findById(id).orElse(null);
//        return event != null ? eventMapper.toDto(event) : null;
//    }
//
//    public EventDTO createEvent(EventDTO eventDTO) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null) {
//            boolean hasCreatorRole = authentication.getAuthorities().stream()
//                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_CREATOR"));
//            boolean hasAdminRole = authentication.getAuthorities().stream()
//                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
//
//            if (hasCreatorRole || hasAdminRole) {
//                // Optionally, set the username from authentication
//                String username = authentication.getName();
//                eventDTO.setUsername(username);
//
//                Event event = eventMapper.toEntity(eventDTO);
//                Event savedEvent = eventRepository.save(event);
//                return eventMapper.toDto(savedEvent);
//            } else {
//                throw new SecurityException("User not authorized to create events");
//            }
//        }
//        throw new SecurityException("User not authenticated");
//    }
//
//    public EventDTO updateEvent(String id, EventDTO eventDTO) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null) {
//            boolean hasCreatorRole = authentication.getAuthorities().stream()
//                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_CREATOR"));
//            boolean hasAdminRole = authentication.getAuthorities().stream()
//                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
//
//            if (hasCreatorRole || hasAdminRole) {
//                Optional<Event> existingEventOpt = eventRepository.findById(id);
//                if (existingEventOpt.isPresent()) {
//                    Event existingEvent = existingEventOpt.get();
//                    // Optionally, set the username from authentication
//                    String username = authentication.getName();
//                    eventDTO.setUsername(username);
//
//                    Event updatedEvent = eventMapper.updateFromDto(eventDTO, existingEvent);
//                    Event savedEvent = eventRepository.save(updatedEvent);
//                    return eventMapper.toDto(savedEvent);
//                } else {
//                    throw new RuntimeException("Event not found");
//                }
//            } else {
//                throw new SecurityException("User not authorized to update events");
//            }
//        }
//        throw new SecurityException("User not authenticated");
//    }
//
//    public boolean deleteEvent(String id) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null) {
//            boolean hasCreatorRole = authentication.getAuthorities().stream()
//                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_CREATOR"));
//            boolean hasAdminRole = authentication.getAuthorities().stream()
//                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
//
//            if (hasCreatorRole || hasAdminRole) {
//                Optional<Event> eventOpt = eventRepository.findById(id);
//                if (eventOpt.isPresent()) {
//                    eventRepository.delete(eventOpt.get());
//                    return true;
//                } else {
//                    throw new RuntimeException("Event not found");
//                }
//            } else {
//                throw new SecurityException("User not authorized to delete events");
//            }
//        }
//        throw new SecurityException("User not authenticated");
//    }
//}
package fr.epita.eventure.service;

import fr.epita.eventure.conversion.EventMapper;
import fr.epita.eventure.datamodel.Event;
import fr.epita.eventure.datamodel.User;
import fr.epita.eventure.dto.EventDTO;
import fr.epita.eventure.repository.EventRepository;
import fr.epita.eventure.util.EventAuthorizationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private EventAuthorizationUtil eventAuthorizationUtil;

    @Autowired
    private UserService userService;

    public EventDTO createEvent(EventDTO eventDTO) {
        Event event = eventMapper.toEntity(eventDTO);
        Event savedEvent = eventRepository.save(event);
        return eventMapper.toDto(savedEvent);
    }

    public EventDTO updateEvent(String eventId, EventDTO updatedEventDTO, String userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found."));

        User user = userService.getUserById(userId);

        if (eventAuthorizationUtil.isCreator(event, user)) {
            eventMapper.updateFromDto(updatedEventDTO, event);
            Event updatedEvent = eventRepository.save(event);
            return eventMapper.toDto(updatedEvent);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to update this event.");
        }
    }

    public void deleteEvent(String eventId, String userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found."));

        User user = userService.getUserById(userId);

        if (eventAuthorizationUtil.isCreator(event, user)) {
            eventRepository.delete(event);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to delete this event.");
        }
    }

    public List<EventDTO> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    public EventDTO getEventById(String eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found."));
        return eventMapper.toDto(event);
    }
}
