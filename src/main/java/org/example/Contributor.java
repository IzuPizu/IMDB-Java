package org.example;

import java.util.List;
import java.util.SortedSet;

public class Contributor<T extends Comparable<T>> extends  Staff implements  RequestsManager {
    public Contributor(User.Information information, AccountType accountType, String username,
                       int experience, List<String> notifications, SortedSet<T> favorites) {
        super(information, accountType, username, experience, notifications, favorites);
    }

    @Override
    public void createRequest(Request r) {
        super.getRequests().add(r);
    }

    @Override
    public void removeRequest(Request r) {
        super.getRequests().remove(r);
    }


    @Override
    public void setContributions(SortedSet contributions) {
        super.setContributions(contributions);
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
