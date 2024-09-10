package org.example;

public class Episode {


    private String episodeName;
    private String duration;
    private String additionalDetails; 

    public Episode(String episodeName,String durationMinutes) {
        this.episodeName = episodeName;
        this.duration = durationMinutes;
    }

    public Episode() { }

    public String getEpisodeName() {
        return episodeName;
    }

    public String getDuration() {
        return duration;
    }

    public String getAdditionalDetails() {
        return additionalDetails;
    }
}
