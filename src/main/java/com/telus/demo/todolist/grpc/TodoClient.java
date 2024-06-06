package com.telus.demo.todolist.grpc;

import com.telus.demo.grpc.todo.*;
import com.telus.demo.grpc.todo.TodoServiceGrpc;
import com.telus.demo.grpc.todo.TodoProto.*;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.List;

public class TodoClient {

    private final TodoServiceGrpc.TodoServiceBlockingStub blockingStub;

    public TodoClient(ManagedChannel channel) {
        blockingStub = TodoServiceGrpc.newBlockingStub(channel);
    }

    public Todo createTodo(String description, boolean completionStatus) {
        CreateTodoRequest request = CreateTodoRequest.newBuilder()
                .setDescription(description)
                .setCompletionStatus(completionStatus)
                .build();
        TodoResponse response = blockingStub.createTodo(request);
        return response.getTodo();
    }

    public Todo getTodoById(long id) {
        GetTodoRequest request = GetTodoRequest.newBuilder()
                .setId(id)
                .build();
        TodoResponse response = blockingStub.getTodo(request);
        return response.getTodo();
    }

    public Todo updateTodo(long id, String description, boolean completionStatus) {
        UpdateTodoRequest request = UpdateTodoRequest.newBuilder()
                .setId(id)
                .setDescription(description)
                .setCompletionStatus(completionStatus)
                .build();
        TodoResponse response = blockingStub.updateTodo(request);
        return response.getTodo();
    }

    public boolean deleteTodoById(long id) {
        DeleteTodoRequest request = DeleteTodoRequest.newBuilder()
                .setId(id)
                .build();
        DeleteTodoResponse response = blockingStub.deleteTodo(request);
        return response.getSuccess();
    }

    public List<Todo> listTodos() {
        ListTodosRequest request = ListTodosRequest.newBuilder().build();
        ListTodosResponse response = blockingStub.listTodos(request);
        return response.getTodosList();
    }

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8081)
                .usePlaintext()
                .build();

        TodoClient client = new TodoClient(channel);

        // Create a new Todo
        Todo todo = client.createTodo("Test Todo", false);
        System.out.println("Created Todo: " + todo);

        // Get the Todo by ID
        Todo fetchedTodo = client.getTodoById(todo.getId());
        System.out.println("Fetched Todo: " + fetchedTodo);

        // Update the Todo
        Todo updatedTodo = client.updateTodo(todo.getId(), "testtodo 2", true);
        System.out.println("Updated Todo: " + updatedTodo);

        // List all Todos
        List<Todo> todos = client.listTodos();
        System.out.println("All Todos: " + todos);

        // Delete the Todo by ID
        boolean isDeleted = client.deleteTodoById(todo.getId());
        System.out.println("Deleted Todo: " + isDeleted);

        // List all Todos after deletion
        todos = client.listTodos();
        System.out.println("All Todos after deletion: " + todos);

        channel.shutdown();
    }
}
