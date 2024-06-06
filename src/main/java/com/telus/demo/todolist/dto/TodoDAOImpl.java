package com.telus.demo.todolist.dto;

import com.telus.demo.todolist.entities.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class TodoDAOImpl implements TodoDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_TODO = "INSERT INTO todos (description, completion_status) VALUES (?, ?)";
    private static final String SELECT_ALL_TODOS = "SELECT * FROM todos";
    private static final String SELECT_COMPLETED = "SELECT * FROM todos where completion_status= true";
    private static final String SELECT_NOT_COMPLETED = "SELECT * FROM todos  where completion_status= true";
    private static final String SELECT_TODO_BY_ID = "SELECT * FROM todos WHERE id = ?";
    private static final String UPDATE_TODO = "UPDATE todos SET description = ?, completion_status = ? WHERE id = ?";
    private static final String DELETE_TODO_BY_ID = "DELETE FROM todos WHERE id = ?";

    @Override
    public List<Todo> getAllTodos() {
        return jdbcTemplate.query(SELECT_ALL_TODOS, new TodoRowMapper());
    }

    @Override
    public List<Todo> getCompletedTodos() {
        return jdbcTemplate.query(SELECT_COMPLETED, new TodoRowMapper());
    }
    @Override
    public List<Todo> getNotCompletedTodos() {
        return jdbcTemplate.query(SELECT_NOT_COMPLETED, new TodoRowMapper());
    }
    @Override
    public Optional<Todo> getTodoById(Long id) {
        return jdbcTemplate.query(SELECT_TODO_BY_ID, new TodoRowMapper(), id)
                .stream()
                .findFirst();
    }

    @Override
    public void saveTodo(Todo todo) {
        jdbcTemplate.update(INSERT_TODO, todo.getDescription(), todo.getCompletionStatus());
    }

    @Override
    public void updateTodo(Todo todo) {
        jdbcTemplate.update(UPDATE_TODO, todo.getDescription(), todo.getCompletionStatus(), todo.getId());
    }

    @Override
    public void deleteTodoById(Long id) {
        jdbcTemplate.update(DELETE_TODO_BY_ID, id);
    }

    private static class TodoRowMapper implements RowMapper<Todo> {
        @Override
        public Todo mapRow(ResultSet rs, int rowNum) throws SQLException {
            Todo todo = new Todo();
            todo.setId(rs.getLong("id"));
            todo.setDescription(rs.getString("description"));
            todo.setCompletionStatus(rs.getBoolean("completion_status"));
            return todo;
        }
    }
}
