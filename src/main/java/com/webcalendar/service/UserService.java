package com.webcalendar.service;

import com.webcalendar.domain.User;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.UUID;
import com.webcalendar.dao.UserDAO;

/**
 * Provides ability to manipulate with user from and databases.
 * Type of operations: add, update, delete and various of search.
 *
 *
 * @author Ruslan Borisov
 */
public interface UserService {

    /**
     * Provides ability to add user to the database
     * Invokes method of {@link UserDAO}:  {@link UserDAO#addUser(User))}
     *
     * @param user : User for add, not null
     * @return true if user was added or false otherwise
     */
    boolean addUser(User user);

    /**
     * Provides ability to update user
     * Invokes method of {@link UserDAO}:  {@link UserDAO#updateUser(User)}
     *
     * @param user : User for update, not null
     * @return true if user was updated or false otherwise
     */
    boolean updateUser(User user);

    /**
     * Provides ability to delete user
     * Invokes method of {@link UserDAO}:  {@link UserDAO#deleteUser(User)}
     *
     * @param user : User for delete, not null
     * @return true if user was deleted or false otherwise
     */
    boolean deleteUser(User user);

    /**
     * Provides ability to search user by id
     * Invokes method of {@link UserDAO}:  {@link UserDAO#getUserById(UUID)}
     *
     * @param id : Id of the user for search, not null
     * @return user for given id or null if there was no mapping for given id
     */
    UserDetails loadUserById(UUID id);

    /**
     * Provides ability to search user by username
     * Invokes method of {@link UserDAO}:  {@link UserDAO#getUserByUsername(String)}
     *
     * @param username : Username of the user for search, not null
     * @return user for given username or null if there was no mapping for given username
     */
    UserDetails loadUserByUsername(String username);

    /**
     * Provides ability to search user by email
     * Invokes method of {@link UserDAO}:  {@link UserDAO#getUserByEmail(String)}
     *
     * @param email : Email of the user for search, not null
     * @return user for given email or null if there was no mapping for given email
     */
    UserDetails loadUserByEmail(String email);
}
