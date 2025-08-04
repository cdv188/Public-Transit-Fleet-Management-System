/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.User;
import java.util.List;

public interface UserDAO {
    boolean addUser(User user);
    User findUserByEmail(String email);
    User validateUser(String email, String password);
    List<User> getAllUsers();
    boolean deleteUser(int userId);

    /**
     * Get all users of a specific type (e.g., "Manager", "Operator")
     * @param userType the user type to filter by
     * @return list of matching users
     */
    List<User> getUsersByType(String userType);
}
