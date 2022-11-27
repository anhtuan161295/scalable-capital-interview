package com.scalable.capital.demo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class TaskController {

    private final TaskRepository repository;

    @Autowired
    public TaskController(TaskRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/tasks")
    public ResponseEntity<Long> create(@RequestBody TaskDto dto) {
        Task task = new Task(dto.getTitle());
        task.setDescription(dto.getDescription());
        task = repository.save(task);
        return ResponseEntity.ok(task.getId());
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskDto> readOne(@PathVariable String id) {
        if (StringUtils.isBlank(id)) {
            return ResponseEntity.noContent().build();
        }
        Task task = repository.findById(Long.valueOf(id)).orElse(null);
        if (task == null) {
            return ResponseEntity.noContent().build();
        }
        TaskDto taskDto = task.toDto();
        return ResponseEntity.ok(taskDto);
    }

    List<String> validStatuses = Arrays.asList(TaskStatus.CREATED.name(), TaskStatus.APPROVED.name(), TaskStatus.REJECTED.name(), TaskStatus.BLOCKED.name(),
            TaskStatus.DONE.name());

    @PutMapping("/tasks/{id}")
    public ResponseEntity<Object> update(@PathVariable String id, @RequestBody TaskDto taskDto) {
        if (StringUtils.isBlank(id)) {
            return ResponseEntity.noContent().build();
        }
        Task task = repository.findById(Long.valueOf(id)).orElse(null);
        if (task == null) {
            return ResponseEntity.noContent().build();
        }
        if (!validStatuses.contains(taskDto.getStatus())) {
            return ResponseEntity.badRequest().body("Available statuses are: CREATED, APPROVED, REJECTED, BLOCKED, DONE.");
        }
        if (StringUtils.isNotBlank(taskDto.getTitle())) {
            task.setTitle(taskDto.getTitle());
        }
        if (StringUtils.isNotBlank(taskDto.getDescription())) {
            task.setDescription(taskDto.getDescription());
        }
        task.setTaskStatus(TaskStatus.valueOf(taskDto.getStatus()));
        repository.save(task);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<TaskDto> delete(@PathVariable String id) {
        if (StringUtils.isBlank(id)) {
            return ResponseEntity.noContent().build();
        }
        Task task = repository.findById(Long.valueOf(id)).orElse(null);
        if (task == null) {
            return ResponseEntity.noContent().build();
        }
        repository.deleteById(Long.valueOf(id));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDto>> findAll() {
        List<TaskDto> tasks = StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(i -> i.toDto())
                .collect(Collectors.toCollection(ArrayList::new));
        return ResponseEntity.ok(tasks);
    }

}
