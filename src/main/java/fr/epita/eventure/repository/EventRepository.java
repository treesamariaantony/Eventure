package fr.epita.eventure.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import fr.epita.eventure.datamodel.Event;

public interface EventRepository extends MongoRepository<Event, String> {
    // Additional methods for custom queries can be added here if needed
}
