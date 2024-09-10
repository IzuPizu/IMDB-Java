package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public abstract class User<T extends Comparable<T>> implements Observer, Subject {
    private Information information;
    private AccountType accountType;
    private String username;
    private int experience;
    private List<String> notifications;
    private SortedSet<T> favorites;
    private boolean loggedIn = false;
    private SortedSet<Actor> actorFavorites;
    private SortedSet<Production> productionFavorites;
    private ProductionComparator productionComparator;


    public User(Information information, AccountType accountType, String username, int experience, List<String> notifications, SortedSet<T> favorites) {
        this.information = information;
        this.accountType = accountType;
        this.username = username;
        this.experience = experience;
        this.notifications = notifications;
        this.favorites = new TreeSet<>(productionComparator);
        this.productionFavorites = new TreeSet<>(new ProductionComparator());
        this.actorFavorites = new TreeSet<>(new ProductionComparator());
    }


    // BUILT
    protected static class Information {
        private Credentials credentials;
        private String name;
        private String country;
        private int age;
        private char gender;
        private LocalDate birthDate;

        //Constructor
        private Information(InformationBuilder builder){
            this.credentials = builder.credentials;
            this.name = builder.name;
            this.country = builder.country;
            this.age = builder.age;
            this.gender = builder.gender;
            this.birthDate = builder.birthDate;
        }

        public Credentials getCredentials() {
            return credentials;
        }

        public String getName() {
            return name;
        }

        public String getCountry() {
            return country;
        }

        public int getAge() {
            return age;
        }

        public char getGender() {
            return gender;
        }

        public LocalDate getBirthDate() {
            return birthDate;
        }

        // BUILDER
        public static class InformationBuilder {
            private Credentials credentials;
            private String name;
            private String country;
            private int age;
            private char gender;
            private LocalDate birthDate;

            public InformationBuilder(Credentials credentials, String name, String country, int age, char gender, LocalDate birthDate) {
                this.credentials = credentials;
                this.name = name;
                this.country = country;
                this.age = age;
                this.gender = gender;
                this.birthDate = birthDate;
            }

            public InformationBuilder(){

            }

            public Information build() {
                return new Information(this);
            }


            public InformationBuilder setCredentials(Credentials credentials) {
                this.credentials = credentials;
                return this;
            }

            public InformationBuilder setName(String name) {
                this.name = name;
                return this;
            }

            public InformationBuilder setCountry(String country) {
                this.country = country;
                return this;
            }

            public InformationBuilder setAge(int age) {
                this.age = age;
                return this;
            }

            public InformationBuilder setGender(char gender) {
                this.gender = gender;
                return this;
            }

            public InformationBuilder setBirthDate(LocalDate birthDate) {
                this.birthDate = birthDate;
                return this;
            }

            public InformationBuilder parseBirthDate(String birthDate) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                this.birthDate = LocalDate.parse(birthDate, formatter);
                return this;
            }

        }

    }

    private ExperienceStrategy experienceStrategy;

    public void setExperienceStrategy(ExperienceStrategy experienceStrategy) {
        this.experienceStrategy = experienceStrategy;
    }

    public void performAction() {
        int experienceGained = experienceStrategy.calculateExperience();
        updateExperience(experienceGained);
    }



    private List<Observer> observers = new ArrayList<>();

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
            update(message);
        }
    }

    @Override
    public void update(String message) {
        notifications.add(message);
    }


    private String generateUniqueUsername(String name) {
        return ""; // Placeholder, implementați logica reală
    }

    private Credentials generateStrongPassword() {
        return new Credentials.Builder("example@example.com", "strongPassword").build();
    }


    //METHODS

    @Override
    public String toString() {
        return "User{" +
                "information=" + information +
                ", accountType=" + accountType +
                ", username='" + username + '\'' +
                ", experience=" + experience +
                ", notifications=" + notifications +
                ", favorites=" + favorites +
                ", experienceStrategy=" + experienceStrategy +
                ", observers=" + observers +
                '}';
    }

    public Information getInformation() {
        return information;
    }


    public SortedSet<T> getFavorites() {
        return favorites;
    }

    public void setFavorites(SortedSet<T> favorites) {
        this.favorites = favorites;
    }


     public void addFavorites(T item) {
        if(item != null)
            favorites.add(item);
    }

    public void removeFavorite(T favorite){
        if(favorite != null){
            favorites.remove(favorite);
        }
    }

    public void printFavorites(SortedSet<T> favorites) {
        for (T favorite : favorites) {
            System.out.println(favorite.toString());
        }
    }

    public void setInformation(Information information) {
        this.information = information;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public List<String> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<String> notifications) {
        this.notifications = notifications;
    }

    public ExperienceStrategy getExperienceStrategy() {
        return experienceStrategy;
    }

    public List<Observer> getObservers() {
        return observers;
    }

    public ProductionComparator getProductionComparator() {
        return productionComparator;
    }

    public void setProductionComparator(ProductionComparator productionComparator) {
        this.productionComparator = productionComparator;
    }

    public void setObservers(List<Observer> observers) {
        this.observers = observers;
    }

    public SortedSet<Actor> getActorFavorites() {
        return actorFavorites;
    }

    public void setActorFavorites(SortedSet<Actor> actorFavorites) {
        this.actorFavorites = actorFavorites;
    }

    public SortedSet<Production> getProductionFavorites() {
        return productionFavorites;
    }

    public void setProductionFavorites(SortedSet<Production> productionFavorites) {
        this.productionFavorites = productionFavorites;
    }
    public void updateExperience(int newExperience) {
        this.experience = newExperience;
        System.out.println("User " + getUsername() + " gained " + newExperience + " experience points.");
    }


    public void logout() {
        loggedIn = false;
    }
    
    public boolean isLoggedIn() {
        return loggedIn;
    }
}
