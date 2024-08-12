package fr.epita.eventure.conversion;

import fr.epita.eventure.datamodel.Participant;
import fr.epita.eventure.datamodel.User;
import fr.epita.eventure.datamodel.Event;
import fr.epita.eventure.datamodel.EventRole;
import fr.epita.eventure.dto.ParticipantDTO;
import fr.epita.eventure.repository.UserRepository;
import fr.epita.eventure.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParticipantMapper {

    @Autowired
    private UserRepository userRepository; // Inject UserRepository

    @Autowired
    private EventRepository eventRepository; // Inject EventRepository

    public ParticipantDTO toDto(Participant participant) {
        ParticipantDTO dto = new ParticipantDTO();
        dto.setId(participant.getId());
        dto.setUserId(participant.getUser() != null ? participant.getUser().getId() : null);
        dto.setEventId(participant.getEvent() != null ? participant.getEvent().getId() : null);
        dto.setRole(participant.getRole());
        return dto;
    }

    public Participant toEntity(ParticipantDTO dto) {
        Participant participant = new Participant();

        // Fetch User and Event entities from repositories
        User user = userRepository.findById(dto.getUserId()).orElse(null);
        Event event = eventRepository.findById(dto.getEventId()).orElse(null);

        participant.setId(dto.getId());
        participant.setUser(user);
        participant.setEvent(event);
        participant.setRole(dto.getRole());

        return participant;
    }

    public Participant updateFromDto(ParticipantDTO dto, Participant participant) {
        User user = userRepository.findById(dto.getUserId()).orElse(null);
        Event event = eventRepository.findById(dto.getEventId()).orElse(null);

        participant.setUser(user);
        participant.setEvent(event);
        participant.setRole(dto.getRole());

        return participant;
    }
}
