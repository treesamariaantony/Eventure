package fr.epita.eventure.datamodel;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Date;

@Document(collection = "tasks")
public class Task {

    @Id
    private String id;
    private String title;
    private String description;
    private String status;
    private Date deadline;

    @DBRef
    private Event event;  // Reference to the Event entity

    @DBRef
    private User assignee;  // Reference to the User entity (Assignee)

    // Constructors, Getters, and Setters

    public Task() {}

    public Task(String title, String description, String status, Date deadline, Event event, User assignee) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.deadline = deadline;
        this.event = event;
        this.assignee = assignee;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }
}
