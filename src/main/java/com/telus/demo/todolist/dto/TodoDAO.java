package com.telus.demo.todolist.dto;

import com.telus.demo.todolist.entities.Todo;

import java.util.List;
import java.util.Optional;

public interface TodoDAO {
    List<Todo> getAllTodos();
    List<Todo> getCompletedTodos();
    List<Todo> getNotCompletedTodos();
    Optional<Todo> getTodoById(Long id);
    void saveTodo(Todo todo);
    void updateTodo(Todo todo);
    void deleteTodoById(Long id);
}
