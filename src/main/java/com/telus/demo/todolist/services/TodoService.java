package com.telus.demo.todolist.services;

import com.telus.demo.todolist.entities.Todo;
import com.telus.demo.todolist.exception.ResourceAlreadyExistsException;
import com.telus.demo.todolist.exception.ResourceNotFoundException;
import com.telus.demo.todolist.repositories.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TodoService {
    @Autowired
    private TodoRepository todoRepository;

    public Todo createTodo(Todo todo) {
        var existingStudent = todoRepository.findByDescription(todo.getDescription()).orElse(null);
        if(existingStudent != null){
            throw new ResourceAlreadyExistsException("Todo already exists  with the given Description.");
        }
        return todoRepository.save(todo);
    }


    public List<Todo> getAllTodos() {
       return todoRepository.findAll();
    }

    public Todo findTaskById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with the given ID."));
    }

    public List<Todo> findAllCompletedTask() {
        return todoRepository.findByCompletionStatus(Boolean.TRUE);
    }

    public List<Todo> findAllInCompleteTask() {

        return todoRepository.findByCompletionStatus(Boolean.FALSE);
    }

    public void deleteTask(Long taskId) {
        todoRepository.deleteById(taskId);
    }

    public Todo updateTask(Todo todo) {
        var existingTodo = todoRepository.findByDescription(todo.getDescription()).orElse(null);
        if(existingTodo == null){
            throw new ResourceNotFoundException("Todo not found with the given ID.");
        } else if (!existingTodo.getId().equals(todo.getId())){
            throw new ResourceAlreadyExistsException("Todo already exists  with the given Description.");
        }
        return todoRepository.save(todo);
    }
}
