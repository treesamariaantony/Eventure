package fr.epita.eventure.util;


import fr.epita.eventure.datamodel.Event;
import fr.epita.eventure.datamodel.Participant;
import fr.epita.eventure.datamodel.User;
import fr.epita.eventure.datamodel.EventRole;
import fr.epita.eventure.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component

public class EventAuthorizationUtil {

    @Autowired
    private ParticipantRepository participantRepository;

    public boolean isCreator(Event event, User user) {
        Participant participant = participantRepository.findByEventAndUser(event, user)
                .orElse(null);
        return participant != null && participant.getRole() == EventRole.CREATOR;
    }
}
