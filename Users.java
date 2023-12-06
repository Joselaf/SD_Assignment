import java.util.HashMap;

public class Users extends User {
    HashMap<String, User> users = new HashMap<>();

    public void add_User(User add) {
        String username = get_user_username(add);
        users.put(username, add);
    }

}
