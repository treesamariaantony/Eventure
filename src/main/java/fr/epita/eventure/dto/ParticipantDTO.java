package fr.epita.eventure.dto;

import fr.epita.eventure.datamodel.EventRole;

public class ParticipantDTO {

    private String id;
    private String userId; // ID of the User
    private String eventId; // ID of the Event
    private EventRole role; // Role enum (CREATOR or PARTICIPANT)

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public EventRole getRole() {
        return role;
    }

    public void setRole(EventRole role) {
        this.role = role;
    }
}
