package org.example;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RequestsHolder {
    //Request list shared among admins
    private static List<Request> allRequests = new ArrayList<>();
    public static void addRequest(Request request) {
        allRequests.add(request);
    }
    public static void removeRequest(Request request) {
        allRequests.remove(request);
    }
    public static void displayAllRequests() {
        System.out.println("Requests shared among admins:");
        for (Request request : allRequests) {
            if(request.isLoaded() == false) {
                System.out.println(request.displayInfo());
                System.out.println("------------------------");
            }
        }
    }
    public static List<Request> getRequests() {
        return allRequests;
    }
    public static void setRequests(List<Request> allRequests) {
        RequestsHolder.allRequests = allRequests;
    }

    public static void resolveAdminsRequest(){
        System.out.println("Resolve request:");
        for(Request request : RequestsHolder.getRequests()){
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

    public static List<Request> getAllRequests() {
        return allRequests;
    }

    public static void setAllRequests(List<Request> allRequests) {
        RequestsHolder.allRequests = allRequests;
    }
}
