package com.webcalendar.service;

import com.webcalendar.dao.UserDAO;
import com.webcalendar.domain.User;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.UUID;

public class UserServiceImpl implements UserService, UserDetailsService {

    private static final Logger logger = Logger.getLogger(UserServiceImpl.class);
    private final UserDAO userDAO;

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public boolean addUser(User user) {

        try {
            logger.info("START ADDING USER...");
            userDAO.addUser(user);
            logger.info("Add User successfully");
            return true;

        } catch (HibernateException e) {
            logger.error(e);
            return false;
        }
    }

    @Override
    public boolean updateUser(User user) {
        try {
            logger.info("START UPDATING USER...");
            userDAO.updateUser(user);
            logger.info("Update User successfully");
            return true;

        } catch (HibernateException e) {
            logger.error(e);
            return false;
        }
    }

    @Override
    public boolean deleteUser(User user) {
        try {
            logger.info("START DELETING USER...");
            userDAO.deleteUser(user);
            logger.info("Delete User successfully");
            return true;

        } catch (HibernateException e) {
            logger.error(e);
            return false;
        }
    }

    @Override
    public UserDetails loadUserById(UUID id) {
        try {
            logger.info("START GETTING USER...");
            User user = userDAO.getUserById(id);
            logger.info("Get User successfully");
            return user;

        } catch (UsernameNotFoundException e) {
            logger.info(e);
            return null;
        } catch (HibernateException e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        try {
            logger.info("START GETTING USER...");
            User user = (User) userDAO.getUserByUsername(username);
            logger.info("Get User successfully");
            return user;

        } catch (UsernameNotFoundException e) {
            logger.info(e);
            return null;
        } catch (HibernateException e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public UserDetails loadUserByEmail(String email) {

        try {
            logger.info("START GETTING USER...");
            User user = (User) userDAO.getUserByEmail(email);
            logger.info("Get User successfully");
            return user;

        } catch (UsernameNotFoundException e) {
            logger.info(e);
            return null;
        } catch (HibernateException e) {
            logger.error(e);
            return null;
        }
    }
}
