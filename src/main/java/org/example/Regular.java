package org.example;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

public class Regular<T extends Comparable<T>> extends User implements  RequestsManager {
    private List<Request> requests = new ArrayList<>();
    private ExperienceStrategy experienceStrategy;
    public Regular(User.Information information, AccountType accountType, String username,
                   int experience, List<String> notifications, SortedSet<T> favorites) {
        super(information, accountType, username, experience, notifications, favorites);
    }


    @Override
    public void createRequest(Request r) {
        requests.add(r);

    }

    @Override
    public void removeRequest(Request r) {
        requests.remove(r);
      
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void addReview(Production production, int ratingValue, String comment) {
        if (hasRated(production)) {
            System.out.println("You have already rated this production. You can edit your rating, but no experience points will be awarded.");
        } else {
            // Create the rating and add it to the production
            Rating rating = new Rating(getUsername(), ratingValue, comment);
            production.addRating(rating);

            // Set the appropriate strategy for adding a review
            this.experienceStrategy = new ReviewExperienceStrategy();
            // Award experience points based on the strategy
            int experiencePoints = experienceStrategy.calculateExperience();
            //If the production was not previously rated, update experience points and set boolean
            if(!production.isPreviouslyRated()) {
                updateExperience(getExperience() + experiencePoints);
                production.setPreviouslyRated(true);
            }
            //Notify the user
            rating.notifyObservers(getUsername() + " has added a new rating for the production: " + rating.toString());

            // Notify the user if the production receives another rating
            production.evaluateProduction(this, ratingValue);

        }
    }

    public void deleteReview(Production production) {
        // Check if the user has rated this production
        if (hasRated(production)) {
            // Remove the rating from the production
            production.removeRating(getUsername());
            // Notify the user if the production receives another rating
            production.evaluateProduction(this, 0);
            System.out.println("Review deleted successfully.");
        } else {
            System.out.println("You haven't rated this production.");
        }
    }

    private void removeFavorite(Production production) {
    }


    private boolean hasRated(Production production) {
        return production != null && production.hasUserRated(getUsername());
    }
    private boolean hasRatedBefore(Production production) {
        return production != null && production.hasUserRated(getUsername());
    }

}
