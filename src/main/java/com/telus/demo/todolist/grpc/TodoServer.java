package com.telus.demo.todolist.grpc;

import com.telus.demo.grpc.todo.*;
import com.telus.demo.grpc.todo.TodoServiceGrpc;
import com.telus.demo.grpc.todo.TodoProto.*;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class TodoServer {

    private static final AtomicLong idCounter = new AtomicLong();
    private static final List<Todo> todoList = new ArrayList<>();

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(8081)
                .addService(new TodoServiceImpl())
                .build();

        server.start();
        System.out.println("Server started at port 8080");
        server.awaitTermination();
    }

    public static class TodoServiceImpl extends TodoServiceGrpc.TodoServiceImplBase {

        @Override
        public void createTodo(CreateTodoRequest request, StreamObserver<TodoResponse> responseObserver) {
            Todo todo = Todo.newBuilder()
                    .setId(idCounter.incrementAndGet())
                    .setDescription(request.getDescription())
                    .setCompletionStatus(request.getCompletionStatus())
                    .build();
            todoList.add(todo);

            TodoResponse response = TodoResponse.newBuilder()
                    .setTodo(todo)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void getTodo(GetTodoRequest request, StreamObserver<TodoResponse> responseObserver) {
            Optional<Todo> todo = todoList.stream()
                    .filter(t -> t.getId() == request.getId())
                    .findFirst();

            if (todo.isPresent()) {
                TodoResponse response = TodoResponse.newBuilder()
                        .setTodo(todo.get())
                        .build();
                responseObserver.onNext(response);
            } else {
                responseObserver.onError(new Exception("Todo not found"));
            }
            responseObserver.onCompleted();
        }

        @Override
        public void updateTodo(UpdateTodoRequest request, StreamObserver<TodoResponse> responseObserver) {
            Optional<Todo> existingTodoOpt = todoList.stream()
                    .filter(t -> t.getId() == request.getId())
                    .findFirst();

            if (existingTodoOpt.isPresent()) {
                Todo existingTodo = existingTodoOpt.get();
                Todo updatedTodo = Todo.newBuilder()
                        .setId(existingTodo.getId())
                        .setDescription(request.getDescription())
                        .setCompletionStatus(request.getCompletionStatus())
                        .build();

                todoList.remove(existingTodo);
                todoList.add(updatedTodo);

                TodoResponse response = TodoResponse.newBuilder()
                        .setTodo(updatedTodo)
                        .build();
                responseObserver.onNext(response);
            } else {
                responseObserver.onError(new Exception("Todo not found"));
            }
            responseObserver.onCompleted();
        }

        @Override
        public void deleteTodo(DeleteTodoRequest request, StreamObserver<DeleteTodoResponse> responseObserver) {
            boolean removed = todoList.removeIf(t -> t.getId() == request.getId());

            DeleteTodoResponse response = DeleteTodoResponse.newBuilder()
                    .setSuccess(removed)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void listTodos(ListTodosRequest request, StreamObserver<ListTodosResponse> responseObserver) {
            ListTodosResponse response = ListTodosResponse.newBuilder()
                    .addAllTodos(todoList)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}
