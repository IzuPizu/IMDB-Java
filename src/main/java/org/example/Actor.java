package org.example;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Actor implements Comparable<Actor>{
    private String actorName;
    private List<Performance> performances;
    private String biography;
    private boolean underAdminResponsibility;

    public Actor(String name, List<Performance> performances, String biography) {
        this.actorName = name;
        this.performances = performances;
        this.biography = biography;
    }
    public static class Performance {
        private String title;
        private String type;
        public Performance() {
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Performance performance = (Performance) obj;
            return Objects.equals(title, performance.title) && Objects.equals(type, performance.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title, type);
        }

        //@JsonCreator
        public Performance( String title, String type) {
            this.title = title;
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }


        @Override
        public String toString() {
            return "Performance{" +
                    "title='" + title + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }

    }

    //Dummy constructor
    public Actor() {  }
    // Getters and setters

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Actor actor = (Actor) obj;
        // Compare fields for equality
        return Objects.equals(actorName, actor.actorName) &&
                Objects.equals(performances, actor.performances) &&
                Objects.equals(biography, actor.biography);
    }
    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }


    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public List<Performance> getPerformances() {
        return performances;
    }

    public void setPerformances(List<Performance> performances) {
        this.performances = performances;
    }


    public String getName() {
        return actorName;
    }

    public void setName(String name) {
        this.actorName = name;
    }

    @Override
    public int compareTo(Actor other) {
        return this.actorName.compareTo(other.actorName);
    }
    public boolean isUnderAdminResponsibility() {
        boolean underAdminResponsibility = false ;
        return underAdminResponsibility;
    }

    public void setUnderAdminResponsibility(boolean underAdminResponsibility) {
        this.underAdminResponsibility = underAdminResponsibility;
    }

    @Override
    public String toString() {
        return "Actor{" +
                "actorName='" + actorName + '\'' +
                ", performances=" + performances +
                ", biography='" + biography + '\'' +
                '}';
    }


}
