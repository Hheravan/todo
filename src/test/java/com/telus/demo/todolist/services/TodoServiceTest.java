package com.telus.demo.todolist.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.telus.demo.todolist.entities.Todo;
import com.telus.demo.todolist.exception.ResourceAlreadyExistsException;
import com.telus.demo.todolist.exception.ResourceNotFoundException;
import com.telus.demo.todolist.repositories.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    private Todo todo;
    private List<Todo> todoList;

    @BeforeEach
    void setUp() {
        todo = new Todo();
        todo.setId(1L);
        todo.setDescription("Test Task");

        todoList = Arrays.asList(todo);
    }

    @Test
    void testCreateTodo() {
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        Todo createdTodo = todoService.createTodo(todo);

        assertEquals(todo, createdTodo);
        verify(todoRepository, times(1)).save(todo);
    }

    @Test
    void testGetAllTodos() {
        when(todoRepository.findAll()).thenReturn(todoList);

        List<Todo> tasks = todoService.getAllTodos();

        assertEquals(todoList, tasks);
        verify(todoRepository, times(1)).findAll();
    }

    @Test
    void testFindTaskById() {
        when(todoRepository.findById(any(Long.class))).thenReturn(Optional.of(todo));

        Todo foundTodo = todoService.findTaskById(1L);

        assertEquals(todo, foundTodo);
        verify(todoRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAllCompletedTask() {
        when(todoRepository.findByCompletionStatus(Boolean.TRUE)).thenReturn(todoList);

        List<Todo> completedTasks = todoService.findAllCompletedTask();

        assertEquals(todoList, completedTasks);
        verify(todoRepository, times(1)).findByCompletionStatus(Boolean.TRUE);
    }

    @Test
    void testFindAllInCompleteTask() {
        when(todoRepository.findByCompletionStatus(Boolean.FALSE)).thenReturn(todoList);

        List<Todo> incompleteTasks = todoService.findAllInCompleteTask();

        assertEquals(todoList, incompleteTasks);
        verify(todoRepository, times(1)).findByCompletionStatus(Boolean.FALSE);
    }

    @Test
    void testDeleteTask() {
        doNothing().when(todoRepository).deleteById(any(Long.class));

        todoService.deleteTask(1L);

        verify(todoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateTask() {
        Todo updatedTodo = new Todo();
        updatedTodo.setId(1L);
        updatedTodo.setDescription("Updated Task");
        updatedTodo.setCompletionStatus(true);

        when(todoRepository.findByDescription(anyString())).thenReturn(Optional.of(todo));
        when(todoRepository.save(any(Todo.class))).thenReturn(updatedTodo);

        Todo result = todoService.updateTask(updatedTodo);

        assertNotNull(result);
        assertEquals("Updated Task", result.getDescription());
        assertTrue(result.getCompletionStatus());
        verify(todoRepository, times(1)).findByDescription(anyString());
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    void testUpdateTaskNotFound() {
        when(todoRepository.findByDescription(anyString())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            todoService.updateTask(todo);
        });

        assertEquals("Todo not found with the given ID.", exception.getMessage());
        verify(todoRepository, times(1)).findByDescription(anyString());
        verify(todoRepository, times(0)).save(any(Todo.class));
    }

    @Test
    void testUpdateTaskAlreadyExists() {
        Todo existingTodo = new Todo();
        existingTodo.setId(2L);
        existingTodo.setDescription("Test Task");

        when(todoRepository.findByDescription(anyString())).thenReturn(Optional.of(existingTodo));

        ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class, () -> {
            todoService.updateTask(todo);
        });

        assertEquals("Todo already exists  with the given Description.", exception.getMessage());
        verify(todoRepository, times(1)).findByDescription(anyString());
        verify(todoRepository, times(0)).save(any(Todo.class));
    }
}
