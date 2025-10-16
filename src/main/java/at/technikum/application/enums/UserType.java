package at.technikum.application.enums;

public enum UserType {
    Admin("Admin"),
    User("User");

    private final String type;

    UserType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
