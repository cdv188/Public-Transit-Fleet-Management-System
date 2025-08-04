package userDAO;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    /**
     * Adds a new user.
     * @param user The user object to be added.
     * @return The added user
     */
    User addUser(User user);

    /**
     * Finds a user by their email address.
     * @param email The email of the user to find.
     * @return An Optional containing the user if found, otherwise empty.
     */
    Optional<User> findUserByEmail(String email);

    /**
     * Validates a user's credentials.
     * @param email The user's email.
     * @param password The user's password.
     * @return An Optional containing the user if validation is successful, otherwise empty.
     */
    Optional<User> validateUser(String email, String password);
    
    /**
     * Get all users of a specific type (e.g., "Manager", "Operator")
     * Added for Ali's OperatorPerformanceReportStrategy
     * @param userType the user type to filter by
     * @return list of matching users
     */
    List<User> getUsersByType(String userType);
}