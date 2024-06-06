package com.telus.demo.todolist.repositories;

import com.telus.demo.todolist.entities.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByCompletionStatus(Boolean Status);
    Optional<Todo> findByDescription(String description);
}
