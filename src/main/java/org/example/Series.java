package org.example;
import org.jetbrains.annotations.NotNull;

import java.lang.Comparable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
public class Series extends Production {

    private Long releaseYear;
    private Long numSeasons;
    private Map<String, List<Episode>> seasons;

    public Series(String title,String type, List<String> directors, List<String> actors,
                  List<Genre> genres, List<Rating> ratings, String plot,
                  double averageRating, Long releaseYear, Long numSeasons,
                  Map<String, List<Episode>> seasons) {
        super(title,type, directors, actors, genres, ratings, plot, averageRating);
        this.releaseYear = releaseYear;
        this.numSeasons = numSeasons;
        this.seasons = seasons;
    }
    public Series() { }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Release Year: " + getReleaseYear());
        System.out.println("Number of Seasons: " + getNumSeasons());

        for (Map.Entry<String, List<Episode>> seasonEntry : getSeasons().entrySet()) {
            System.out.println("Season " + seasonEntry.getKey() + ":");
            List<Episode> episodes = seasonEntry.getValue();
            for (Episode episode : episodes) {
                System.out.println("  Episode: " + episode.getEpisodeName());
                System.out.println("    Duration: " + episode.getDuration() + " minutes");
                System.out.println("    Additional Details: " + episode.getAdditionalDetails());
            }
        }
    }
    public Long getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Long releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Long getNumSeasons() {
        return numSeasons;
    }

    public void setNumSeasons(Long numSeasons) {
        this.numSeasons = numSeasons;
    }

    public Map<String, List<Episode>> getSeasons() {
        return seasons;
    }

    public void setSeasons(Map<String, List<Episode>> seasons) {
        this.seasons = seasons;
    }


}
