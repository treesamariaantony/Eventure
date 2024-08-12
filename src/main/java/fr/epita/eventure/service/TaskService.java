package fr.epita.eventure.service;

import fr.epita.eventure.conversion.TaskMapper;
import fr.epita.eventure.datamodel.Task;
import fr.epita.eventure.datamodel.User;
import fr.epita.eventure.dto.TaskDTO;
import fr.epita.eventure.repository.TaskRepository;
import fr.epita.eventure.util.EventAuthorizationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private EventAuthorizationUtil eventAuthorizationUtil;

    @Autowired
    private UserService userService;  // Assuming UserService is defined and provides getUserById method

    public TaskDTO createTask(TaskDTO taskDTO, String userId) {
        Task task = taskMapper.toEntity(taskDTO);

        User user = userService.getUserById(userId);

        if (eventAuthorizationUtil.isCreator(task.getEvent(), user)) {
            Task savedTask = taskRepository.save(task);
            return taskMapper.toDto(savedTask);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to create tasks for this event.");
        }
    }

    public TaskDTO updateTask(String taskId, TaskDTO updatedTaskDTO, String userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found."));

        User user = userService.getUserById(userId);

        if (eventAuthorizationUtil.isCreator(task.getEvent(), user)) {
            taskMapper.updateFromDto(updatedTaskDTO, task);
            Task updatedTask = taskRepository.save(task);
            return taskMapper.toDto(updatedTask);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to update this task.");
        }
    }

    public void deleteTask(String taskId, String userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found."));

        User user = userService.getUserById(userId);

        if (eventAuthorizationUtil.isCreator(task.getEvent(), user)) {
            taskRepository.delete(task);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to delete this task.");
        }
    }

    public List<TaskDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    public TaskDTO getTaskById(String taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found."));
        return taskMapper.toDto(task);
    }

    public List<TaskDTO> getTasksByTitle(String title) {
        List<Task> tasks = taskRepository.findByTitleContainingIgnoreCase(title);
        return tasks.stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }
}
