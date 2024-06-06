package com.telus.demo.todolist.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telus.demo.todolist.entities.Todo;
import com.telus.demo.todolist.services.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(TodoController.class)
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    private Todo todo;
    private Todo todoCompleted;
    private List<Todo> todoList;
    private List<Todo> todoCompletedList;

    @BeforeEach
    void setUp() {
        todo = new Todo();
        todo.setId(1L);
        todo.setDescription("Test Task");
        todoList = Arrays.asList(todo);

        todoCompleted = new Todo();
        todoCompleted.setId(2L);
        todoCompleted.setDescription("Test Task completed");
        todoCompleted.setCompletionStatus(Boolean.TRUE);
        todoCompletedList = Arrays.asList(todoCompleted);
    }

    @Test
    void testGetAllTasks() throws Exception {
        when(todoService.getAllTodos()).thenReturn(todoList);

        mockMvc.perform(get("/api/v1/todos/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].description").value("Test Task"));

        verify(todoService, times(1)).getAllTodos();
    }

    @Test
    void testGetTaskById() throws Exception {
        when(todoService.findTaskById(anyLong())).thenReturn(todo);

        mockMvc.perform(get("/api/v1/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Test Task"));

        verify(todoService, times(1)).findTaskById(anyLong());
    }

    @Test
    void testGetAllCompletedTasks() throws Exception {
        when(todoService.findAllCompletedTask()).thenReturn(todoCompletedList);

        mockMvc.perform(get("/api/v1/todos/completed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(2L))
                .andExpect(jsonPath("$[0].description").value("Test Task completed"));

        verify(todoService, times(1)).findAllCompletedTask();
    }

    @Test
    void testGetAllIncompleteTasks() throws Exception {
        when(todoService.findAllInCompleteTask()).thenReturn(todoList);

        mockMvc.perform(get("/api/v1/todos/incomplete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].description").value("Test Task"));

        verify(todoService, times(1)).findAllInCompleteTask();
    }

    @Test
    void testCreateTask() throws Exception {
        when(todoService.createTodo(any(Todo.class))).thenReturn(todo);

        mockMvc.perform(post("/api/v1/todos/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(todo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Test Task"));

        verify(todoService, times(1)).createTodo(any(Todo.class));
    }

    @Test
    void testUpdateTask() throws Exception {
        when(todoService.updateTask(any(Todo.class))).thenReturn(todo);

        mockMvc.perform(put("/api/v1/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(todo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Test Task"));

        verify(todoService, times(1)).updateTask(any(Todo.class));
    }

    @Test
    void testDeleteTask() throws Exception {
        doNothing().when(todoService).deleteTask(anyLong());

        mockMvc.perform(delete("/api/v1/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));

        verify(todoService, times(1)).deleteTask(anyLong());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
