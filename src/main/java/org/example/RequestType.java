package org.example;

public enum RequestType {
    DELETE_ACCOUNT,
    ACTOR_ISSUE,
    MOVIE_ISSUE,
    OTHERS;


    public String getRequestType(){
        return this.name();
    }


}
