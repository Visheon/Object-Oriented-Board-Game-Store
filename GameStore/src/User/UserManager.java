package User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the loading of user accounts from UserAccounts.txt.
 * Constructs Admin or Customer objects depending on the role field in each line.
 * Format: userID; name; houseNumber; postcode; city; role
 */
public class UserManager {

    private static final String FILE_PATH = "UserAccounts.txt";
    private List<User> users;

    public UserManager() {
        users = new ArrayList<>();
        loadUsers();
    }

    // Reads every line from UserAccounts.txt and builds the user list — skips malformed lines
    private void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(";");
                if (parts.length != 6) continue;

                int userId         = Integer.parseInt(parts[0].trim());
                String name        = parts[1].trim();
                int houseNumber    = Integer.parseInt(parts[2].trim());
                String postcode    = parts[3].trim();
                String city        = parts[4].trim();
                String role        = parts[5].trim().toLowerCase();

                Address address = new Address(houseNumber, postcode, city);

                if (role.equals("admin")) {
                    users.add(new Admin(userId, name, address));
                } else if (role.equals("customer")) {
                    users.add(new Customer(userId, name, address));
                }
                // unrecognised roles are silently skipped
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: UserAccounts.txt not found.");
        } catch (IOException e) {
            System.out.println("Error reading UserAccounts.txt: " + e.getMessage());
        }
    }

    public List<User> getUsers() {
        return users;
    }

    public User getUserByIndex(int index) {
        if (index < 1 || index > users.size()) return null;
        return users.get(index - 1);
    }

    // Searches the user list by user ID — returns null if no match is found
    public User getUserById(int userId) {
        for (User u : users) {
            if (u.getUserId() == userId) return u;
        }
        return null;
    }
}