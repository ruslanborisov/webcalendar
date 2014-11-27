package com.webcalendar.service;

import com.webcalendar.dao.UserDAO;
import com.webcalendar.domain.User;
import com.webcalendar.domain.UserRole;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import static com.webcalendar.domain.UserRole.EnumRole.ROLE_USER;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    private UserDAO userDAOMock;
    private UserService userService;
    private User user;

    @Before
    public void setUp() {
        UserRole role = new UserRole(1, ROLE_USER);
        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(role);
        user = new User(UUID.randomUUID(), "username", "passw", "bor@ukr.net", true, LocalDate.now(), userRoles);

        userDAOMock = mock(UserDAO.class);
        userService = new UserServiceImpl(userDAOMock);
    }

    @Test
    public void testAddUser() {

        boolean actualResult = userService.addUser(user);

        assertTrue(actualResult);
        verify(userDAOMock, times(1)).addUser(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddUserThrowException() {

        doThrow(new IllegalArgumentException()).when(userDAOMock).addUser(null);
        boolean actualResult = userService.addUser(null);
        assertFalse(actualResult);
        verify(userDAOMock, times(1)).addUser(null);
    }

    @Test
    public void testUpdateUser() {


        boolean actualResult = userService.updateUser(user);

        assertTrue(actualResult);
        verify(userDAOMock, times(1)).updateUser(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateUserThrowException() {

        doThrow(new IllegalArgumentException()).when(userDAOMock).updateUser(null);
        boolean actualResult = userService.updateUser(null);
        assertFalse(actualResult);
        verify(userDAOMock, times(1)).updateUser(null);
    }

    @Test
    public void deleteUser() {

        boolean actualResult = userService.deleteUser(user);
        assertTrue(actualResult);
        verify(userDAOMock, times(1)).deleteUser(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteUserThrowException() {

        doThrow(new IllegalArgumentException()).when(userDAOMock).deleteUser(null);
        boolean actualResult = userService.deleteUser(null);
        assertFalse(actualResult);
        verify(userDAOMock, times(1)).deleteUser(null);
    }

    @Test
    public void testLoadUserById() {

        when(userDAOMock.getUserById(user.getId())).thenReturn(user);
        UserDetails actualResult = userService.loadUserById(user.getId());

        assertEquals(user, actualResult);
        verify(userDAOMock, times(1)).getUserById(user.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadUserByIdThrowException() {

        doThrow(new IllegalArgumentException()).when(userDAOMock).getUserById(null);
        UserDetails actualResult = userService.loadUserById(null);
        assertNull(actualResult);
        verify(userDAOMock, times(1)).getUserById(null);
    }

    @Test
    public void testLoadUserByEmail() {

        when(userDAOMock.getUserByEmail(user.getEmail())).thenReturn(user);
        UserDetails actualResult = userService.loadUserByEmail(user.getEmail());

        assertEquals(user, actualResult);
        verify(userDAOMock, times(1)).getUserByEmail(user.getEmail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadUserByEmailThrowException() {

        doThrow(new IllegalArgumentException()).when(userDAOMock).getUserByEmail(null);
        UserDetails actualResult = userService.loadUserByEmail(null);
        assertNull(actualResult);
        verify(userDAOMock, times(1)).getUserByEmail(null);
    }

    @Test
    public void loadUserByUsername() {

        when(userDAOMock.getUserByUsername(user.getUsername())).thenReturn(user);
        UserDetails actualResult = userService.loadUserByUsername(user.getUsername());

        assertEquals(user, actualResult);
        verify(userDAOMock, times(1)).getUserByUsername(user.getUsername());
    }

    @Test(expected = IllegalArgumentException.class)
    public void loadUserByUsernameThrowException() {

        doThrow(new IllegalArgumentException()).when(userDAOMock).getUserByUsername(null);
        UserDetails actualResult = userService.loadUserByUsername(null);
        assertNull(actualResult);
        verify(userDAOMock, times(1)).getUserByUsername(null);
    }
}
