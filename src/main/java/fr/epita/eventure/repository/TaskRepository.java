package fr.epita.eventure.repository;

import fr.epita.eventure.datamodel.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends MongoRepository<Task, String> {

    List<Task> findByTitleContainingIgnoreCase(String title);

}
