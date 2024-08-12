package fr.epita.eventure.controller;

import fr.epita.eventure.dto.TaskDTO;
import fr.epita.eventure.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO, @RequestHeader("userId") String userId) {
        TaskDTO createdTask = taskService.createTask(taskDTO, userId);
        return ResponseEntity.ok(createdTask);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable String taskId, @RequestBody TaskDTO taskDTO, @RequestHeader("userId") String userId) {
        TaskDTO updatedTask = taskService.updateTask(taskId, taskDTO, userId);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable String taskId, @RequestHeader("userId") String userId) {
        taskService.deleteTask(taskId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        List<TaskDTO> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable String taskId) {
        TaskDTO task = taskService.getTaskById(taskId);
        return ResponseEntity.ok(task);
    }
}
