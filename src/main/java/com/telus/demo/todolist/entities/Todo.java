package com.telus.demo.todolist.entities;


import jakarta.persistence.*;

@Entity
@Table
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // this is the primary key which will be auto generated
    private Long id;
    @Column(unique=true)
    private String description;
    private Boolean completionStatus = false;

    public Todo() {

    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getCompletionStatus() {
        return this.completionStatus;
    }
    public void setCompletionStatus(Boolean status) {
        this.completionStatus = status;
    }
}
