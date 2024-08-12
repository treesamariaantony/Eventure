package fr.epita.eventure.conversion;

import fr.epita.eventure.datamodel.Event;
import fr.epita.eventure.datamodel.User;
import fr.epita.eventure.dto.EventDTO;
import fr.epita.eventure.repository.UserRepository; // Import your UserRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    @Autowired
    private UserRepository userRepository; // Inject UserRepository to fetch User entities

    /**
     * Converts an Event entity to an EventDTO.
     *
     * @param event The Event entity.
     * @return The corresponding EventDTO.
     */
    public EventDTO toDto(Event event) {
        EventDTO dto = new EventDTO();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setStartDate(event.getStartDate());
        dto.setEndDate(event.getEndDate());
        dto.setDescription(event.getDescription());
        dto.setLocation(event.getLocation());
        dto.setUserId(event.getUser() != null ? event.getUser().getId() : null); // Use userId
        return dto;
    }

    /**
     * Converts an EventDTO to an Event entity.
     *
     * @param dto The EventDTO.
     * @return The corresponding Event entity.
     */
    public Event toEntity(EventDTO dto) {
        Event event = new Event();
        event.setId(dto.getId());
        event.setTitle(dto.getTitle());
        event.setStartDate(dto.getStartDate());
        event.setEndDate(dto.getEndDate());
        event.setDescription(dto.getDescription());
        event.setLocation(dto.getLocation());

        // Fetch User entity from UserRepository
        User user = userRepository.findById(dto.getUserId()).orElse(null);
        event.setUser(user);

        return event;
    }

    /**
     * Updates an existing Event entity from an EventDTO.
     *
     * @param dto The EventDTO.
     * @param event The Event entity to update.
     * @return The updated Event entity.
     */
    public Event updateFromDto(EventDTO dto, Event event) {
        event.setTitle(dto.getTitle());
        event.setStartDate(dto.getStartDate());
        event.setEndDate(dto.getEndDate());
        event.setDescription(dto.getDescription());
        event.setLocation(dto.getLocation());

        // Update the User entity based on the userId
        User user = userRepository.findById(dto.getUserId()).orElse(null);
        event.setUser(user);

        return event;
    }
}
