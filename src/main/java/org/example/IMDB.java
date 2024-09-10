package org.example;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.lang.Comparable;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/*  Class contains the extracted information from json files */
public class IMDB<T extends Comparable<T>> {
    private boolean firstRun = true;
    private static IMDB instance;
    private List<Regular> regularList;
    private List<Contributor> contributorList;
    private List<Admin> adminList;
    private List<Actor> actors;
    private List<Request> requests;
    private List<Production> productions;
    public User authenticatedUser;
    private ProductionComparator productionComparator;



    private void startGraphicalInterface() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                IMDBGUI imdbGUI = new IMDBGUI();
            }
        });
    }
    public void selectMode() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose the mode of operation:");
        System.out.println("1) Graphical Interface");
        System.out.println("2) Terminal");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                startGraphicalInterface();
                System.out.println("Graphical Interface mode selected.");
                break;
            case 2:
                IMDB.getInstance().run();
                System.out.println("Terminal mode selected.");
                break;
            default:
                System.out.println("Invalid option. Defaulting to Terminal mode.");
                break;
        }
    }
    private IMDB() {
        regularList = new ArrayList<>();
        contributorList = new ArrayList<>();
        adminList = new ArrayList<>();
        requests = new ArrayList<>();
        productions = new ArrayList<>();
        actors = new ArrayList<>();
        initializeData();
    }

    private void initializeData() {
        if(firstRun){
            parseActorsJson("C:\\Users\\User1\\JavaIMDB\\POO-Tema-2023-checker\\src\\test\\resources\\testResources\\actors.json");
            parseRequestsJson("C:\\Users\\User1\\JavaIMDB\\POO-Tema-2023-checker\\src\\test\\resources\\testResources\\requests.json");
            parseProductionsJson("C:\\Users\\User1\\JavaIMDB\\POO-Tema-2023-checker\\src\\test\\resources\\testResources\\production.json");
            parseAccountsJson("C:\\Users\\User1\\JavaIMDB\\POO-Tema-2023-checker\\src\\test\\resources\\testResources\\accounts.json");
            firstRun = false;  // Setăm indicatorul pentru a nu mai intra în acest bloc la rulările ulterioare
        }
    }
    public static IMDB getInstance() {
        if (instance == null) {
            instance = new IMDB();

        }
        return instance;
    }
    public void run() {
        initializeData();
        // Authentication
        authenticatedUser = authenticateUser();
        while (authenticatedUser == null) {
            System.out.println("Authentication failed. Please try again.");
            authenticatedUser = authenticateUser();
        }

        // Display options based on user role
        displayOptions(authenticatedUser);

    }

    public boolean isValidUser(String email ,String password){
        for (User user : regularList) {
            if (user.getInformation().getCredentials().getEmail().equals(email)
                    && user.getInformation().getCredentials().getPassword().equals(password)) {

                return true;
            }
        }

        for (User user : contributorList) {
            if (user.getInformation().getCredentials().getEmail().equals(email)
                    && user.getInformation().getCredentials().getPassword().equals(password)) {
                System.out.println("Welcome back user " + user.getUsername() + "!" );
                System.out.println("Username:" + user.getUsername() );
                System.out.println("User experience:" + user.getExperience());

                return true;
            }
        }

        for (User user : adminList) {
            if (user.getInformation().getCredentials().getEmail().equals(email)
                    && user.getInformation().getCredentials().getPassword().equals(password)) {
                System.out.println("Welcome back user " + user.getUsername() + "!" );
                System.out.println("Username:" + user.getUsername() );
                System.out.println("User experience:" + user.getExperience());

                return true;
            }
        }

        return false;
    }
    public User authenticateUser() {
        System.out.println("Welcome back! Enter your credentials!");

        Scanner scanner = new Scanner(System.in);
        System.out.print("email: ");
        String email = scanner.nextLine();
        System.out.print("password: ");
        String password = scanner.nextLine();

        for (User user : regularList) {
            if (user.getInformation().getCredentials().getEmail().equals(email)
                    && user.getInformation().getCredentials().getPassword().equals(password)) {
                System.out.println("Welcome back user " + user.getUsername() + "!" );
                System.out.println("Username:" + user.getUsername() );
                System.out.println("User experience:" + user.getExperience());

                return user;
            }
        }

        for (User user : contributorList) {
            if (user.getInformation().getCredentials().getEmail().equals(email)
                    && user.getInformation().getCredentials().getPassword().equals(password)) {
                System.out.println("Welcome back user " + user.getUsername() + "!" );
                System.out.println("Username:" + user.getUsername() );
                System.out.println("User experience:" + user.getExperience());

                return user;
            }
        }

        for (User user : adminList) {
            if (user.getInformation().getCredentials().getEmail().equals(email)
                    && user.getInformation().getCredentials().getPassword().equals(password)) {
                System.out.println("Welcome back user " + user.getUsername() + "!" );
                System.out.println("Username:" + user.getUsername() );
                System.out.println("User experience: - ");

                return user;
            }
        }

        return null;
    }

    private void displayOptions(User user) {
        // Display options based on user role
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose action:");
        if (user instanceof Regular) {
            System.out.println("1) View productions details");
            System.out.println("2) View actors details");
            System.out.println("3) View notifications");
            System.out.println("4) Search for actor/movie/series");
            System.out.println("5) Add/Delete actor/movie/series to/from favorites");
            System.out.println("6) Create/Delete request");
            System.out.println("7) Add/Delete review for movie/series");
            System.out.println("8) Logout");
        } else if (user instanceof Contributor) {
            System.out.println("1) View productions details");
            System.out.println("2) View actors details");
            System.out.println("3) View notifications");
            System.out.println("4) Search for actor/movie/series");
            System.out.println("5) Add/Delete actor/movie/series to/from favorites");
            System.out.println("6) Create/Delete request");
            System.out.println("7) Add/Delete actor/movie/series from system");
            System.out.println("8) Solve a request");
            System.out.println("9) Update Movie Details");
            System.out.println("10) Update Actor Details");
            System.out.println("11) Logout");
        } else if (user instanceof Admin) {
            System.out.println("1) View productions details");
            System.out.println("2) View actors details");
            System.out.println("3) View notifications");
            System.out.println("4) Search for actor/movie/series");
            System.out.println("5) Add/Delete actor/movie/series to/from favorites");
            System.out.println("6) Add/Delete actor/movie/series from system");
            System.out.println("7) Solve a request");
            System.out.println("8) Update Movie Details");
            System.out.println("9) Update Actor Details");
            System.out.println("10) Add/Delete user");
            System.out.println("11) Logout");

        }

        // Get user input
        int chosenOption = scanner.nextInt();
        // Perform action based on the chosen option
        execute(user, chosenOption);
    }


    private void execute(User user, int chosenOption) {
        switch (chosenOption) {
            case 1:
                System.out.println("Visualize productions details");
                viewProductionsDetails();
                break;
            case 2:
                System.out.println("View actors details:");
                viewActorsDetails();
                break;
            case 3:
                System.out.println("View notifications");
                System.out.println(authenticatedUser.getNotifications());
                break;
            case 4:
                System.out.println("Search for actor/movie/series:");
                searchForActorMovieSeries();
                break;
            case 5:
                System.out.println("Add/Delete actor/movie/series to/from favorites:");
                addDeleteToFromFavorites();
                break;
            case 6:
                if(authenticatedUser.getAccountType().equals(AccountType.Regular) ||
                        authenticatedUser.getAccountType().equals(AccountType.Contributor) ){
                    System.out.println("Create/Delete request");
                    createDeleteRequest();

                }else if(authenticatedUser.getAccountType().equals(AccountType.Admin)){
                    System.out.println("Add/Delete actor/movie/series from system");
                    addDeleteFromSystem();
                }
                break;
            case 7:
                if(authenticatedUser.getAccountType().equals(AccountType.Regular)) {
                    System.out.println("Add/Delete review for movie/series");
                    addDeleteReview();
                }else if(authenticatedUser.getAccountType().equals(AccountType.Contributor)) {
                    System.out.println("Add/Delete actor/movie/series from system");
                    addDeleteFromSystem();
                } else if (authenticatedUser.getAccountType().equals(AccountType.Admin)) {
                    System.out.println("Solve a request");
                    solveRequest();
                }
                break;
            case 8:
                if(authenticatedUser.getAccountType().equals(AccountType.Regular)) {
                    System.out.println("Logout:");
                    logout();
                }else if(authenticatedUser.getAccountType().equals(AccountType.Contributor)) {
                    System.out.println("Solve a request");
                    solveRequest();
                } else if (authenticatedUser.getAccountType().equals(AccountType.Admin)) {
                    System.out.println("Update Movie Details");
                    updateProduction();
                }
                break;
            case 9:
                if(authenticatedUser.getAccountType().equals(AccountType.Contributor)) {
                    System.out.println("Update Movie Details");
                    updateProduction();

                } else if (authenticatedUser.getAccountType().equals(AccountType.Admin)) {
                    System.out.println("Update Actor Details");
                    updateActor();
                }
                break;
            case 10:
                if(authenticatedUser.getAccountType().equals(AccountType.Contributor)) {
                    System.out.println("Update Actor Details");
                    updateActor();
                } else if (authenticatedUser.getAccountType().equals(AccountType.Admin)) {
                    System.out.println("Add/Delete User");
                    addDeleteUser();
                }
                break;
            case 11:
                if(authenticatedUser.getAccountType().equals(AccountType.Contributor)) {
                    System.out.println("Logout:");
                    logout();
                } else if (authenticatedUser.getAccountType().equals(AccountType.Admin)) {
                    System.out.println("Logout:");
                    logout();
                }
                break;
            default:
                System.out.println("Invalid option. Please choose a valid option.");
                break;
        }
        //The options has been executed -> display options again
        displayOptions(authenticatedUser);
    }


    // Read and parse Json files
    public void parseActorsJson(String jsonfile) {
        JSONParser parser = new JSONParser();
        try {
            //Read JSON file
            Object object = parser.parse(new FileReader(jsonfile));
            //Convert the object to JSONObject
            JSONArray jsonActorsArray = (JSONArray) object;
            // Process each actor in the array
            for (Object act : jsonActorsArray) {
                JSONObject actorObj = (JSONObject) act;
                String actorName = (String) actorObj.get("name");
                String actorBiography = (String) actorObj.get("biography");
                // Process performances
                JSONArray performancesArray = (JSONArray) actorObj.get("performances");
                List<Actor.Performance> performances = new ArrayList<>();

                // Process each performance in the array
                for (Object perf : performancesArray) {
                    JSONObject perfObj = (JSONObject) perf;
                    String title = (String) perfObj.get("title");
                    String type = (String) perfObj.get("type");

                    // Create Performance object and add to the performances list
                    Actor.Performance performance = new Actor.Performance(title, type);
                    performances.add(performance);
                }
                // Create Actor object and add to the actorList
                Actor actor = new Actor(actorName, performances, actorBiography);
                actors.add(actor);

            }
        }catch (IOException e){
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void parseRequestsJson(String jsonfile) {
        JSONParser parser = new JSONParser();
        try {
            //Read JSON file
            Object object = parser.parse(new FileReader(jsonfile));
            //Convert the object to JSONObject
            JSONArray jsonRequestArray = (JSONArray) object;
            // Process each request
            for (Object req : jsonRequestArray) {
                JSONObject requestObj = (JSONObject) req;
                String typeString = (String) requestObj.get("type");
                RequestType type = RequestType.valueOf(typeString);

                String dateStr = (String) requestObj.get("createdDate");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                //LocalDateTime date = null;
                LocalDateTime createdDate = LocalDateTime.parse(dateStr, formatter);
                String username = (String) requestObj.get("username");
                String to = (String) requestObj.get("to");
                String description = (String) requestObj.get("description");
                String actorName = null;
                String movieTitle = null;
                Request request = null;
                if(requestObj.containsKey("actorName")){
                   actorName = (String) requestObj.get("actorName");
                     request = new Request(type,createdDate,actorName,null,description,username,to,authenticatedUser);

                }else if (requestObj.containsKey("movieTitle")){
                    movieTitle = (String) requestObj.get("movieTitle");
                    request = new Request(type,createdDate,null,movieTitle,description,username,to,authenticatedUser);

                }else{
                    request = new Request(type,createdDate,null,null,description,username,to,authenticatedUser);
                }
                requests.add(request);



            }
        }catch (IOException e){
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


    }

    public void parseAccountsJson(String jsonfile) {
        JSONParser parser = new JSONParser();
        User user;
        try {
            // Read JSON file
            Object object = parser.parse(new FileReader(jsonfile));
            // Convert the object to JSONArray
            JSONArray jsonAccountsArray = (JSONArray) object;

            // Get the accounts data
            for (Object acc : jsonAccountsArray) {
                // Data for all types of users
                JSONObject account = (JSONObject) acc;
                String username = (String) account.get("username");
                //String experience = (String) account.get("experience");
                String userType = (String) account.get("userType");
                String experienceString = (String) account.get("experience");
                int experience = experienceString != null ? Integer.parseInt(experienceString) : 0;
                JSONArray notificationsArray = (JSONArray) account.get("notifications");
                List<String> notificationsList = notificationsArray != null ? parseStringList(notificationsArray) : new ArrayList<>();

                // Parse the information object
                JSONObject informationJson = (JSONObject) account.get("information");

                User.Information information = new User.Information.InformationBuilder()
                        .setCredentials(parseCredentials((JSONObject) informationJson.get("credentials")))
                        .setName((String) informationJson.get("name"))
                        .setCountry((String) informationJson.get("country"))
                        .setAge(Integer.parseInt(String.valueOf(informationJson.get("age"))))
                        .setGender(((String) informationJson.get("gender")).charAt(0))
                        .parseBirthDate((String) informationJson.get("birthDate")).build();

                //Get actorsContribution, productionsContribution
                JSONArray actorsContributionArray = (JSONArray) account.get("actorsContribution");
                JSONArray productionsContributionArray = (JSONArray) account.get("productionsContribution");

                SortedSet<T> actorContributions = new TreeSet<>(new ProductionComparator());
                SortedSet<T> productionContributions = new TreeSet<>(new ProductionComparator());

                if(productionsContributionArray != null) {
                    for (Object productionName : productionsContributionArray) {
                        Production production = getProductionByName((String) productionName);
                        if (production != null) {
                          // Add production contribution to the Staff
                         productionContributions.add((T) production);
                        }
                    }
                }
                if(actorsContributionArray != null) {
                    for (Object actorName : actorsContributionArray) {
                        Actor actor = getActorByName((String) actorName);
                        if (actor != null) {
                            // Add production contribution to the Staff
                            actorContributions.add((T)actor);
                        }
                    }
                }

                SortedSet<T> contributions = new TreeSet<>(new ProductionComparator());
                contributions.addAll(productionContributions);
                contributions.addAll(actorContributions);

                //Get favoriteProductions , favoriteActors -> favorites
                JSONArray favoriteProductionsArray = (JSONArray) account.get("favoriteProductions");
                JSONArray favoriteActorsArray = (JSONArray) account.get("favoriteActors");

                SortedSet<T> actorFavorites = new TreeSet<>(new ProductionComparator());
                SortedSet<T> productionFavorites = new TreeSet<>(new ProductionComparator());
                // Populate favoriteProductions in the favorites set
                if(favoriteProductionsArray != null) {
                    for (Object productionName : favoriteProductionsArray) {
                        Production production = getProductionByName((String) productionName);
                        if (production != null) {
                         productionFavorites.add((T)production);
                        }
                    }
                }
                // Populate favoriteActors in the favorites set
                if(favoriteActorsArray != null) {
                    for (Object actorName : favoriteActorsArray) {
                        Actor actor = getActorByName((String) actorName);
                        if (actor != null) {
                            actorFavorites.add((T) actor);
                        }
                    }
                }
                SortedSet<T> favorites = new TreeSet<>(new ProductionComparator());
                favorites.addAll(actorFavorites);
                favorites.addAll(productionFavorites);

                // Determine the user type and create the corresponding object using UserFactory
                AccountType accountType = AccountType.valueOf(userType); // Use the enum valueOf method
                user = UserFactory.createUser(information,accountType, username, experience, notificationsList,favorites);
                user.setFavorites(favorites);

                if (user instanceof Contributor) {
                    ((Contributor<T>) user).setContributions(contributions);
                } else if (user instanceof Admin) {
                    ((Admin<T>) user).setContributions(contributions);
                }

                switch (accountType) {
                    case Regular:
                        addRegular((Regular) user);
                        break;
                    case Contributor:
                        addContributor((Contributor) user);
                        break;
                    case Admin:
                        addAdmin((Admin) user);
                        break;
                }

            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }


    private Actor getActorByName(String actorName) {
        for (Actor actor : actors) {
            if (actor.getName().equals(actorName)) {
                return actor;
            }
        }
        return null;
    }
    public Production getProductionByName(String productionName) {
        for (Production production : productions) {
            if (production.getTitle().equals(productionName)) {
                return production;
            }
        }
        return null;
    }

    private Credentials parseCredentials(JSONObject credentialsJson) {
        return new Credentials.Builder(
                (String) credentialsJson.get("email"),
                (String) credentialsJson.get("password"))
                .build();
    }
    private LocalDate parseBirthDate(String birthDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(birthDate, formatter);
    }

    private List<String> parseStringList(JSONArray jsonArray) {
        List<String> list = new ArrayList<>();
        for (Object item : jsonArray) {
            list.add((String) item);
        }
        return list;
    }

    public void parseProductionsJson(String jsonfile) {
        JSONParser parser = new JSONParser();
        try {
            // Read JSON file
            Object object = parser.parse(new FileReader(jsonfile));
            // Convert the object to JSONArray
            JSONArray jsonProductionsArray = (JSONArray) object;
            // Process each production in the array
            for (Object production : jsonProductionsArray) {
                JSONObject productionObj = (JSONObject) production;

                // Extract basic information
                String productionTitle = (String) productionObj.get("title");
                String productionType = (String) productionObj.get("type");

                // Extract directors
                JSONArray directorsArray = (JSONArray) productionObj.get("directors");
                List<String> directors = new ArrayList<>();
                for (Object director : directorsArray) {
                    directors.add((String) director);
                }

                // Extract actors
                JSONArray actorsArray = (JSONArray) productionObj.get("actors");
                List<String> actorsExtracted = new ArrayList<>();
                for (Object actor : actorsArray) {
                    String actorNameStr = (String) actor;
                    Actor existingActor = getActorByName(actorNameStr);

                    // If the actor doesn't exist, create a new Actor object and add it to the list
                    if (existingActor == null) {
                        List<Actor.Performance> performances = new ArrayList<>();
                        Actor.Performance performance = new Actor.Performance(productionTitle, productionType);
                        performances.add(performance);
                        Actor newActor = new Actor(actorNameStr,performances, "Biography not available");
                        actors.add(newActor);
                        actorsExtracted.add(newActor.getActorName());
                        // Add to admin's responsability
                        newActor.setUnderAdminResponsibility(true);
                    } else {
                        actorsExtracted.add(existingActor.getActorName());
                    }

                }
                // Extract genres
                JSONArray genresArray = (JSONArray) productionObj.get("genres");
                List<Genre> genres = new ArrayList<>();
                for (Object genreObj : genresArray) {
                    String genreString = (String) genreObj;
                    Genre genre = Genre.valueOf(genreString);
                    genres.add(genre);
                }

                // Extract ratings
                JSONArray ratingsArray = (JSONArray) productionObj.get("ratings");
                List<Rating> ratings = new ArrayList<>();
                if (ratingsArray != null) {
                    for (Object ratingObj : ratingsArray) {
                        JSONObject ratingJson = (JSONObject) ratingObj;
                        String username = (String) ratingJson.get("username");
                        long ratingValue = (long) ratingJson.get("rating");
                        String comment = (String) ratingJson.get("comment");
                        Rating rating = new Rating(username, (int) ratingValue, comment);
                        ratings.add(rating);
                    }
                }

                // Extract plot, average rating, duration, release year
                String plot = (String) productionObj.get("plot");
                double averageRating = (double) productionObj.get("averageRating");

                // Additional extraction for series
                if (productionType.equals("Series")) {
                    Long numSeasons = (Long) productionObj.get("numSeasons");
                    JSONObject seasonsObj = (JSONObject) productionObj.get("seasons");
                    Long releaseYear = (Long) productionObj.get("releaseYear");
                    Map<String, List<Episode>> seasons = parseSeasons(seasonsObj);
                    // Create a Series object and store the extracted information
                    Series series = new Series(productionTitle,productionType ,directors, actorsExtracted, genres, ratings,
                            plot, averageRating, releaseYear, numSeasons, seasons);
                    productions.add(series);
                } else {
                    // Extract duration and release year for movies
                    String duration = (String) productionObj.get("duration");
                    Long releaseYear = (Long) productionObj.get("releaseYear");
                    // Create a Movie object and store the extracted information
                    Movie movie = new Movie(productionTitle, productionType,directors, actorsExtracted, genres, ratings,
                            plot, averageRating, duration, releaseYear);
                    productions.add(movie);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }

    private Map<String, List<Episode>> parseSeasons(JSONObject seasonsObj) {
        Map<String, List<Episode>> seasons = new HashMap<>();
        for (Object seasonNameObj : seasonsObj.keySet()) {
            String seasonName = (String) seasonNameObj;
            JSONArray episodesArray = (JSONArray) seasonsObj.get(seasonName);
            List<Episode> episodes = new ArrayList<>();
            for (Object episodeObj : episodesArray) {
                JSONObject episodeJson = (JSONObject) episodeObj;
                String episodeName = (String) episodeJson.get("episodeName");
                String episodeDuration = (String) episodeJson.get("duration");
                Episode episode = new Episode(episodeName, episodeDuration);
                episodes.add(episode);
            }
            seasons.put(seasonName, episodes);
        }
        return seasons;
    }


    public void addRegular(Regular regular) {
        regularList.add(regular);
    }

    public void addContributor(Contributor contributor) {
        contributorList.add(contributor);
    }

    public void addAdmin(Admin admin) {
        adminList.add(admin);
    }

    public void addActor(Actor actor) {
        actors.add(actor);
    }

    public void addRequest(Request request) {
        requests.add(request);
    }

    public void addProduction(Production production) {
        productions.add(production);
    }

    public static void setInstance(IMDB instance) {
        IMDB.instance = instance;
    }

    public List<Regular> getRegularList() {
        return regularList;
    }

    public void setRegularList(List<Regular> regularList) {
        this.regularList = regularList;
    }

    public List<Contributor> getContributorList() {
        return contributorList;
    }

    public void setContributorList(List<Contributor> contributorList) {
        this.contributorList = contributorList;
    }

    public List<Admin> getAdminList() {
        return adminList;
    }

    public void setAdminList(List<Admin> adminList) {
        this.adminList = adminList;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    public List<Production> getProductions() {
        return productions;
    }

    public void setProductions(List<Production> productions) {
        this.productions = productions;
    }


    public User getAuthenticatedUser(){
        return authenticatedUser;
    }


    //Methods for executing options
    private void viewProductionsDetails(){
        System.out.println("1) All Productions");
        System.out.println("2) Filter by Genre");
        System.out.println("3) Filter by Number of Reviews");

        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        switch (option) {
            case 1:
                getProductions().forEach(production -> {
                    production.getRatings().sort((r1, r2) -> {
                        int exp1 = findUserByUsername(r1.getUsername()).getExperience();
                        int exp2 = findUserByUsername(r2.getUsername()).getExperience();
                        return Integer.compare(exp2, exp1);
                    });
                    System.out.println(production);
                });
                break;
            case 2:
                // Filter by Genre
                System.out.println("Enter genre to filter by:");
                scanner.nextLine(); // Consume the newline character
                String genreInput = scanner.nextLine();
                Genre filterGenre = Genre.valueOf(genreInput);

                List<Production> filteredByGenre = productions.stream()
                        .filter(p -> p.getGenres().contains(filterGenre))
                        .peek(production -> {
                            production.getRatings().sort((r1,r2)->{
                                int exp1 = findUserByUsername(r1.getUsername()).getExperience();
                                int exp2 = findUserByUsername(r2.getUsername()).getExperience();
                                return Integer.compare(exp2,exp1);
                            });
                        })
                        .collect(Collectors.toList());

                System.out.println("Productions filtered by genre " + genreInput + ":");
                filteredByGenre.forEach(System.out::println);
                break;
            case 3:
                // Filter by Number of Reviews
                System.out.println("Enter minimum number of reviews:");
                int minReviews = scanner.nextInt();

                List<Production> filteredByReviews = productions.stream()
                        .filter(p -> p.getRatings().size() >= minReviews)
                        .peek(production -> {
                            production.getRatings().sort((r1,r2)->{
                                int exp1 = findUserByUsername(r1.getUsername()).getExperience();
                                int exp2 = findUserByUsername(r2.getUsername()).getExperience();
                                return Integer.compare(exp2,exp1);
                            });
                        })
                        .collect(Collectors.toList());

                System.out.println("Productions with at least " + minReviews + " reviews:");
                filteredByReviews.forEach(System.out::println);
                break;
            default:
                System.out.println("Invalid option. Please choose a valid option.");
                break;
        }
    }

    private void viewActorsDetails(){
        System.out.println("1) View all actors details");
        System.out.println("2) Filter by name");

        Scanner newScanner = new Scanner(System.in);
        int newoption = newScanner.nextInt();
        switch (newoption) {
            case 1:
                actors.forEach(System.out::println);
                break;
            case 2:
                System.out.println("Enter name to filter by:");
                newScanner.nextLine(); // Consume the newline character
                String nameInput = newScanner.nextLine();
                String name = String.valueOf(nameInput);

                List<Actor> filteredByName = actors.stream()
                        .filter(p -> p.getName().contains(name))
                        .collect(Collectors.toList());

                System.out.println("Actors details filtered by name " + nameInput + ":");
                filteredByName.forEach(System.out::println);
                break;
        }
    }

    private void searchForActorMovieSeries(){
        System.out.println("1) Search for actor");
        System.out.println("2) Search for movie/series");

        Scanner searchScanner = new Scanner(System.in);
        int searchOption = searchScanner.nextInt();
        switch (searchOption) {
            case 1:
                // Search for actor
                System.out.println("Enter actor's name:");
                searchScanner.nextLine(); // Consume the newline character
                String actorNameInput = searchScanner.nextLine();
                String actorNameToSearch = actorNameInput.toLowerCase();

                List<Actor> matchingActors = actors.stream()
                        .filter(actor -> actor.getName().toLowerCase().contains(actorNameToSearch))
                        .collect(Collectors.toList());

                System.out.println("Matching actors for '" + actorNameInput + "':");
                matchingActors.forEach(System.out::println);
                break;

            case 2:
                // Search for movie/series
                System.out.println("Enter movie/series title:");
                searchScanner.nextLine(); // Consume the newline character
                String titleInput = searchScanner.nextLine();
                String titleToSearch = titleInput.toLowerCase();

                List<Production> matchingProductions = productions.stream()
                        .filter(production -> production.getTitle().toLowerCase().contains(titleToSearch))
                        .collect(Collectors.toList());

                System.out.println("Matching movies/series for '" + titleInput + "':");
                matchingProductions.forEach(System.out::println);
                break;

            default:
                System.out.println("Invalid option. Please choose a valid option.");
                break;
        }
    }

    private void addDeleteToFromFavorites(){
        System.out.println("1) Add to favorites");
        System.out.println("2) Delete from favorites");

        Scanner favoritesScanner = new Scanner(System.in);
        int favoritesOption = favoritesScanner.nextInt();

        switch (favoritesOption) {
            case 1:
                System.out.println("Enter the name of the actor/movie/series to add to favorites:");
                favoritesScanner.nextLine(); // Consume the newline character
                String favoriteName = favoritesScanner.nextLine();

                // Find the production or actor by name
                Production productionToAdd = getProductionByName(favoriteName);
                Actor actorToAdd = getActorByName(favoriteName);

                if (productionToAdd != null) {
                    authenticatedUser.addFavorites(productionToAdd);
                    System.out.println(favoriteName + " added to favorites.");

                } else if (actorToAdd != null) {
                    authenticatedUser.addFavorites(actorToAdd);
                    System.out.println(favoriteName + " added to favorites.");

                } else {
                    System.out.println("Production or actor not found with the name: " + favoriteName);
                }

                System.out.println("Favorite's list updated:");
                authenticatedUser.printFavorites(getAuthenticatedUser().getFavorites());
                break;
            case 2:
                // Get the list of favorites
                SortedSet<T> userFavorites = authenticatedUser.getFavorites();
                if (userFavorites.isEmpty()) {
                    System.out.println("Your favorites list is empty.");
                    break;
                }
                System.out.println("Select the item to delete from favorites:");
                // Display the list of favorites to the user
                int index = 1;
                for (T favorite : userFavorites) {
                    System.out.println(index + ". " + favorite);
                    index++;
                }
                // Prompt the user to enter the number corresponding to the item they want to delete
                int selection = favoritesScanner.nextInt();
                favoritesScanner.nextLine(); // Consume the newline character

                // Validate the user's selection
                if (selection >= 1 && selection <= userFavorites.size()) {
                    // Delete the selected item from favorites
                    T itemToDelete = null;
                    int currentIndex = 1;

                    for (T favorite : userFavorites) {
                        if (currentIndex == selection) {
                            itemToDelete = favorite;
                            break;
                        }
                        currentIndex++;
                    }

                    if (itemToDelete != null) {
                        authenticatedUser.removeFavorite(itemToDelete);
                        System.out.println(itemToDelete + " removed from favorites.");
                        System.out.println("Favorites list updated:");
                        authenticatedUser.printFavorites(authenticatedUser.getFavorites());
                    } else {
                        System.out.println("Error: Unable to find the selected item.");
                    }
                } else {
                    System.out.println("Invalid selection. Please choose a valid item number.");
                }

                break;
            default:
                System.out.println("Invalid option. Please choose a valid option.");
                break;
        }
    }

    private void createDeleteRequest(){
        System.out.println("1) Create request");
        System.out.println("2) Delete request");

        Scanner requestScanner = new Scanner(System.in);
        int requestOption = requestScanner.nextInt();
        String toResolver = null;
        String actorName = null;
        String movieTitle = null;

        switch (requestOption) {
            case 1:
                // Create request
                System.out.println("Choose the type of request:");
                System.out.println("1) DELETE_ACCOUNT");
                System.out.println("2) ACTOR_ISSUE");
                System.out.println("3) MOVIE_ISSUE");
                System.out.println("4) OTHERS");

                int requestTypeOption = requestScanner.nextInt();
                RequestType requestType = null;

                switch (requestTypeOption) {
                    case 1:
                        requestType = RequestType.DELETE_ACCOUNT;
                        toResolver = "ADMIN";
                        break;
                    case 2:
                        requestType = RequestType.ACTOR_ISSUE;
                        System.out.println("Enter the name of the actor:");
                        requestScanner.nextLine(); // Consume the newline character
                        actorName = requestScanner.nextLine();

                        // Search for the actor in the system
                        Actor actor = getActorByName(actorName);
                        if (actor != null) {
                            // Check if the actor is added by a Contributor
                            for (Contributor contributor : contributorList) {
                                if (contributor.getContributions().contains(actor) &&
                                        !contributor.getUsername().equals(authenticatedUser.getUsername())) {
                                    toResolver = contributor.getUsername();
                                    break;
                                }
                            }
                            // If not found among Contributors, check among Admins
                            if (toResolver == null) {
                                for (Admin admin : adminList) {
                                    if (admin.getContributions().contains(actor)) {
                                        toResolver = admin.getUsername();
                                        break;
                                    }
                                }
                            }
                        }
                        break;
                    case 3:
                        requestType = RequestType.MOVIE_ISSUE;
                        System.out.println("Enter the name of the production:");
                        requestScanner.nextLine(); // Consume the newline character
                        movieTitle = requestScanner.nextLine();
                        // Search for the production in the system
                        Production production = getProductionByName(movieTitle);
                        if (production != null) {
                            // Check if the production is added by a Contributor
                            for (Contributor contributor : contributorList) {
                                if (contributor.getContributions().contains(production) &&
                                !contributor.getUsername().equals(authenticatedUser.getUsername())) {
                                    toResolver = contributor.getUsername();
                                    break;
                                }
                            }
                            // If not found among Contributors, check among Admins
                            if (toResolver == null) {
                                for (Admin admin : adminList) {
                                    if (admin.getContributions().contains(production)) {
                                        toResolver = admin.getUsername();
                                        break;
                                    }
                                }
                            }
                        }
                        break;
                    case 4:
                        requestType = RequestType.OTHERS;
                        toResolver = "ADMIN";
                        break;
                    default:
                        System.out.println("Invalid request type. Please choose a valid option.");
                        break;
                }

                if (requestType != null) {
                    // Prompt the user for description
                    System.out.println("Enter the description of the request:");
                    requestScanner.nextLine(); // Consume the newline character
                    String description = requestScanner.nextLine();


                    // Create the request object
                    Request request = new Request(requestType, LocalDateTime.now(), actorName, movieTitle, description,
                            authenticatedUser.getUsername(), toResolver,authenticatedUser);

                    User resolver = null;
                    // Notify observers (users who created the request) about the new request
                    authenticatedUser.notifyObservers("You have created a new request: " + request.getDescription());

                    //Add the request to the creator's list
                    if(authenticatedUser instanceof Regular){
                        ((Regular)authenticatedUser).createRequest(request);
                    }else if(authenticatedUser instanceof Contributor){
                        ((Contributor)authenticatedUser).createRequest(request);
                    }

                    // Add the request to the appropriate list of resolvers
                    if (requestType.equals(RequestType.ACTOR_ISSUE)) {
                        Actor actor = getActorByName(actorName);
                        resolver = findUserForActor(actor);

                    }else if(requestType.equals(RequestType.MOVIE_ISSUE)){
                        Production production = getProductionByName(movieTitle);
                        resolver = findUserForProduction(production);

                    } else  {
                        //Add request to shared list among admins
                        RequestsHolder.addRequest(request);
                    }

                    if (resolver != null) {
                        if (resolver instanceof Contributor) {
                            Contributor contributorResolver = (Contributor) resolver;
                            contributorResolver.createRequest(request);
                            contributorResolver.update("You have received a new request: " + request.getDescription());
                        } else if (resolver instanceof Admin) {
                            Admin adminResolver = (Admin) resolver;
                            adminResolver.getRequests().add(request);
                            // Notify Contributor/Admin who received the request
                            adminResolver.update("You have received a new request: " + request.getDescription());
                        }

                    }

                    System.out.println("Request created successfully.");
                }
                break;
            case 2:
                // Delete request
                // Display the list of available requests
                System.out.println("Select the request to delete:");
                // Check if authenticatedUser is an instance of Contributor/Regular
                if(authenticatedUser instanceof Contributor){
                    Contributor contributorResolver = (Contributor) authenticatedUser;
                    List<Request> contributorRequests = contributorResolver.getRequests();
                    if (!contributorRequests.isEmpty()) {
                        for (int i = 0; i < contributorRequests.size(); i++) {
                            System.out.println((i + 1) + ") " + contributorRequests.get(i).getType() +
                                    " - " + contributorRequests.get(i).getDescription());
                        }
                        int deleteRequestOption = requestScanner.nextInt();

                        if (deleteRequestOption >= 1 && deleteRequestOption <= contributorRequests.size()) {
                            Request requestToDelete = contributorRequests.get(deleteRequestOption - 1);

                            // Remove the request
                            contributorResolver.removeRequest(requestToDelete);
                            System.out.println("Request deleted successfully.");
                        } else {
                            System.out.println("Invalid option. Please choose a valid request to delete.");
                        }

                    }else {
                        System.out.println("No requests to delete.");
                    }
                }

                if (authenticatedUser instanceof Regular) {

                    Regular regularResolver = (Regular) authenticatedUser;
                    List<Request> regularRequests = regularResolver.getRequests();

                    if (!regularRequests.isEmpty()) {
                        for (int i = 0; i < regularRequests.size(); i++) {
                            System.out.println((i + 1) + ") " + regularRequests.get(i).getType() +
                                    " - " + regularRequests.get(i).getDescription());
                        }

                        int deleteRequestOption = requestScanner.nextInt();

                        if (deleteRequestOption >= 1 && deleteRequestOption <= regularRequests.size()) {
                            Request requestToDelete = regularRequests.get(deleteRequestOption - 1);

                            // Remove the request
                            regularResolver.removeRequest(requestToDelete);
                            System.out.println("Request deleted successfully.");
                        } else {
                            System.out.println("Invalid option. Please choose a valid request to delete.");
                        }
                    } else {
                        System.out.println("No requests to delete.");
                    }
                }
                break;
            default:
                System.out.println("Invalid option. Please choose a valid option.");
                break;
        }
    }

    private User findUserForProduction(Production production) {
        for (Contributor contributor : contributorList) {
            if (contributor.getContributions().contains(production)) {
                return contributor;
            }
        }
        for (Admin admin : adminList) {
            if (admin.getContributions().contains(production)) {
                return admin;
            }
        }
        return null;
    }

    private User findUserForActor(Actor actor) {
        for (Contributor contributor : contributorList) {
            if (contributor.getContributions().contains(actor)) {
                return contributor;
            }
        }
        for (Admin admin : adminList) {
            if (admin.getContributions().contains(actor)) {
                return admin;
            }
        }
        return null;
    }

    private void addDeleteFromSystem(){
        System.out.println("1) Add actor to the system");
        System.out.println("2) Add production to system");
        System.out.println("3) Delete actor from system");
        System.out.println("4) Delete production from system");
        Scanner adminScanner = new Scanner(System.in);
        int adminOption = adminScanner.nextInt();
        switch (adminOption) {
            case 1:
                // Add actor to the system
                System.out.println("Enter the name of the actor to add:");
                adminScanner.nextLine(); // Consume the newline character
                String newActorName = adminScanner.nextLine();

                // Enter performances
                List<Actor.Performance> performances = new ArrayList<>();
                System.out.println("Enter the number of performances:");
                int numberOfPerformances = adminScanner.nextInt();
                adminScanner.nextLine(); // Consume the newline character

                for (int i = 0; i < numberOfPerformances; i++) {
                    System.out.println("Enter performance title:");
                    String title = adminScanner.nextLine();
                    System.out.println("Enter performance type (Movie/Series):");
                    String type = adminScanner.nextLine();
                    performances.add(new Actor.Performance(title, type));
                }

                // Create the Actor object with additional information
                System.out.println("Enter the actor's biography:");
                String actorBiography = adminScanner.nextLine();


                // Create the Actor object and add it to the system
                Actor newActor = new Actor(newActorName,performances, actorBiography);
                if(authenticatedUser instanceof Staff){
                    Staff staffInstance = (Staff) authenticatedUser;
                    //Add actor to contributions
                    staffInstance.addActorSystem(newActor);
                    // Add actor in system also
                    actors.add(newActor);
                    AddActorExperienceStrategy addActorExperienceStrategy = new AddActorExperienceStrategy();
                    staffInstance.setExperienceStrategy(addActorExperienceStrategy);
                    int experiencePoints = staffInstance.getExperienceStrategy().calculateExperience();
                    if(!staffInstance.getAccountType().equals(AccountType.Admin)) {
                        staffInstance.updateExperience(staffInstance.getExperience() + experiencePoints);
                    }
                }

                System.out.println("Actor added successfully.");
                break;
            case 2:
                // Add movie/series to the system
                System.out.println("Enter the title of the production to add:");
                adminScanner.nextLine(); // Consume the newline character
                String newProductionTitle = adminScanner.nextLine();


                System.out.println("Enter the type of the production (Movie/Series):");
                adminScanner.nextLine(); // Consume the newline character
                String productionType = adminScanner.nextLine();


                System.out.println("Enter the plot of the production (Movie/Series):");
                adminScanner.nextLine(); // Consume the newline character
                String productionPlot = adminScanner.nextLine();


                System.out.println("Enter the duration for the (Movie/Series):");
                adminScanner.nextLine(); // Consume the newline character
                String duration = adminScanner.nextLine();


                System.out.println("Enter the release year for the (Movie/Series):");
                Long releaseYear = adminScanner.nextLong();
                adminScanner.nextLine(); // Consume the newline character


                System.out.println("Enter the average rating for the (Movie/Series):");
                Double averageRating = adminScanner.nextDouble();
                adminScanner.nextLine(); // Consume the newline character


                // Enter directors
                List<String> directors = new ArrayList<>();
                System.out.println("Enter the number of directors:");
                int numberOfDirectors = adminScanner.nextInt();
                adminScanner.nextLine(); // Consume the newline character

                for (int i = 0; i < numberOfDirectors; i++) {
                    System.out.println("Enter director name:");
                    directors.add(adminScanner.nextLine());
                }

                // Enter actors
                List<String> actors = new ArrayList<>();
                System.out.println("Enter the number of actors:");
                int numberOfActors = adminScanner.nextInt();
                adminScanner.nextLine(); // Consume the newline character

                for (int i = 0; i < numberOfActors; i++) {
                    System.out.println("Enter actor name:");
                    actors.add(adminScanner.nextLine());
                }

                // Enter genres
                List<Genre> genres = new ArrayList<>();
                System.out.println("Enter the number of genres:");
                int numberOfGenres = adminScanner.nextInt();
                adminScanner.nextLine(); // Consume the newline character

                for (int i = 0; i < numberOfGenres; i++) {
                    System.out.println("Enter genre:");
                    genres.add(Genre.valueOf(adminScanner.nextLine()));
                }

                // Create the Production object and add it to the system
                Production newProduction;
                if ("Movie".equalsIgnoreCase(productionType)) {
                    newProduction = new Movie(newProductionTitle,productionType ,directors,actors,
                            genres, new ArrayList<>(), productionPlot, averageRating,duration,releaseYear);
                } else if ("Series".equalsIgnoreCase(productionType)) {
                    System.out.println("Enter the number of seasons:");
                    Long numberOfSeasons = adminScanner.nextLong();
                    adminScanner.nextLine(); // Consume the newline character
                    newProduction = new Series(newProductionTitle,productionType, directors , actors,
                           genres, new ArrayList<>(), productionPlot, averageRating,releaseYear,numberOfSeasons,null);
                } else {
                    System.out.println("Invalid production type. Please enter Movie or Series.");
                    break;
                }

                if(authenticatedUser instanceof Staff){
                    Staff staffInstance = (Staff) authenticatedUser;
                    staffInstance.addProductionSystem(newProduction);
                    //Add production in system
                    productions.add(newProduction);

                    AddProductionExperienceStrategy addProductionExperienceStrategy = new AddProductionExperienceStrategy();
                    staffInstance.setExperienceStrategy(addProductionExperienceStrategy);
                    int experiencePoints = staffInstance.getExperienceStrategy().calculateExperience();
                    if(!staffInstance.getAccountType().equals(AccountType.Admin)) {
                        staffInstance.updateExperience(staffInstance.getExperience() + experiencePoints);
                    }
                }

                System.out.println("Production added successfully.");
                break;
            case 3:
                // Delete actor from the system
                System.out.println("Select the actor to delete:");
                SortedSet<T> actorsContributions = null;
                if(authenticatedUser instanceof Staff){
                    Staff staffInstance = (Staff) authenticatedUser;
                    actorsContributions = staffInstance.getContributions();
                }

                List<Actor> userContributedActors = new ArrayList<>();
                if (actorsContributions != null) {
                    for (Object contribution : actorsContributions) {
                        if (contribution instanceof Actor) {
                            userContributedActors.add((Actor) contribution);
                        }
                    }
                }

                displayActorsContributions(userContributedActors);
                int actorToDeleteIndex = adminScanner.nextInt();
                adminScanner.nextLine(); // Consume the newline character

                Actor actorToDelete = userContributedActors.get(actorToDeleteIndex - 1);
                if (authenticatedUser instanceof Staff) {
                    Staff staffInstance = (Staff) authenticatedUser;
                    //Remove from entire system
                    getActors().remove(actorToDelete);
                    //Remove from its contributions
                    staffInstance.removeActorSystem(actorToDelete.getName());
                }
                System.out.println("Actor deleted successfully.");
                break;

            case 4:
                //Delete by available productions
                System.out.println("Choose the production to delete:");
                SortedSet<T> productionsContributions = null;

                if (authenticatedUser instanceof Staff) {
                    Staff staffInstance = (Staff) authenticatedUser;
                    productionsContributions = staffInstance.getContributions();
                }

                List<Production> userContributedProductions = new ArrayList<>();
                if (productionsContributions != null) {
                    for (Object contribution : productionsContributions) {
                        if (contribution instanceof Production) {
                            userContributedProductions.add((Production) contribution);
                        }
                    }
                }

                displayProductionContributions(userContributedProductions);

                int productionToDeleteIndex = adminScanner.nextInt();
                adminScanner.nextLine(); // Consume the newline character

                Production productionToDelete = userContributedProductions.get(productionToDeleteIndex - 1);
                if (authenticatedUser instanceof Staff) {
                    Staff staffInstance = (Staff) authenticatedUser;
                    //Remove from entire system
                    getProductions().remove(productionToDelete);
                    //Remove from its contributions
                    staffInstance.removeProductionSystem(productionToDelete.getTitle());
                }
                System.out.println("Production deleted successfully.");
                break;

            default:
                System.out.println("Invalid option. Please choose a valid option.");
                break;
        }
    }

    private void displayActorsContributions(List<Actor> actors) {
        System.out.println("Choose an actor to delete:");
        for (int i = 0; i < actors.size(); i++) {
            System.out.println((i + 1) + ") " + actors.get(i).getName());
        }
    }

    private void displayProductionContributions(List<Production> productions) {
        for (int i = 0; i < productions.size(); i++) {
            System.out.println((i + 1) + ") " + productions.get(i).getTitle());
        }
    }

    private void addDeleteReview(){
        System.out.println("1) Add review");
        System.out.println("2) Delete review");
        Scanner regularScanner = new Scanner(System.in);
        int regularOption = regularScanner.nextInt();
        switch (regularOption){
            case 1:
                System.out.println("Enter the title of the movie/series:");
                regularScanner.nextLine();
                String newTitle = regularScanner.nextLine();
                System.out.println("Enter the rating of the movie/series:");
                Integer ratingValue = regularScanner.nextInt();
                System.out.println("Enter the comment of the movie/series:");
                regularScanner.nextLine();
                String comment = regularScanner.nextLine();
                //Search the production
                Production production = getProductionByName(newTitle);
                Staff contributor = getContributorAdminForProduction(production);
                if(production != null){
                    if(authenticatedUser instanceof Regular){
                        Regular regularInstance = (Regular) authenticatedUser;
                        //Notify Regular and add rating/review
                        regularInstance.addReview(production,ratingValue,comment);
                        // Notify Contributor/Admin who added the production
                        contributor.notifyContributorAdminForRating(production);
                    }
                }else{
                    System.out.println("Could not find the production in the system.");
                }
                break;
            case 2:
                System.out.println("Enter the title of the movie/series:");
                regularScanner.nextLine();
                String deleteTitle = regularScanner.nextLine();

                //List productions reviews
                // Search the production
                Production productionToDelete = getProductionByName(deleteTitle);
                if(productionToDelete != null){
                    if(authenticatedUser instanceof Regular){
                        Regular regularInstance = (Regular) authenticatedUser;
                        regularInstance.deleteReview(productionToDelete);
                    }
                }else{
                    System.out.println("Could not find the production in the system.");
                }
                break;
            default:
                System.out.println("Invalid option. Please choose a valid option.");
                break;
        }
    }

    public Staff getContributorAdminForProduction(Production production){
        Staff contributorProduction = null;
        if (production != null) {
            // Check if the production is added by a Contributor
            for (Contributor contributor : contributorList) {
                if (contributor.getContributions().contains(production)) {
                    contributorProduction = contributor;
                    break;
                }
            }
            // If not found among Contributors, check among Admins
            if (contributorProduction == null) {
                for (Admin admin : adminList) {
                    if (admin.getContributions().contains(production)) {
                        contributorProduction = admin;
                        break;
                    }
                }
            }
        }
        return contributorProduction;
    }

    private void addDeleteUser(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose action:");
        System.out.println("1) Add User");
        System.out.println("2) Delete User");
        int userAction = scanner.nextInt();

        switch (userAction) {
            case 1:
                System.out.println("Creating a new user:");
                System.out.println("Enter the name of the person who owns the account:");
                scanner.nextLine(); // Consume the newline character
                String ownerName = scanner.nextLine();
                String generatedUsername = generateUniqueUsername(ownerName);
                String generatedPassword = generateStrongPassword();

                System.out.println("Generated username: " + generatedUsername);
                System.out.println("Generated password: " + generatedPassword);

                User.Information newUserInformation = new User.Information.InformationBuilder().build();

                // Create the user using UserFactory
                User newUser = UserFactory.createUser(newUserInformation, AccountType.Regular, generatedUsername, 0, new ArrayList<String>(),new TreeSet<T>());

                // Add the new user to the appropriate user list
                ((Admin)authenticatedUser).addUser(newUser);

                System.out.println("User added successfully.");
                break;

            case 2:
                System.out.println("Select a user to delete:");
                Admin authenticatedAdmin = (Admin)authenticatedUser;
                List<String> usersToDelete = authenticatedAdmin.getAddedUsers();

                for (int i = 0; i < usersToDelete.size(); i++) {
                    System.out.println((i+1) + ") " + usersToDelete.get(i));
                }
                // Find and delete the user with the specified username
                int userChoice = scanner.nextInt();
                if (userChoice < 1 || userChoice > usersToDelete.size()) {
                    System.out.println("Invalid choice.");
                } else {
                    String userToDelete = usersToDelete.get(userChoice - 1);
                    deleteUserByUsername(userToDelete);
                    System.out.println("User deleted successfully.");
                }
                break;

            default:
                System.out.println("Invalid choice.");
                break;
        }
    }

    private User findUserByUsername(String username){
        List<User> userList = new ArrayList<>();
        userList.addAll(regularList);
        userList.addAll(contributorList);
        userList.addAll(adminList);

        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
    private void deleteUserByUsername(String usernameToDelete) {
        // Remove the user from the appropriate user list
        List<User> userList = new ArrayList<>();
        userList.addAll(regularList);
        userList.addAll(contributorList);
        userList.addAll(adminList);

        for (User user : userList) {
            if (user.getUsername().equals(usernameToDelete)) {
                ((Admin) user).removeUserDetails(user);
                ((Admin)authenticatedUser).removeUser(user);

                System.out.println("User '" + usernameToDelete + "' deleted successfully.");
                return;
            }
        }

        System.out.println("User not found with username: " + usernameToDelete);
    }

    private String generateUniqueUsername(String ownerName) {

        String baseUsername = ownerName.toLowerCase().replace(" ", "_");
        String uniqueUsername = baseUsername + "_" + System.currentTimeMillis();
        return uniqueUsername;
    }

    private String generateStrongPassword() {

        String upperChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerChars = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialChars = "!@#$%^&*()-=_+[]{}|;:'\",.<>/?";

        String allChars = upperChars + lowerChars + numbers + specialChars;
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 12; i++) {
            int index = (int) (Math.random() * allChars.length());
            password.append(allChars.charAt(index));
        }

        return password.toString();
    }

    private void solveRequest(){
       //First list requests shared among admins
        if(authenticatedUser instanceof Admin){
            RequestsHolder.displayAllRequests();
            RequestsHolder.resolveAdminsRequest();
        }
        if(authenticatedUser instanceof Staff) {
            Staff staffInstance = (Staff) authenticatedUser;
            staffInstance.resolveRequests();
        }

    }

    private void updateActor(){
        System.out.println("Enter Actor's name to update:");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine(); // Consume the newline character
        String actor = scanner.nextLine();

        if (authenticatedUser instanceof Admin) {
            Admin admin = (Admin) authenticatedUser;
            admin.updateActor(getActorByName(actor));
        }
        if (authenticatedUser instanceof Contributor) {
            Contributor contributor = (Contributor) authenticatedUser;
            contributor.updateActor(getActorByName(actor));
        }
    }

    private void updateProduction(){
        System.out.println("Enter Production's name to update:");
        Scanner scanner = new Scanner(System.in);
        String production = scanner.nextLine();

        if (authenticatedUser instanceof Contributor) {
            Contributor contributor = (Contributor) authenticatedUser;
            contributor.updateProduction(getProductionByName(production));
        }

        if (authenticatedUser instanceof Admin) {
            Admin admin = (Admin) authenticatedUser;
            admin.updateProduction(getProductionByName(production));
        }
    }

    private void logout() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1) Login again");
            System.out.println("2) Close the application");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    authenticatedUser.logout();
                    run();
                    return;
                case 2:
                    System.out.println("Closing the application...");
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please choose a valid option.");
            }
        }
    }

    public void setAuthenticatedUser(User authenticatedUser) {
    this.authenticatedUser = authenticatedUser;
    }
}
