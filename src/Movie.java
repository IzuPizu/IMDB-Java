package org.example;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class Movie extends Production {
    private String duration;
    private Long releaseYear;

    public Movie(String title,String type, List<String> directors, List<String> actors,
                 List<Genre> genres, List<Rating> ratings, String plot,
                 double averageRating, String duration, Long releaseYear) {
        super(title,type, directors, actors, genres, ratings, plot, averageRating);
        this.duration = duration;
        this.releaseYear = releaseYear;
    }

    public Movie(){
        super();
    }

    @Override
    public void displayInfo() {
        super.displayInfo();  // Afișează informațiile generice ale producției
        System.out.println("Duration: " + getDuration() + " minutes");
        System.out.println("Release Year: " + (getReleaseYear() != null ? getReleaseYear() : "N/A"));
    }
    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Long getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Long releaseYear) {
        this.releaseYear = releaseYear;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Movie other = (Movie) obj;

        return Objects.equals(getTitle(), other.getTitle()) &&
                Objects.equals(getType(), other.getType()) &&
                Objects.equals(getDirectors(), other.getDirectors()) &&
                Objects.equals(getActors(), other.getActors()) &&
                Objects.equals(getGenres(), other.getGenres()) &&
                Objects.equals(getRatings(), other.getRatings()) &&
                Objects.equals(getPlot(), other.getPlot()) &&
                Objects.equals(getAverageRating(), other.getAverageRating()) &&
                Objects.equals(duration, other.duration) &&
                Objects.equals(releaseYear, other.releaseYear);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getType(), getDirectors(), getActors(), getGenres(),
                getRatings(), getPlot(), getAverageRating(), duration, releaseYear);
    }


}
