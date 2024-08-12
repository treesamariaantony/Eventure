package fr.epita.eventure.dto;

import java.util.Date;

public class EventDTO {

    private String id;
    private String title;
    private Date startDate;
    private Date endDate;
    private String description;
    private String location;
    private String userId;  // Reference to the User ID (instead of the full User object)

    // Constructors, Getters, and Setters

    public EventDTO() {}

    public EventDTO(String id, String title, Date startDate, Date endDate, String description, String location, String userId) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.location = location;
        this.userId = userId;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
