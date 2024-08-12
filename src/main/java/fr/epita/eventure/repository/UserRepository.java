package fr.epita.eventure.repository;

import fr.epita.eventure.datamodel.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
}
