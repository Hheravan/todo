package com.telus.demo.todolist.grpc;

import com.telus.demo.grpc.todo.*;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class TodoServiceImplTest {

    @InjectMocks
    private TodoServer.TodoServiceImpl todoServiceImpl;

    @Mock
    private AtomicLong idCounter;

    @Mock
    private List<Todo> todoList;

    @Mock
    private StreamObserver<TodoResponse> todoResponseObserver;

    @Mock
    private StreamObserver<DeleteTodoResponse> deleteTodoResponseObserver;

    @Mock
    private StreamObserver<ListTodosResponse> listTodosResponseObserver;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        todoServiceImpl = new TodoServer.TodoServiceImpl();
        todoList = new ArrayList<>();
        idCounter = new AtomicLong(1);
    }

    @Test
    public void testCreateTodo() {
        // Arrange
        CreateTodoRequest request = CreateTodoRequest.newBuilder()
                .setDescription("Test Todo")
                .setCompletionStatus(false)
                .build();
        when(idCounter.incrementAndGet()).thenReturn(1L);

        // Act
        todoServiceImpl.createTodo(request, todoResponseObserver);

        // Assert
        ArgumentCaptor<TodoResponse> responseCaptor = ArgumentCaptor.forClass(TodoResponse.class);
        verify(todoResponseObserver).onNext(responseCaptor.capture());
        verify(todoResponseObserver).onCompleted();

        TodoResponse response = responseCaptor.getValue();
        assertEquals(1L, response.getTodo().getId());
        assertEquals("Test Todo", response.getTodo().getDescription());
        assertEquals(false, response.getTodo().getCompletionStatus());
    }

    @Test
    public void testGetTodo() {
        // Arrange
        Todo todo = Todo.newBuilder()
                .setId(1L)
                .setDescription("Test Todo")
                .setCompletionStatus(false)
                .build();
        todoList.add(todo);
        GetTodoRequest request = GetTodoRequest.newBuilder().setId(1L).build();

        // Act
        todoServiceImpl.getTodo(request, todoResponseObserver);

        // Assert
        ArgumentCaptor<TodoResponse> responseCaptor = ArgumentCaptor.forClass(TodoResponse.class);
        verify(todoResponseObserver).onNext(responseCaptor.capture());
        verify(todoResponseObserver).onCompleted();

        TodoResponse response = responseCaptor.getValue();
        assertEquals(1L, response.getTodo().getId());
        assertEquals("Test Todo", response.getTodo().getDescription());
        assertEquals(false, response.getTodo().getCompletionStatus());
    }

    @Test
    public void testUpdateTodo() {
        // Arrange
        Todo todo = Todo.newBuilder()
                .setId(1L)
                .setDescription("Test Todo")
                .setCompletionStatus(false)
                .build();
        todoList.add(todo);
        UpdateTodoRequest request = UpdateTodoRequest.newBuilder()
                .setId(1L)
                .setDescription("Test Todo 2")
                .setCompletionStatus(true)
                .build();

        // Act
        todoServiceImpl.updateTodo(request, todoResponseObserver);

        // Assert
        ArgumentCaptor<TodoResponse> responseCaptor = ArgumentCaptor.forClass(TodoResponse.class);
        verify(todoResponseObserver).onNext(responseCaptor.capture());
        verify(todoResponseObserver).onCompleted();

        TodoResponse response = responseCaptor.getValue();
        assertEquals(1L, response.getTodo().getId());
        assertEquals("Test Todo 2", response.getTodo().getDescription());
        assertEquals(true, response.getTodo().getCompletionStatus());
    }

    @Test
    public void testDeleteTodo() {
        // Arrange
        Todo todo = Todo.newBuilder()
                .setId(1L)
                .setDescription("Test Todo")
                .setCompletionStatus(false)
                .build();
        todoList.add(todo);
        DeleteTodoRequest request = DeleteTodoRequest.newBuilder().setId(1L).build();

        // Act
        todoServiceImpl.deleteTodo(request, deleteTodoResponseObserver);

        // Assert
        ArgumentCaptor<DeleteTodoResponse> responseCaptor = ArgumentCaptor.forClass(DeleteTodoResponse.class);
        verify(deleteTodoResponseObserver).onNext(responseCaptor.capture());
        verify(deleteTodoResponseObserver).onCompleted();

        DeleteTodoResponse response = responseCaptor.getValue();
        assertTrue(response.getSuccess());
    }

    @Test
    public void testListTodos() {
        // Arrange
        Todo todo1 = Todo.newBuilder()
                .setId(1L)
                .setDescription("Test Todo")
                .setCompletionStatus(false)
                .build();
        Todo todo2 = Todo.newBuilder()
                .setId(2L)
                .setDescription("Test Todo 2")
                .setCompletionStatus(false)
                .build();
        todoList.add(todo1);
        todoList.add(todo2);
        ListTodosRequest request = ListTodosRequest.newBuilder().build();

        // Act
        todoServiceImpl.listTodos(request, listTodosResponseObserver);

        // Assert
        ArgumentCaptor<ListTodosResponse> responseCaptor = ArgumentCaptor.forClass(ListTodosResponse.class);
        verify(listTodosResponseObserver).onNext(responseCaptor.capture());
        verify(listTodosResponseObserver).onCompleted();

        ListTodosResponse response = responseCaptor.getValue();
        assertEquals(2, response.getTodosCount());
        assertEquals(1L, response.getTodos(0).getId());
        assertEquals(2L, response.getTodos(1).getId());
    }
}
