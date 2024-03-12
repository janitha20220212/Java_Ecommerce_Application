import java.io.*;
import java.util.*;

class UserManager implements Serializable {
    private List<User> userList;

    public UserManager() {
        this.userList = new ArrayList<>();
        // Add a default user for testing
        userList.add(new User("admin", "admin123"));
    }

    public boolean authenticateUser(String username, String password) {
        for (User user : userList) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true; // Authentication successful
            }
        }
        return false; // Authentication failed
    }

    public void addUser(User user) {
        userList.add(user);
    }
}