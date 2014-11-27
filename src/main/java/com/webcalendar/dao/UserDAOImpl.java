package com.webcalendar.dao;

import com.webcalendar.domain.User;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.UUID;

public class UserDAOImpl implements UserDAO, Serializable {

    private final SessionFactory sessionFactory;

    public UserDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public void addUser(User user) throws HibernateException {
        if (user == null)
            throw new IllegalArgumentException("ADD USER FAILED: You are trying to add a null user");
        if (user.getUsername() == null)
            throw new IllegalArgumentException("ADD USER FAILED: no login");
        if (user.getPassword() == null)
            throw new IllegalArgumentException("ADD USER FAILED: no password");

        sessionFactory.getCurrentSession().save(user);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public void updateUser(User user) throws HibernateException {
        if (user == null)
            throw new IllegalArgumentException("UPDATE USER FAILED: You are trying to add a null user");
        if (user.getUsername() == null)
            throw new IllegalArgumentException("UPDATE USER FAILED: no login");
        if (user.getPassword() == null)
            throw new IllegalArgumentException("UPDATE USER FAILED: no password");

        sessionFactory.getCurrentSession().update(user);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public void deleteUser(User user) throws HibernateException {
        if (user==null)
            throw new IllegalArgumentException("DELETE USER FAILED: user cannot be null");

        sessionFactory.getCurrentSession().delete(user);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public User getUserById(UUID id) throws HibernateException, UsernameNotFoundException {
        if (id==null)
            throw new IllegalArgumentException("GET CARD FAILED: Id cannot be null");

        User user = (User) sessionFactory.getCurrentSession().get(User.class, id);

        if(user==null)
            throw new UsernameNotFoundException("user with id: " + id + " not found!");

        return user;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public UserDetails getUserByUsername(String username) throws HibernateException, UsernameNotFoundException {
        if (username==null)
            throw new IllegalArgumentException("GET USER FAILED: login cannot be null");

        Query query = sessionFactory.getCurrentSession().createQuery("from User u where u.username=:username ");
        query.setParameter("username", username);

        User result = (User) query.uniqueResult();

        if(result==null)
            throw new UsernameNotFoundException("username: " + username + " not found!");

        return result;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public UserDetails getUserByEmail(String email) throws HibernateException, UsernameNotFoundException {
        if (email==null)
            throw new IllegalArgumentException("GET USER FAILED: email cannot be null");

        Query query = sessionFactory.getCurrentSession().createQuery("from User u where u.email=:email ");
        query.setParameter("email", email);

        User result = (User) query.uniqueResult();

        if(result==null)
            throw new UsernameNotFoundException("email: " + email + " not found!");

        return result;
    }
}