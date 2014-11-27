package com.webcalendar.dao;

import com.webcalendar.domain.User;
import org.hibernate.HibernateException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.UUID;

/**
 * Layer that allows you to perform various manipulations
 * with the user in the database
 *
 * @author Ruslan Borisov
 */
public interface UserDAO {

    /**
     * Add user to database
     *
     * @param user : User for add, not null
     * @throws HibernateException if happen database exception.
     */
    public void addUser(User user) throws HibernateException;

    /**
     * Update user from database
     *
     * @param user : User for update, not null
     * @throws HibernateException if happen database exception.
     */
    public void updateUser(User user) throws HibernateException;

    /**
     * Delete user from database
     *
     * @param user : User for delete, not null
     * @throws HibernateException if happen database exception.
     */
    public void deleteUser(User user) throws HibernateException;

    /**
     * Get user by id from database
     *
     * @param id : Id of user whom need find, not null
     * @return user by given id
     * @throws HibernateException if happen database exception.
     * @throws UsernameNotFoundException if user not exists in database
     */
    public User getUserById(UUID id) throws HibernateException, UsernameNotFoundException;

    /**
     * Get user by username from database
     *
     * @param username : Username of user whom need find, not null
     * @return user by given username
     * @throws HibernateException if happen database exception.
     * @throws UsernameNotFoundException if user not exists in database
     */
    public UserDetails getUserByUsername(String username) throws HibernateException, UsernameNotFoundException;

    /**
     * Get user by email from database
     *
     * @param email : Email of user whom need find, not null
     * @return user by given email
     * @throws HibernateException if happen database exception.
     * @throws UsernameNotFoundException if user not exists in database
     */
    public UserDetails getUserByEmail(String email) throws HibernateException, UsernameNotFoundException;
}
