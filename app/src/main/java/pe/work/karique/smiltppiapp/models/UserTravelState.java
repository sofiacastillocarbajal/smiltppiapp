package pe.work.karique.smiltppiapp.models;

public class UserTravelState {
    private String id;
    private String description;
    private String numberOfUsers;

    public UserTravelState() {
    }

    public UserTravelState(String id, String description, String numberOfUsers) {
        this.id = id;
        this.description = description;
        this.numberOfUsers = numberOfUsers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNumberOfUsers() {
        return numberOfUsers;
    }

    public void setNumberOfUsers(String numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
    }
}
