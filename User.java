
public class User {
    String username;
    String password;

    public User() {
        username = "";
        password = "";
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String get_user_username(User u) {
        return u.username;
    }

    public String get_user_password(User u) {
        return u.password;
    }
}
