package fr.epita.eventure.datamodel;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Document(collection = "participants")
public class Participant {

    @Id
    private String id;

    @DBRef
    private User user;  // Reference to the User entity

    @DBRef
    private Event event;  // Reference to the Event entity

    private EventRole role;  // Enum for Role (CREATOR, PARTICIPANT)

    // Constructors, Getters, and Setters

    public Participant() {}

    public Participant(User user, Event event, EventRole role) {
        this.user = user;
        this.event = event;
        this.role = role;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public EventRole getRole() {
        return role;
    }

    public void setRole(EventRole role) {
        this.role = role;
    }
}
