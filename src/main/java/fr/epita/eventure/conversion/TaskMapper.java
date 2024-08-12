package fr.epita.eventure.conversion;

import fr.epita.eventure.datamodel.Task;
import fr.epita.eventure.datamodel.User;
import fr.epita.eventure.datamodel.Event;
import fr.epita.eventure.dto.TaskDTO;
import fr.epita.eventure.repository.UserRepository;
import fr.epita.eventure.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    @Autowired
    private UserRepository userRepository; // Inject UserRepository

    @Autowired
    private EventRepository eventRepository; // Inject EventRepository

    public TaskDTO toDto(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setDeadline(task.getDeadline());
        dto.setEventId(task.getEvent() != null ? task.getEvent().getId() : null);
        dto.setAssigneeId(task.getAssignee() != null ? task.getAssignee().getId() : null);
        return dto;
    }

    public Task toEntity(TaskDTO dto) {
        Task task = new Task();

        // Fetch User and Event entities from repositories
        User assignee = userRepository.findById(dto.getAssigneeId()).orElse(null);
        Event event = eventRepository.findById(dto.getEventId()).orElse(null);

        task.setId(dto.getId());
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setDeadline(dto.getDeadline());
        task.setEvent(event);
        task.setAssignee(assignee);

        return task;
    }

    public Task updateFromDto(TaskDTO dto, Task task) {
        User assignee = userRepository.findById(dto.getAssigneeId()).orElse(null);
        Event event = eventRepository.findById(dto.getEventId()).orElse(null);

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setDeadline(dto.getDeadline());
        task.setEvent(event);
        task.setAssignee(assignee);

        return task;
    }
}
