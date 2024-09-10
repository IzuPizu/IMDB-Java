package org.example;

import java.util.*;
import java.util.List;

public abstract class Staff<T extends Comparable<T>> extends User implements StaffInterface,Observer {
    private List<Request> requests;
    private SortedSet<T> contributions;
    private ProductionComparator productionComparator;
    private ExperienceStrategy experienceStrategy;
    public Staff(User.Information information, AccountType accountType, String username,
                 int experience, List<String> notifications, SortedSet<T> favorites) {
        super(information, accountType, username, experience, notifications, favorites);
        this.contributions = new TreeSet<>(productionComparator);
        this.requests = new ArrayList<>();
        for (Request request : requests) {
            request.addObserver(this); // Register the staff as an observer for each request
        }

    }

    public void notifyContributorAdminForRating(Production production) {
        // Iterate over each Contributor/Admin's contributions
        for (T contribution : contributions) {
            if (contribution instanceof Production && contribution.equals(production)) {
                // Notify the Contributor/Admin
               update("Your contributed production '" + production.getTitle() + "' has received a new rating.");
            }
        }
    }

    public List<Request> getRequests() {
        return requests;
    }
    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }
    public SortedSet<T> getContributions() {
        return contributions;
    }

    @Override
    public void addProductionSystem(Production p) {
        contributions.add((T) p);
    }
    @Override
    public void addActorSystem(Actor a) {
        contributions.add((T) a);
    }
    @Override
    public void removeProductionSystem(String name) {
        Production productionToRemove = null;
        // Iterate over contributions to find the production with the given name
        for (Object contribution : getContributions()) {
            if (contribution instanceof Production) {
                Production production = (Production) contribution;
                if (production.getTitle().equals(name)) {
                    productionToRemove = production;
                    break;
                }
            }
        }

        // Remove the production from the contributions set
        if (productionToRemove != null) {
            getContributions().remove(productionToRemove);
            System.out.println("Production '" + name + "' removed from the system.");
        } else {
            System.out.println("Production '" + name + "' not found in your contributions.");
        }


    }
    @Override
    public void removeActorSystem(String name) {
        Actor actorToRemove = null;

        // Iterate over contributions to find the actor with the given name
        for (Object contribution : getContributions()) {
            if (contribution instanceof Actor) {
                Actor actor = (Actor) contribution;
                if (actor.getActorName().equals(name)) {
                    actorToRemove = actor;
                    break;
                }
            }
        }

        // Remove the actor from the contributions set
        // If is not under admin responsability , but was found in the
        // Contributor's contributions safely delete the actor
        if(!actorToRemove.isUnderAdminResponsibility()) {
            if (actorToRemove != null) {
                getContributions().remove(actorToRemove);
                System.out.println("Actor '" + name + "' removed from the system.");
            } else {
                System.out.println("Actor '" + name + "' not found in your contributions.");
            }
        }else{
            if (actorToRemove != null) {
                getContributions().remove(actorToRemove);
                System.out.println("Actor '" + name + "' removed from the system by admin.");
            } else {
                System.out.println("Actor '" + name + "' not found in your contributions.");
            }
        }
    }

    @Override
    public void updateProduction(Production p) {
        System.out.println("Updating production details: " + p.getTitle());

        if (contributions.contains(p)) {

            updateProductionDetails(p);
            System.out.println("Production details updated successfully.");
        } else {
            System.out.println("You do not have the authority to update details for this production.");
        }
    }
    @Override
    public void updateActor(Actor a) {
        System.out.println("Updating actor details: " + a.getActorName());

        if (contributions.contains(a)) {

            updateActorDetails(a);
            System.out.println("Actor details updated successfully.");
        } else {
            System.out.println("You do not have the authority to update details for this actor.");
        }
    }
    @Override
    public void resolveRequests() {
        System.out.println("Requests:");
        //Search through specific admin/contributor request list
        for (Request request : requests) {
            //If the request has not been loaded yet (rejected/solved)
            if (!request.isLoaded()) {
                User creatorUser = request.getCreator();
                // Display request information
                System.out.println(request.displayInfo());
                //Check if the request is resolved or not
                System.out.println("Choose option:");
                System.out.println("1. Solve request");
                System.out.println("2. Reject request");
                Scanner newscanner = new Scanner(System.in);
                Integer option = newscanner.nextInt();
                newscanner.nextLine();
                switch (option) {
                    case 1:
                        request.setResolved(true);
                        request.setLoaded(true);
                        if (creatorUser != null) {
                            creatorUser.update("Your request has been resolved: " + request.getDescription());
                            if(!creatorUser.getAccountType().equals(AccountType.Admin)) {
                                creatorUser.updateExperience(creatorUser.getExperience() + 1);
                            }
                        }
                        break;
                    case 2:
                        request.setResolved(false);
                        request.setLoaded(true);
                        if (creatorUser != null) {
                            creatorUser.update("Your request has been rejected: " + request.getDescription());
                        }
                        break;
                    default:
                        System.out.println("Invalid option.");
                }

            }
        }

    }


    protected abstract boolean resolveRequest(Request request);
    private void updateProductionDetails(Production production) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose the details to modify:");
        System.out.println("1. Title");
        System.out.println("2. Description");
        System.out.println("3. Directors");
        System.out.println("4. Actors");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                System.out.println("Enter the new title:");
                scanner.nextLine(); // Consume the newline character
                String newTitle = scanner.nextLine();
                production.setTitle(newTitle);
                break;
            case 2:
                System.out.println("Enter the new description:");
                scanner.nextLine(); // Consume the newline character
                String newDescription = scanner.nextLine();
                production.setPlot(newDescription);
                break;
            case 3:
                System.out.println("Add/Delete/Modify directors:");
                scanner.nextLine(); // Consume the newline character
                System.out.println("1) Add a director");
                System.out.println("2) Delete a director");
                System.out.println("3) Modify a director");
                int choose = scanner.nextInt();
                switch (choose){
                    case 1:
                        System.out.println("Enter director's name :");
                        scanner.nextLine(); // Consume the newline character
                        String newDirector = scanner.nextLine();
                        production.getDirectors().add(newDirector);
                        break;
                    case 2:
                        System.out.println("Enter director's name :");
                        scanner.nextLine(); // Consume the newline character
                        String oldDirector = scanner.nextLine();
                        production.getDirectors().remove(oldDirector);
                        break;
                    case 3:
                        System.out.println("Enter director's name to modify:");
                        scanner.nextLine(); // Consume the newline character
                        String modifyDirector = scanner.nextLine();
                        System.out.println("Enter new director's name:");
                        scanner.nextLine(); // Consume the newline character
                        String newestDirector = scanner.nextLine();
                        ListIterator<String> iterator = production.getDirectors().listIterator();
                        while (iterator.hasNext()) {
                            if (iterator.next().equals(modifyDirector)) {
                                iterator.set(newestDirector);
                                break;
                            }
                        }
                        break;
                    default:
                        System.out.println("Please choose a valid option.");
                        break;
                }
            case 4:
                System.out.println("Add/Delete/Modify actors:");
                scanner.nextLine(); // Consume the newline character
                System.out.println("1) Add an actor");
                System.out.println("2) Delete an actor");
                System.out.println("3) Modify an actor");
                int choosescnd = scanner.nextInt();
                switch (choosescnd){
                    case 1:
                        System.out.println("Enter actor's name :");
                        scanner.nextLine(); // Consume the newline character
                        String newActor = scanner.nextLine();
                        production.getActors().add(newActor);
                        break;
                    case 2:
                        System.out.println("Enter actor's name :");
                        scanner.nextLine(); // Consume the newline character
                        String oldActor = scanner.nextLine();
                        production.getActors().remove(oldActor);
                        break;
                    case 3:
                        System.out.println("Enter actor's name to modify:");
                        scanner.nextLine(); // Consume the newline character
                        String modifyActor = scanner.nextLine();
                        System.out.println("Enter new director's name:");
                        scanner.nextLine(); // Consume the newline character
                        String newestActor = scanner.nextLine();
                        ListIterator<String> iterator = production.getActors().listIterator();
                        while (iterator.hasNext()) {
                            if (iterator.next().equals(modifyActor)) {
                                iterator.set(newestActor);
                                break;
                            }
                        }
                        break;
                    default:
                        System.out.println("Please choose a valid option.");
                        break;
                }
                break;

            // Add more cases for other details
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void updateActorDetails(Actor actor) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose the details to modify:");
        System.out.println("1) Actor Name");
        System.out.println("2) Biography");
        System.out.println("3) Performances");
        // Add more options as needed

        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                System.out.println("Enter the new actor name:");
                scanner.nextLine(); // Consume the newline character
                String newActorName = scanner.nextLine();
                actor.setActorName(newActorName);
                break;
            case 2:
                System.out.println("Enter the new biography:");
                scanner.nextLine(); // Consume the newline character
                String newBiography = scanner.nextLine();
                actor.setBiography(newBiography);
                break;
            case 3:
                System.out.println("Add/Delete/Modify performances:");
                System.out.println("1) Add performance");
                System.out.println("2) Delete performance");
                System.out.println("3) Modify performance");
                int choosescnd = scanner.nextInt();
                switch (choosescnd) {
                    case 1:
                        System.out.println("Enter performance's title :");
                        scanner.nextLine(); // Consume the newline character
                        String newPerformance = scanner.nextLine();
                        System.out.println("Enter performance's type :");
                        scanner.nextLine(); // Consume the newline character
                        String newPerformanceType = scanner.nextLine();
                        Actor.Performance newPerformanceobj = new Actor.Performance(newPerformance, newPerformanceType);
                        actor.getPerformances().add(newPerformanceobj);
                        break;
                    case 2:
                        System.out.println("Enter performance's name :");
                        scanner.nextLine(); // Consume the newline character
                        String oldPerformance = scanner.nextLine();
                        ListIterator<Actor.Performance> iterator = actor.getPerformances().listIterator();
                        while (iterator.hasNext()) {
                            if (iterator.next().equals(oldPerformance)) {
                                iterator.remove();
                                break;
                            }
                        }
                        break;
                    case 3:
                        System.out.println("Enter performance's name to modify:");
                        scanner.nextLine(); // Consume the newline character
                        String modifyPerformanceName = scanner.nextLine();

                        // Find the performance with the specified name
                        Actor.Performance modifyPerformance = null;
                        for (Actor.Performance performance : actor.getPerformances()) {
                            if (performance.getTitle().equals(modifyPerformanceName)) {
                                modifyPerformance = performance;
                                break;
                            }
                        }

                        if (modifyPerformance != null) {
                            System.out.println("Enter new performance's title:");
                            String newestPerfTitle = scanner.nextLine();
                            // Update the title of the found performance
                            modifyPerformance.setTitle(newestPerfTitle);
                            System.out.println("Performance title updated successfully.");
                        } else {
                            System.out.println("Performance not found.");
                        }
                        break;

                        default:
                        System.out.println("Invalid choice.");
                }
                break;

            default:
                System.out.println("Invalid choice");

        }
    }

    @Override
    public void updateExperience(int newExperience) {
        super.updateExperience(newExperience);
    }

    @Override
    public ProductionComparator getProductionComparator() {
        return productionComparator;
    }

    @Override
    public void setProductionComparator(ProductionComparator productionComparator) {
        this.productionComparator = productionComparator;
    }

    @Override
    public ExperienceStrategy getExperienceStrategy() {
        return experienceStrategy;
    }

    @Override
    public void setExperienceStrategy(ExperienceStrategy experienceStrategy) {
        this.experienceStrategy = experienceStrategy;
    }

    public void setContributions(SortedSet<T> contributions) {
        this.contributions = contributions;
    }
}
