package sparta.team6.momo.model;

public enum UserRole {
    ROLE_USER("ROLE_USER"),
    ROLE_GUEST("ROLE_GUEST");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

}
