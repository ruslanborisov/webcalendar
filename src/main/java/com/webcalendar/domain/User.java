package com.webcalendar.domain;

import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name="Users")
public class User implements Serializable, UserDetails {
    private static final long serialVersionUID = 1L;

    @Id
    @Type(type="uuid-char")
    @Column(name = "id", nullable=false)
    private UUID id;

    @Column(name="username", nullable=false, unique = true)
    private String username;

    @Column(name="email", nullable=false, unique = true)
    private String email;

    @Column(name="password", nullable=false)
    private String password;

    @Column(name="active", nullable=false)
    private boolean active;

    @Column(name="registerDate", nullable=false)
    private LocalDate registerDate;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="user_role", joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="role_id"))
    private Set<UserRole> userRoles = new HashSet<>();

    @OneToMany(fetch=FetchType.LAZY, mappedBy="user", cascade=CascadeType.ALL)
    private List<EventAdapter> events = new ArrayList<>();

    public User() {
    }

    public User(UUID id, String username, String password, String email, boolean active,
                LocalDate registerDate, Set<UserRole> userRoles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.active = active;
        this.registerDate = registerDate;
        this.userRoles = userRoles;
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Set<UserRole> getUserRoles() {
        return userRoles;
    }
    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public LocalDate getRegisterDate() {
        return registerDate;
    }
    public void setRegisterDate(LocalDate registerDate) {
        this.registerDate = registerDate;
    }

    public List<EventAdapter> getEvents() {
        return events;
    }
    public void setEvents(List<EventAdapter> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", password="
                + password + ", email=" + email + "]";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return userRoles.stream()
                .map(userRole -> new SimpleGrantedAuthority(userRole.getRole().name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


}