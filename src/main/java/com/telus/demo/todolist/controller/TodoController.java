package com.telus.demo.todolist.controller;

import com.telus.demo.todolist.entities.Todo;
import com.telus.demo.todolist.services.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping("/")
    public ResponseEntity<List<Todo>> getAllTasks() {
        return ResponseEntity.ok(todoService.getAllTodos());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(todoService.findTaskById(id));
    }


    @GetMapping("/completed")
    public ResponseEntity<List<Todo>> getAllCompletedTasks() {
        return ResponseEntity.ok(todoService.findAllCompletedTask());
    }

    @GetMapping("/incomplete")
    public ResponseEntity<List<Todo>> getAllIncompleteTasks() {
        return ResponseEntity.ok(todoService.findAllInCompleteTask());
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Todo> createTask(@RequestBody Todo todo) {
        return ResponseEntity.ok(todoService.createTodo(todo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTask(@PathVariable Long id, @RequestBody Todo todo) {
        todo.setId(id);
        return ResponseEntity.ok(todoService.updateTask(todo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteTask(@PathVariable Long id) {
        todoService.deleteTask(id);
        return ResponseEntity.ok(true);
    }
}