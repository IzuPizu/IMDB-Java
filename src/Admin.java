package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

public class Admin <T extends Comparable<T>> extends Staff {
    public Admin(User.Information information, AccountType accountType, String username,
                 int experience, List<String> notifications, SortedSet<T> favorites) {
        super(information, accountType, username, experience, notifications, favorites);
    }
    private List<String> addedUsers = new ArrayList<>();

    @Override
    public SortedSet getContributions() {
        return super.getContributions();
    }

    @Override
    public void setContributions(SortedSet contributions) {
        super.setContributions(contributions);
    }

    @Override
    public List<Request> getRequests() {
        return super.getRequests();
    }

    @Override
    public void setRequests(List list) {
        super.setRequests(list);
    }

    @Override
    public void resolveRequests() {
        super.resolveRequests();
    }

    public void addUser(User user) {
        if (user instanceof Regular) {
            // Add to regular list
            IMDB.getInstance().getRegularList().add((Regular) user);
            addedUsers.add(user.getUsername());
        } else if (user instanceof Contributor) {
            // Add to contributor list
            IMDB.getInstance().getContributorList().add((Contributor) user);
            addedUsers.add(user.getUsername());
        } else if (user instanceof Admin) {
            // Add to admin list
            IMDB.getInstance().getAdminList().add((Admin) user);
            addedUsers.add(user.getUsername());
        }
    }

    public List<String> getAddedUsers() {
        return addedUsers;
    }

    public void setAddedUsers(List<String> addedUsers) {
        this.addedUsers = addedUsers;
    }

    public void removeUser(User user) {
        if (user instanceof Regular) {
            IMDB.getInstance().getRegularList().remove(user);
            addedUsers.remove(user.getUsername());
        } else if (user instanceof Contributor) {
            // Remove from contributor list
            IMDB.getInstance().getContributorList().remove(user);
            addedUsers.remove(user.getUsername());
        } else if (user instanceof Admin) {
            // Remove from admin list
            IMDB.getInstance().getAdminList().remove(user);
            addedUsers.remove(user.getUsername());
        }
    }

    public void removeUserDetails(User user) {
        if (user instanceof Staff) {
            Staff staff = (Staff) user;
            if(user instanceof Contributor){
                Contributor contributor = (Contributor) user;
                transferContributionsToAdmins(contributor);
            }
            staff.getRequests().clear();
            staff.getContributions().clear();
            staff.getActorFavorites().clear();
            staff.getNotifications().clear();
            staff.getProductionFavorites().clear();
            staff.getFavorites().clear();

        }
        if (user instanceof Regular) {
           Regular regular = (Regular) user;
            regular.getRequests().clear();
            regular.getActorFavorites().clear();
            regular.getNotifications().clear();
            regular.getProductionFavorites().clear();
        }
    }

    private void transferContributionsToAdmins(Contributor contributor) {
        SortedSet<T> contributedProductions = contributor.getContributions();
        SortedSet<Actor> contributedActors = contributor.getActorFavorites();

        for (Object admin : IMDB.getInstance().getAdminList()) {
            Admin adminobj = (Admin)admin;
            adminobj.getContributions().addAll(contributedProductions);
            adminobj.getActorFavorites().addAll(contributedActors);
        }
    }
    @Override
    public void addProductionSystem(Production p) {
        super.addProductionSystem(p);
    }

    @Override
    public void addActorSystem(Actor a) {
        super.addActorSystem(a);
    }

    @Override
    protected boolean resolveRequest(Request request) {
        return true;
    }

    @Override
    public void updateProduction(Production p) {
        super.updateProduction(p);
    }

    @Override
    public void updateActor(Actor a) {
        super.updateActor(a);
    }

    @Override
    public void removeProductionSystem(String name) {
        Production productionToRemove = null;

        for (Object contribution : getContributions()) {
            if (contribution instanceof Production) {
                Production production = (Production) contribution;
                if (production.getTitle().equals(name)) {
                    productionToRemove = production;
                    break;
                }
            }
        }

        if (productionToRemove != null) {
            getContributions().remove(productionToRemove);
            System.out.println("Production '" + name + "' removed from the system by admin.");
        } else {
            System.out.println("Production '" + name + "' not found.");
        }
    }

    @Override
    public void removeActorSystem(String name) {
        Actor actorToRemove = null;

        for (Object contribution : getContributions()) {
            if (contribution instanceof Actor) {
                Actor actor = (Actor) contribution;
                if (actor.getActorName().equals(name)) {
                    actorToRemove = actor;
                    break;
                }
            }
        }

        if (actorToRemove != null) {
            getContributions().remove(actorToRemove);
            System.out.println("Actor '" + name + "' removed from the system by admin.");
        } else {
            System.out.println("Actor '" + name + "' not found.");
        }
    }
}
