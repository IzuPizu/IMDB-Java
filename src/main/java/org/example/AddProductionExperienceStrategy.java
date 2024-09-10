package org.example;

public class AddProductionExperienceStrategy implements ExperienceStrategy {
    @Override
    public int calculateExperience() {
        // Specify the experience points for adding a production
        return 10;
    }
}