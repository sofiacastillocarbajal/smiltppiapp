package pe.work.karique.smiltppiapp.repositories;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import pe.work.karique.smiltppiapp.models.UserTravelState;

public class UserTravelStateRepositories {
    private static UserTravelStateRepositories userTravelStateRepositories;
    private List<UserTravelState> userTravelStates;

    private  UserTravelStateRepositories() {
        userTravelStates = new ArrayList<>();
        userTravelStates.add(new UserTravelState("1", "Espera", "3"));
        userTravelStates.add(new UserTravelState("2", "A bordo", "4"));
        userTravelStates.add(new UserTravelState("3", "Finalizado", "2"));
    }

    public static UserTravelStateRepositories getInstance(){
        if (userTravelStateRepositories == null){
            userTravelStateRepositories = new UserTravelStateRepositories();
        }
        return userTravelStateRepositories;
    }

    public List<UserTravelState> getUserTravelStates() {
        return userTravelStates;
    }
}
