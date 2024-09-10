package org.example;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Objects;
/* Represent the productions from the system */
public abstract class Production implements Comparable<Production>{
    private String title;
    private String type;
    private List<String> directors;
    private List<String> actors;
    private List<Genre> genres;
    private List<Rating> ratings;
    private Double averageRating;
    private String plot;
    private boolean previouslyRated;

    public Production() {

    }
    public void evaluateProduction(Regular regularUser, int rating) {
        // Notify regular users who have evaluated this production
        for (Rating productionRating : ratings) {
            if (productionRating.getUsername().equals(regularUser.getUsername())) {
                // Regular user has evaluated this production
                regularUser.update("The production '" + getTitle() + "' has received a new rating.");
                break;
            }
        }
    }

    public Production(String title,String type, List<String> directors, List<String> actors,
                      List<Genre> genres, List<Rating> ratings, String plot, double averageRating) {
        this.title = title;
        this.directors = directors;
        this.actors = actors;
        this.genres = genres;
        this.ratings = ratings;
        this.plot = plot;
        this.averageRating = averageRating;
        this.type = type;
    }


    private Double calculateAverageRating() {
        if (ratings == null || ratings.isEmpty()) {
            return null;
        }

        double sum = 0;
        for (Rating rating : ratings) {
            sum += rating.getRating();
        }

        return sum / ratings.size();
    }

    public String getProductionTitle() {
        return title;
    }

    public void setProductionTitle(String productionTitle) {
        this.title = title;
    }

    public List<String> getActors() {
        return actors;
    }

    public void setActors(List<String> actors) {
        this.actors = actors;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public void addRating(Rating rating) {
        ratings.add(rating);
    }
    public void removeRating(String username){  ratings.removeIf(rating -> rating.getUsername().equals(username)); }
    public void removeFavorite(User user) {

    }

    public boolean hasUserRated(String username) {
        for (Rating rating : ratings) {
            if (rating.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public void setDirectors(List<String> directors) {
        this.directors = directors;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Production other = (Production) obj;

        return Objects.equals(getTitle(), other.getTitle()) &&
                Objects.equals(getType(), other.getType()) &&
                Objects.equals(getDirectors(), other.getDirectors()) &&
                Objects.equals(getActors(), other.getActors()) &&
                Objects.equals(getGenres(), other.getGenres()) &&
                Objects.equals(getRatings(), other.getRatings()) &&
                Objects.equals(getPlot(), other.getPlot()) &&
                Objects.equals(getAverageRating(), other.getAverageRating());

    }


    @Override
    public String toString() {
        StringBuilder productionDetails = new StringBuilder();
        productionDetails.append("Production{title='").append(title).append('\'');
        productionDetails.append(", type='").append(type).append('\'');
        productionDetails.append(", directors=").append(directors);
        productionDetails.append(", actors=").append(actors);
        productionDetails.append(", genres=").append(genres);
        productionDetails.append(", ratings=").append(ratings.stream().map(Rating::toString).collect(Collectors.toList()));
        productionDetails.append(", averageRating=").append(averageRating);
        productionDetails.append(", plot='").append(plot).append('\'');
        productionDetails.append('}');
        return productionDetails.toString();
    }
    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getType(), getDirectors(), getActors(), getGenres(),
                getRatings(), getPlot(), getAverageRating());
    }

    @Override
    public int compareTo(Production otherProduction) {
        int titleComparison = this.getProductionTitle().compareTo(otherProduction.getProductionTitle());

        if (titleComparison != 0) {
            return titleComparison;
        } else {
            return this.getClass().getName().compareTo(otherProduction.getClass().getName());
        }
    }

    public boolean isPreviouslyRated() {
        return previouslyRated;
    }

    public void setPreviouslyRated(boolean previouslyRated) {
        this.previouslyRated = previouslyRated;
    }

    public void displayInfo() {
        System.out.println("Production Title: " + getTitle());
        System.out.println("Type: " + getType());
        System.out.println("Directors: " + getDirectors());
        System.out.println("Actors: " + getActors());
        System.out.println("Genres: " + getGenres());
        System.out.println("Ratings: " + getRatings());
        System.out.println("Average Rating: " + getAverageRating());
        System.out.println("Plot: " + getPlot());
    }
}
