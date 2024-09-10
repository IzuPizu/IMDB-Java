package org.example;
import java.util.List;
import java.util.SortedSet;

public class UserFactory{
    public static <T extends Comparable<T>> User createUser(User.Information information,
                                                            AccountType accountType,
                                                            String username,
                                                            int experience,
                                                            List<String> notifications,
                                                            SortedSet<T> favorites) {
        switch (accountType) {
            case Regular:
                return new Regular(information, accountType, username, experience,  notifications,  favorites);
            case Contributor:
                return new Contributor(information, accountType, username, experience, notifications, favorites);
            case Admin:
                return new Admin(information, accountType, username, experience, notifications, favorites);
            default:
                throw new IllegalArgumentException("Invalid account type");
        }
    }


}