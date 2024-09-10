package org.example;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Request implements Subject {
    private List<Observer> observers = new ArrayList<>();
    private RequestType type;
    private LocalDateTime createdDate;
    private String actorName;
    private String movieTitle;
    private String description;
    private String username;
    private String to;
    private boolean resolved ;
    private boolean loaded ;
    private User creator;

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }


    public Request(){

    }

    public Request( RequestType type,
                   LocalDateTime creationDate,
                    String actorName,
                    String movieTitle,
                    String description,
                    String username,
                     String to,
                    User creator

    ) {
        this.type= type;
        this.createdDate = creationDate;
        this.actorName = actorName;
        this.description = description;
        this.username = username;
        this.to = to;
        this.movieTitle = movieTitle;
        this.creator = creator;
    }


    private String determineResolverUsername() {
        if (type!= null) {
            switch (type) {
                case DELETE_ACCOUNT:
                case OTHERS:
                    return "ADMIN";
                case ACTOR_ISSUE:
                case MOVIE_ISSUE:
                    return username;
                default:
                    return null;
            }
        } else {
  
            return null;
        }
    }


    public void resolve(boolean isResolved) {
        if (isResolved) {
            System.out.println("Request resolved. User " + username + " notified.");
            creator.update("Your request has been resolved.");
        } else {
            System.out.println("Request rejected. User " + username + " notified.");
            creator.update("Your request has been rejected.");
        }
    }

    public String displayInfo() {
        StringBuilder result = new StringBuilder();
        result.append("Request Type: ").append(type).append("\n");

        if (createdDate != null) {
            result.append("Creation Date: ").append(createdDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).append("\n");
        } else {
            result.append("Creation Date: null\n");
        }

        result.append("Subject Title: ").append(actorName != null ? actorName : "null").append("\n");
        result.append("Issue Description: ").append(description != null ? description : "null").append("\n");
        result.append("Requester Username: ").append(username!= null ? username : "null").append("\n");
        result.append("Resolver Username: ").append(to != null ? to : "null").append("\n");

        return result.toString();

    }
    //Getters and Setters

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public Request(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public User getCreator() {
        return creator;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }


    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }
}
