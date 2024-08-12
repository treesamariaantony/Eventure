package fr.epita.eventure.repository;

import fr.epita.eventure.datamodel.Event;
import fr.epita.eventure.datamodel.Participant;
import fr.epita.eventure.datamodel.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface ParticipantRepository extends MongoRepository<Participant, String> {
    Optional<Participant> findByEventAndUser(Event event, User user);
}

