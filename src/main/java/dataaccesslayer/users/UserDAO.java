package dataaccesslayer.users;

import java.util.List;
import java.util.Optional;

/**
 * DAO interface for user management.
 */
public interface UserDAO {

    /**
     * Adds a new user.
     *
     * @param user the user to add
     * @return the added user
     */
    User addUser(User user);

    /**
     * Finds a user by email.
     *
     * @param email the email to search for
     * @return an Optional containing the user if found
     */
    Optional<User> findUserByEmail(String email);

    /**
     * Validates user credentials.
     *
     * @param email    the user's email
     * @param password the user's password
     * @return an Optional containing the user if valid
     */
    Optional<User> validateUser(String email, String password);

    /**
     * Retrieves all users of a specific type.
     *
     * @param userType the user type to filter by
     * @return list of matching users
     */
    List<User> getUsersByType(String userType);
}
